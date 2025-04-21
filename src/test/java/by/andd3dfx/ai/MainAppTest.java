package by.andd3dfx.ai;

import by.andd3dfx.ai.controller.dto.MsgRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainAppTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    void loadContextAndAskQuestion() {
        var response = given()
                .body(MsgRequest.builder().prompt("Write code of bubble sort using Java").build())
                .when()
                .contentType(ContentType.JSON)
                .post("/api/generate")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().asPrettyString();

        log.info("Got an answer: {}", response);
        assertThat(response).isNotBlank();
    }
}
