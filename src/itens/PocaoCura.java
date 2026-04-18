package itens;

import personagens.Jogador;

public class PocaoCura extends Consumivel {
    private final int cura;
    private final int escudoTemporario;

    public PocaoCura(int cura) {
        this(cura, 60);
    }

    public PocaoCura(int cura, int escudoTemporario) {
        super(
            "Pocao de Cura",
            "Recupera " + cura + " de vida e concede " + escudoTemporario + " de escudo temporario",
            TipoConsumivel.CURA
        );
        this.cura = cura;
        this.escudoTemporario = escudoTemporario;
    }

    @Override
    public void usar(Jogador jogador) {
        jogador.curar(this.cura);
        jogador.aplicarEscudoTemporario(this.escudoTemporario);
    }
}
