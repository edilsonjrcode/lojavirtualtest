package serverest;

import dto.LoginDTO;
import io.restassured.http.ContentType;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class LoginTest extends BaseTest {

    @Test
    public void deveFazerLoginComSucesso() {
        LoginDTO loginDto = new LoginDTO("fulano@qa.com", "teste");
        given()
                .contentType(ContentType.JSON)
                .body(loginDto)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", containsString("Login realizado com sucesso")).log().all();
    }

    @Test
    public void deveFalharLoginComEmailEmBranco() {
        LoginDTO loginComSenhaInvalida = new LoginDTO("", "senhaInvalida");
        given()
                .contentType(ContentType.JSON)
                .body(loginComSenhaInvalida)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", containsString("email não pode ficar em branco"));
    }

    @Test
    public void deveFalharLoginComSenhaEmBranco() {
        LoginDTO loginComSenhaInvalida = new LoginDTO("fulano@qa.com", "");
        given()
                .contentType(ContentType.JSON)
                .body(loginComSenhaInvalida)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("password", containsString("password não pode ficar em branco"));
    }

    @Test
    public void deveFalharLoginComEmailInvalido() {
        LoginDTO loginComSenhaInvalida = new LoginDTO("ciclano@qa.com", "senhaInvalida");
        given()
                .contentType(ContentType.JSON)
                .body(loginComSenhaInvalida)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", containsString("Email e/ou senha inválidos"));
    }

    @Test
    public void deveFalharLoginComSenhaInvalida() {
        LoginDTO loginComSenhaInvalida = new LoginDTO("fulano@qa.com", "senhaInvalida");
        given()
                .contentType(ContentType.JSON)
                .body(loginComSenhaInvalida)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", containsString("Email e/ou senha inválidos"));
    }
}
