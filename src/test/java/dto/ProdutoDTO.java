package dto;

import com.github.javafaker.Faker;

public class ProdutoDTO {

    private String nome;
    private Integer preco;
    private String descricao;
    private Integer quantidade;

    public ProdutoDTO() {
        Faker faker = new Faker();
        this.nome = faker.commerce().productName(); // Nome realista para produtos
        this.preco = faker.number().numberBetween(1, 1000); // Preço realista
        this.descricao = faker.lorem().sentence(); // Descrição mais realista
        this.quantidade = faker.number().numberBetween(1, 1000); // Categoria do produto
    }

    public ProdutoDTO(String nome, Integer preco, String descricao, Integer quantidade) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getPreco() {
        return preco;
    }

    public void setPreco(Integer preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

}
