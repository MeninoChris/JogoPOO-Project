package itens;

import personagens.Jogador;

public class Fruta extends Consumivel {
    private final int cura;

    public Fruta(String nome, int cura) {
        super(nome, "Recupera " + cura + " de vida", TipoConsumivel.CURA);
        this.cura = cura;
    }

    @Override
    public void usar(Jogador jogador) {
        jogador.curar(this.cura);
    }
}
