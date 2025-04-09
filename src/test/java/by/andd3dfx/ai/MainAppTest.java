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

    @Test
    public void startOllama() {
        try ( // container {
              OllamaContainer ollama = new OllamaContainer("ollama/ollama:0.6.5")
              // }
        ) {
            ollama.start();

            String version = given().baseUri(ollama.getEndpoint())
                    .get("/api/version").jsonPath().get("version");
            assertThat(version).isEqualTo("0.6.5");
        }
    }

    @Test
    public void startOllamaDownloadModelAndCommitToImage() throws IOException, InterruptedException {
        String newImageName = "tc-ollama-all-minilm";
        try (OllamaContainer ollama = new OllamaContainer("ollama/ollama:0.6.5")) {
            ollama.start();

            // pullModel
            ollama.execInContainer("ollama", "pull", "all-minilm");

            String modelName = given()
                    .baseUri(ollama.getEndpoint())
                    .get("/api/tags")
                    .jsonPath()
                    .getString("models[0].name");
            assertThat(modelName).contains("minilm");

            // commitToImage
            ollama.commitToImage(newImageName);
        }
        try (
                // substitute
                OllamaContainer ollama = new OllamaContainer(
                        DockerImageName.parse(newImageName)
                                .asCompatibleSubstituteFor("ollama/ollama:0.6.5")
                )
        ) {
            ollama.start();
            String modelName = given()
                    .baseUri(ollama.getEndpoint())
                    .get("/api/tags")
                    .jsonPath()
                    .getString("models[0].name");
            assertThat(modelName).contains("minilm");
        }
    }
}
