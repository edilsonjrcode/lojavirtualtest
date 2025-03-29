package serverest;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import org.junit.jupiter.api.Test;

import dto.ProdutoDTO;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.apache.http.HttpStatus;

public class ProdutoTest extends BaseTest {
    @Test
    public void deveListarProdutos() {
        when()
                .get("/produtos")
                .then()
                .statusCode(200)
                .body("produtos", not(empty()));
    }

    @Test
    public void naoDeveCadastrarProdutoSemToken() {
        String produto = "{\"nome\":\"Teclado\",\"preco\":200,\"descricao\":\"Teclado mecânico RGB\",\"quantidade\":5}";

        given()
                .contentType(ContentType.JSON)
                .body(produto)
                .when()
                .post("/produtos")
                .then()
                .statusCode(401)
                .body("message", containsString("Token de acesso ausente"));
    }

    @Test
    public void naoDeveCadastrarProdutoComMesmoNome() {
        ProdutoDTO produtoComMesmoNome = new ProdutoDTO("Logitech MX Vertical", 200, "Mouse", 5);

        given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .body(produtoComMesmoNome)
                .when()
                .post("/produtos")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", containsString("Já existe produto com esse nome"));
    }

    @Test
    public void deveCadastrarProduto() {
        ProdutoDTO produto = new ProdutoDTO();
        given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .body(produto)
                .when()
                .post("/produtos")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", containsString("Cadastro realizado com sucesso"));
    }

    @Test
    public void deveEncontrarProdutoPeloId() {
        given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .params("_id", "BeeJh5lz3k6kSIzA")
                .when()
                .get("/produtos/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("produtos[0]", hasKey("nome"))
                .body("produtos[0]", hasKey("preco"))
                .body("produtos[0]", hasKey("descricao"))
                .body("produtos[0]", hasKey("quantidade"))
                .body("produtos[0]", hasKey("_id"));
    }

    @Test
    public void deveDeletarProdutoPeloId() {
        given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .params("_id", "BeeJh5lz3k6kSIzA")
                .when()
                .delete("/produtos/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", containsString("Registro excluído com sucesso | Nenhum registro excluído"));

    }

    @Test
    public void naoDeveDeletarProdutoPeloIdInexistente() {
        given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .params("_id", "eee")
                .when()
                .delete("/produtos/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", containsString("Nenhum registro excluído"));

    }

    @Test
    public void naoDeveEncontrarProdutoComIdInexistente() {
        String id = "1d1n3x1st3nt3";
        given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .params("_id", id)
                .when()
                .get("/produtos/")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", containsString("Produto não encontrado"));
    }

    @Test
    public void deveEditarProdutoPeloId() {
        String id = "K6leHdftCeOJj8BJ";
        given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .params("_id", id)
                .when()
                .put("/produtos/")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", containsString("Produto não encontrado"));
    }
}
