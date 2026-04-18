package armas;

import java.util.Random;
import personagens.Criatura;

public abstract class Arma {
    private final String nome;
    private final int ataque;
    private final int chance;
    private final int chanceCritico;
    private final double multiplicadorCritico;

    public Arma(String nome, int ataque, int chance) {
        this(nome, ataque, chance, 0, 2.0);
    }

    public Arma(String nome, int ataque, int chance, int chanceCritico, double multiplicadorCritico) {
        this.nome = nome;
        this.ataque = ataque;
        this.chance = chance;
        this.chanceCritico = chanceCritico;
        this.multiplicadorCritico = multiplicadorCritico;
    }

    public void descricao() {
        System.out.println(
            this.nome
                + " (Dano = "
                + this.ataque
                + ", Chance = "
                + this.chance
                + "%, Critico = "
                + this.chanceCritico
                + "%, Multiplicador Critico = x"
                + this.multiplicadorCritico
                + ")"
        );
    }

    public void golpe(Criatura atacante, Criatura alvo) {
        Random rd = new Random();
        int sorteio = rd.nextInt(100);

        if (sorteio < this.chance) {
            int danoFinal = this.ataque;
            int chanceCriticoFinal = this.chanceCritico;
            double multiplicadorCriticoFinal = this.multiplicadorCritico;

            if (atacante instanceof personagens.Jogador jogador) {
                danoFinal += jogador.getBonusDano();
                chanceCriticoFinal += jogador.getBonusChanceCritico();
                multiplicadorCriticoFinal += jogador.getBonusMultiplicadorCritico();
            }

            if (chanceCriticoFinal > 100) {
                chanceCriticoFinal = 100;
            }

            int sorteioCritico = rd.nextInt(100);
            if (sorteioCritico < chanceCriticoFinal) {
                danoFinal = (int) Math.round(danoFinal * multiplicadorCriticoFinal);
                System.out.println("Acerto critico!");
            }

            alvo.tomaDano(danoFinal);
        } else {
            System.out.println("Ataque falhou.");
        }
    }
}
