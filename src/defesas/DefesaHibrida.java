package defesas;

import java.util.Random;

public class DefesaHibrida extends Defesa {
    private static final Random RD = new Random();

    private final int reducao;
    private final int chanceEsquiva;

    public DefesaHibrida(int reducao, int chanceEsquiva) {
        this.reducao = reducao;
        this.chanceEsquiva = chanceEsquiva;
    }

    @Override
    public int danoReduzido(int danoOriginal) {
        if (RD.nextInt(100) < this.chanceEsquiva) {
            System.out.println("O inimigo esquivou completamente do golpe.");
            return 0;
        }

        int danoFinal = danoOriginal - this.reducao;
        if (danoFinal < 0) {
            danoFinal = 0;
        }

        System.out.println("A defesa hibrida bloqueou parte do dano.");
        return danoFinal;
    }
}
