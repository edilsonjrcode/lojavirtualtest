package serverest;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dto.LoginDTO;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.apache.http.HttpStatus;

public class BaseTest {
    public static String TOKEN;

    @BeforeAll
    public static void setup() {
        LoginDTO loginDto = new LoginDTO("fulano@qa.com", "teste");
        RestAssured.baseURI = "http://localhost:3000";
        TOKEN = given()
                .contentType(ContentType.JSON)
                .body(loginDto)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", containsString("Login realizado com sucesso"))
                .extract().jsonPath().get("authorization");
    }
}
