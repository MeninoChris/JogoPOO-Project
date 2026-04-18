package itens;

import personagens.Jogador;

public class PergaminhoForca extends Consumivel {
    private final int bonusDano;

    public PergaminhoForca(int bonusDano) {
        super("Pergaminho de Forca", "Aumenta o dano em " + bonusDano);
        this.bonusDano = bonusDano;
    }

    @Override
    public void usar(Jogador jogador) {
        jogador.adicionarBonusDano(this.bonusDano);
    }
}
