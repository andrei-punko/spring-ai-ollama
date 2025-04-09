package by.andd3dfx.ai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MainAppTest {

    private final String OLLAMA_IMAGE_VERSION = "0.6.5";
    private final String OLLAMA_IMAGE_NAME = "ollama/ollama:" + OLLAMA_IMAGE_VERSION;

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
        String newImageName = "tc-ollama-all-minilm";
        try (var ollama = new OllamaContainer(OLLAMA_IMAGE_NAME)) {
            ollama.start();

            // pull model
            ollama.execInContainer("ollama", "pull", "all-minilm");

            checkModelName(ollama);

            // commit to image
            ollama.commitToImage(newImageName);
        }
        try (   // substitute
                var ollama = new OllamaContainer(
                        DockerImageName.parse(newImageName)
                                .asCompatibleSubstituteFor(OLLAMA_IMAGE_NAME)
                )
        ) {
            ollama.start();

            checkModelName(ollama);
        }
    }

    private void checkModelName(OllamaContainer ollama) {
        String modelName = given()
                .baseUri(ollama.getEndpoint())
                .get("/api/tags")
                .jsonPath()
                .getString("models[0].name");
        assertThat(modelName).contains("minilm");
    }
}
