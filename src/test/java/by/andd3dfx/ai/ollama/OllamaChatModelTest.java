package by.andd3dfx.ai.ollama;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import static by.andd3dfx.ai.ollama.OllamaContainerTest.OLLAMA_IMAGE_NAME;
import static by.andd3dfx.ai.ollama.OllamaContainerTest.TINYLLAMA_IMAGE_NAME;
import static by.andd3dfx.ai.ollama.OllamaContainerTest.TINYLLAMA_MODEL_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class OllamaChatModelTest {

    private static final Logger log = LoggerFactory.getLogger(OllamaChatModelTest.class);

    @Container
    static OllamaContainer ollama = new OllamaContainer(
            DockerImageName.parse(TINYLLAMA_IMAGE_NAME)
                    .asCompatibleSubstituteFor(OLLAMA_IMAGE_NAME)
    );

    @Test
    void askModel() {
        var model = OllamaChatModel.builder()
                .defaultOptions(OllamaOptions.builder().model(TINYLLAMA_MODEL_NAME).build())
                .ollamaApi(new OllamaApi(baseUrl()))
                .build();

        log.info("Ask Ollama: `Tell me about Belarus`");
        String answer = model.call("Tell me about Belarus");
        assertThat(answer).isNotBlank();
        log.info("Ollama answer:\n{}", answer);
    }

    static String baseUrl() {
        return String.format("http://%s:%d", ollama.getHost(), ollama.getFirstMappedPort());
    }
}
