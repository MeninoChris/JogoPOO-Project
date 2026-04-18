package armas;

import java.util.Random;
import personagens.Criatura;

public abstract class Arma {
    private final String nome;
    private final int ataque;
    private final int chance;

    public Arma(String nome, int ataque, int chance) {
        this.nome = nome;
        this.ataque = ataque;
        this.chance = chance;
    }

    public void descricao() {
        System.out.println(this.nome + " (Dano = " + this.ataque + ", Chance = " + this.chance + "%)");
    }

    public void golpe(Criatura alvo) {
        Random rd = new Random();
        int sorteio = rd.nextInt(100);

        if (sorteio < this.chance) {
            alvo.tomaDano(this.ataque);
        } else {
            System.out.println("Ataque falhou.");
        }
    }
}
