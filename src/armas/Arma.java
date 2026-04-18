package armas;

import java.util.Random;
import personagens.Criatura;
import personagens.Inimigo;

public abstract class Arma {
    private static final Random RD = new Random();

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
        String descricao =
            this.nome
                + " (Dano = "
                + this.ataque
                + ", Chance = "
                + this.chance
                + "%, Critico = "
                + this.chanceCritico
                + "%, Multiplicador Critico = x"
                + this.multiplicadorCritico
                + ")";

        String efeitoEspecial = getDescricaoEfeitoEspecial();
        if (!efeitoEspecial.isEmpty()) {
            descricao += " - " + efeitoEspecial;
        }

        System.out.println(descricao);
    }

    public void golpe(Criatura atacante, Criatura alvo) {
        if (!tentouAcertar()) {
            System.out.println("Ataque falhou.");
            return;
        }

        aplicarDano(atacante, alvo, calcularDano(atacante));
    }

    protected boolean tentouAcertar() {
        return RD.nextInt(100) < this.chance;
    }

    protected int calcularDano(Criatura atacante) {
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

        if (RD.nextInt(100) < chanceCriticoFinal) {
            danoFinal = (int) Math.round(danoFinal * multiplicadorCriticoFinal);
            System.out.println("Acerto critico!");
        }

        return danoFinal;
    }

    protected void aplicarDano(Criatura atacante, Criatura alvo, int dano) {
        alvo.tomaDano(dano);
    }

    protected void aplicarDanoIgnorandoDefesa(Criatura alvo, int dano) {
        if (alvo instanceof Inimigo inimigo) {
            inimigo.tomaDanoIgnorandoDefesa(dano);
            return;
        }

        alvo.tomaDano(dano);
    }

    protected boolean sortearChance(int chancePercentual) {
        return RD.nextInt(100) < chancePercentual;
    }

    protected String getNome() {
        return this.nome;
    }

    protected String getDescricaoEfeitoEspecial() {
        return "";
    }
}
