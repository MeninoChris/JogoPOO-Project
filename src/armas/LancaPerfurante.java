package armas;

import personagens.Criatura;

public class LancaPerfurante extends Arma {
    private static final int CHANCE_IGNORAR_DEFESA = 35;

    public LancaPerfurante() {
        super("Lanca Perfurante", TipoArma.LONGA_DISTANCIA, 85, 80, 15, 2.0);
    }

    @Override
    protected void aplicarDano(Criatura atacante, Criatura alvo, int dano) {
        if (sortearChance(CHANCE_IGNORAR_DEFESA)) {
            atacante.narrar("ativou o efeito perfurante e ignorara a defesa do alvo.");
            aplicarDanoIgnorandoDefesa(alvo, dano);
            return;
        }

        super.aplicarDano(atacante, alvo, dano);
    }

    @Override
    protected String getDescricaoEfeitoEspecial() {
        return CHANCE_IGNORAR_DEFESA + "% de ignorar defesa";
    }
}
