package itens;

import personagens.Jogador;

public class Fruta extends Consumivel {
    private final int cura;
    private final int escudoTemporario;

    public Fruta(String nome, int cura) {
        this(nome, cura, 15);
    }

    public Fruta(String nome, int cura, int escudoTemporario) {
        super(
            nome,
            "Recupera " + cura + " de vida e concede " + escudoTemporario + " de escudo temporario",
            TipoConsumivel.CURA
        );
        this.cura = cura;
        this.escudoTemporario = escudoTemporario;
    }

    @Override
    public void usar(Jogador jogador) {
        jogador.aplicarRecuperacao(this.cura, this.escudoTemporario);
    }
}
