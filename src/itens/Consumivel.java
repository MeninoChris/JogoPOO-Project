package itens;

import personagens.Jogador;

public abstract class Consumivel {
    private final String nome;
    private final String descricao;

    public Consumivel(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getDescricaoCompleta() {
        return this.nome + " - " + this.descricao;
    }

    public abstract void usar(Jogador jogador);
}
