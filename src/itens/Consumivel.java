package itens;

import personagens.Jogador;

public abstract class Consumivel {
    private final String nome;
    private final String descricao;
    private final TipoConsumivel tipoConsumivel;

    public Consumivel(String nome, String descricao, TipoConsumivel tipoConsumivel) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipoConsumivel = tipoConsumivel;
    }

    public String getDescricaoCompleta() {
        return this.nome + " - " + this.descricao;
    }

    public String getNomeExibicao() {
        return this.nome;
    }

    public String getDescricaoPreBatalha() {
        return this.descricao;
    }

    public TipoConsumivel getTipoConsumivel() {
        return this.tipoConsumivel;
    }

    public abstract void usar(Jogador jogador);
}
