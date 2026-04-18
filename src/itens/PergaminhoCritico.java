package itens;

import personagens.Jogador;

public class PergaminhoCritico extends Consumivel {
    private final int bonusChanceCritico;
    private final double bonusMultiplicadorCritico;

    public PergaminhoCritico(int bonusChanceCritico, double bonusMultiplicadorCritico) {
        super(
            "Pergaminho Critico",
            "Aumenta a chance critica em "
                + bonusChanceCritico
                + "% e o multiplicador critico em +"
                + bonusMultiplicadorCritico,
            TipoConsumivel.APRIMORAMENTO
        );
        this.bonusChanceCritico = bonusChanceCritico;
        this.bonusMultiplicadorCritico = bonusMultiplicadorCritico;
    }

    @Override
    public void usar(Jogador jogador) {
        jogador.adicionarBonusCritico(this.bonusChanceCritico, this.bonusMultiplicadorCritico);
    }
}
