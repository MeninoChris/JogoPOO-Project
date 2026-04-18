package itens;

import personagens.Jogador;

public class PocaoCura extends Consumivel {
    private final int cura;

    public PocaoCura(int cura) {
        super("Pocao de Cura", "Recupera " + cura + " de vida", TipoConsumivel.CURA);
        this.cura = cura;
    }

    @Override
    public void usar(Jogador jogador) {
        jogador.curar(this.cura);
    }
}
