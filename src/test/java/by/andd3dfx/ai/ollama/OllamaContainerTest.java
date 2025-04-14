package by.andd3dfx.ai.ollama;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class OllamaContainerTest {

    private static final Logger log = LoggerFactory.getLogger(OllamaContainerTest.class);

    public static final String OLLAMA_IMAGE_VERSION = "0.6.5";
    public static final String OLLAMA_IMAGE_NAME = "ollama/ollama:" + OLLAMA_IMAGE_VERSION;
    public static String TINYLLAMA_MODEL_NAME = "tinyllama";
    public static String TINYLLAMA_IMAGE_NAME = "tc-ollama-" + TINYLLAMA_MODEL_NAME;

    @Test
    public void startOllama() {
        try (var ollama = new OllamaContainer(OLLAMA_IMAGE_NAME)) {
            ollama.start();

            checkApiVersion(ollama);
        }
    }

    private void checkApiVersion(OllamaContainer ollama) {
        String apiVersion = given()
                .baseUri(ollama.getEndpoint())
                .get("/api/version")
                .jsonPath().get("version");
        assertThat(apiVersion).isEqualTo(OLLAMA_IMAGE_VERSION);
    }

    @Test
    public void startOllamaDownloadModelAndCommitToImage() throws IOException, InterruptedException {
        try (var ollama = new OllamaContainer(OLLAMA_IMAGE_NAME)) {
            ollama.start();

            // pull model
            ollama.execInContainer("ollama", "pull", TINYLLAMA_MODEL_NAME);

            checkModelName(ollama);

            // commit to image
            ollama.commitToImage(TINYLLAMA_IMAGE_NAME);
        }
        try (   // substitute
                var ollama = new OllamaContainer(
                        DockerImageName.parse(TINYLLAMA_IMAGE_NAME)
                                .asCompatibleSubstituteFor(OLLAMA_IMAGE_NAME)
                )
        ) {
            ollama.start();

            checkModelName(ollama);
        }
    }

    @Test
    public void startOllamaAndAskModel() {
        try (var ollama = new OllamaContainer(
                DockerImageName.parse(TINYLLAMA_IMAGE_NAME).asCompatibleSubstituteFor(OLLAMA_IMAGE_NAME)
        )) {
            ollama.start();

            checkModelName(ollama);
            askModel(ollama);
        }
    }

    private void checkModelName(OllamaContainer ollama) {
        String modelName = given()
                .baseUri(ollama.getEndpoint())
                .get("/api/tags")
                .jsonPath()
                .getString("models[0].name");
        assertThat(modelName).contains(TINYLLAMA_MODEL_NAME);
    }

    private void askModel(OllamaContainer ollama) {
        var model = OllamaChatModel.builder()
                .defaultOptions(OllamaOptions.builder().model(TINYLLAMA_MODEL_NAME).build())
                .ollamaApi(new OllamaApi(ollama.getEndpoint()))
                .build();

        log.info("Ask Ollama: `Tell me about Belarus`");
        String answer = model.call("Tell me about Belarus");
        assertThat(answer).isNotBlank();
        log.info("Ollama answer:\n{}", answer);
    }
}
