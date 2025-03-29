package serverest;

import com.github.javafaker.Faker;
import dto.UsuarioDTO;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UsuarioTest extends BaseTest {

    @Test
    public void deveListarUsuarios() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("usuarios", not(empty()));
    }

    @Test
    public void deveCadastrarNovoUsuario() {
        UsuarioDTO usuarioDTO = new UsuarioDTO("true");
        given()
                .contentType(ContentType.JSON)
                .body(usuarioDTO)
                .when()
                .post("/usuarios")
                .then().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", containsString("Cadastro realizado com sucesso"));
    }

    @Test
    public void naoDeveCadastrarUsuarioComEmailDuplicado() {
        UsuarioDTO usuarioExistente = new UsuarioDTO("Fulano da Silva", "fulano@qa.com", "teste", "true");
        given()
                .contentType(ContentType.JSON)
                .body(usuarioExistente)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", containsString("Este email já está sendo usado"));
    }
}
