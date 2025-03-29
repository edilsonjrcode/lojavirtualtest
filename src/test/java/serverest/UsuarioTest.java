package serverest;

import dto.UsuarioDTO;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

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
    public void naoDeveListarUsuariosComFiltroEmailInvalido() {
        given()
                .contentType(ContentType.JSON)
                .params("email", "email.invalido.com.br")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", containsString("email deve ser um email válido"));
    }

    @Test
    public void deveListarUsuariosComFiltroEmail() {
        given()
                .contentType(ContentType.JSON)
                .params("email", "fulano@qa.com")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("fulano@qa.com"));
    }

    @Test
    public void deveCadastrarNovoUsuario() {
        UsuarioDTO usuarioDTO = new UsuarioDTO("true");
        given()
                .contentType(ContentType.JSON)
                .body(usuarioDTO)
                .when()
                .post("/usuarios")
                .then()
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

    @Test
    public void naoDeveCadastrarUsuarioSemBody() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("nome é obrigatório"))
                .body(containsString("email é obrigatório"))
                .body(containsString("password é obrigatório"))
                .body(containsString("administrador é obrigatório"));
    }

    @Test
    public void naoDeveCadastrarUsuarioSemNome() {
        UsuarioDTO usuarioDTO = new UsuarioDTO("true");

        String userEmail = usuarioDTO.getEmail();
        String userSenha = usuarioDTO.getPassword();
        String userAdministrador = usuarioDTO.getAdministrador();

        String body = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\",\"administrador\":\"%s\"}",
                userEmail, userSenha,
                userAdministrador);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("nome é obrigatório"));
    }

    @Test
    public void naoDeveCadastrarUsuarioSemEmail() {
        UsuarioDTO usuarioDTO = new UsuarioDTO("true");

        String userNome = usuarioDTO.getNome();
        String userSenha = usuarioDTO.getPassword();
        String userAdministrador = usuarioDTO.getAdministrador();

        String body = String.format(
                "{\"nome\":\"%s\",\"password\":\"%s\",\"administrador\":\"%s\"}",
                userNome, userSenha,
                userAdministrador);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("email é obrigatório"));
    }

    @Test
    public void naoDeveCadastrarUsuarioSemPassword() {
        UsuarioDTO usuarioDTO = new UsuarioDTO("true");

        String userNome = usuarioDTO.getNome();
        String userEmail = usuarioDTO.getEmail();
        String userAdministrador = usuarioDTO.getAdministrador();

        String body = String.format(
                "{\"nome\":\"%s\",\"email\":\"%s\",\"administrador\":\"%s\"}",
                userNome, userEmail,
                userAdministrador);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("password é obrigatório"));
    }

    @Test
    public void naoDeveCadastrarUsuarioSemAdministrador() {
        UsuarioDTO usuarioDTO = new UsuarioDTO("true");

        String userNome = usuarioDTO.getNome();
        String userEmail = usuarioDTO.getEmail();
        String userSenha = usuarioDTO.getPassword();

        String body = String.format(
                "{\"nome\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                userNome, userEmail,
                userSenha);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("administrador é obrigatório"));
    }

    @Test
    public void naoDeveCadastrarUsuarioVazio() {
        UsuarioDTO usuarioDTO = new UsuarioDTO("", "", "", "");
        given()
                .contentType(ContentType.JSON)
                .body(usuarioDTO)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("nome não pode ficar em branco"))
                .body(containsString("email não pode ficar em branco"))
                .body(containsString("password não pode ficar em branco"))
                .body(containsString("administrador deve ser 'true' ou 'false'"));
    }
}
