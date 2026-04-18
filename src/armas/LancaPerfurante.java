package armas;

import personagens.Criatura;

public class LancaPerfurante extends Arma {
    private static final int CHANCE_IGNORAR_DEFESA = 35;

    public LancaPerfurante() {
        super("Lanca Perfurante", 85, 80, 15, 2.0);
    }

    @Override
    protected void aplicarDano(Criatura atacante, Criatura alvo, int dano) {
        if (sortearChance(CHANCE_IGNORAR_DEFESA)) {
            System.out.println("Golpe perfurante ignorou a defesa!");
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
