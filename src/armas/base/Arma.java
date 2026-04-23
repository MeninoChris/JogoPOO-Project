package armas.base;

import java.util.Random;
import personagens.Criatura;
import personagens.Inimigo;
import personagens.Jogador;

public abstract class Arma {
    private static final Random RD = new Random();

    private final String nome;
    private final TipoArma tipoArma;
    private final int ataque;
    private final int chance;
    private final int chanceCritico;
    private final double multiplicadorCritico;

    public Arma(String nome, TipoArma tipoArma, int ataque, int chance) {
        this(nome, tipoArma, ataque, chance, 0, 2.0);
    }

    public Arma(
        String nome,
        TipoArma tipoArma,
        int ataque,
        int chance,
        int chanceCritico,
        double multiplicadorCritico
    ) {
        this.nome = nome;
        this.tipoArma = tipoArma;
        this.ataque = ataque;
        this.chance = chance;
        this.chanceCritico = chanceCritico;
        this.multiplicadorCritico = multiplicadorCritico;
    }

    public String getDescricaoCombate() {
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
                + ", "
                + getDescricaoMunicao()
                + ")";

        String efeitoEspecial = getDescricaoEfeitoEspecial();
        if (!efeitoEspecial.isEmpty()) {
            descricao += " - " + efeitoEspecial;
        }

        return descricao;
    }

    public String getDescricaoSelecao() {
        String descricao =
            this.nome
                + " ("
                + getDescricaoCategoria()
                + ", Precisao = "
                + this.chance
                + "%, Critico = "
                + this.chanceCritico
                + "%, Multiplicador Critico = x"
                + this.multiplicadorCritico
                + ", "
                + getDescricaoMunicao()
                + ")";

        String efeitoEspecial = getDescricaoEfeitoEspecial();
        if (!efeitoEspecial.isEmpty()) {
            descricao += " - " + efeitoEspecial;
        }

        return descricao;
    }

    public void golpe(Criatura atacante, Criatura alvo) {
        if (!tentouAcertar(atacante)) {
            atacante.narrar("usou " + this.nome + ", mas o ataque falhou.");
            return;
        }

        int danoCalculado = calcularDano(atacante);
        atacante.narrar("usou " + this.nome + ". Dano previsto: " + danoCalculado + ".");
        aplicarDano(atacante, alvo, danoCalculado);
    }

    protected boolean tentouAcertar() {
        return RD.nextInt(100) < this.chance;
    }

    protected boolean tentouAcertar(Criatura atacante) {
        int chanceFinal = getChanceAcertoFinal(atacante);
        if (chanceFinal == this.chance) {
            return tentouAcertar();
        }
        return RD.nextInt(100) < chanceFinal;
    }

    protected int calcularDano(Criatura atacante) {
        int danoFinal = this.ataque;
        int chanceCriticoFinal = this.chanceCritico;
        double multiplicadorCriticoFinal = this.multiplicadorCritico;

        if (atacante instanceof Jogador jogador) {
            danoFinal += jogador.getBonusDanoComArma(this);
            chanceCriticoFinal += jogador.getBonusChanceCriticoComArma(this);
            multiplicadorCriticoFinal += jogador.getBonusMultiplicadorCriticoComArma(this);
        }

        if (chanceCriticoFinal > 100) {
            chanceCriticoFinal = 100;
        }

        if (RD.nextInt(100) < chanceCriticoFinal) {
            danoFinal = (int) Math.round(danoFinal * multiplicadorCriticoFinal);
            atacante.narrar("conseguiu um acerto critico.");
        }

        return atacante.aplicarPenalidadeDano(danoFinal);
    }

    protected void aplicarDano(Criatura atacante, Criatura alvo, int dano) {
        alvo.tomaDano(dano, atacante);
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

    public String getNomeExibicao() {
        return this.nome;
    }

    public TipoArma getTipoArma() {
        return this.tipoArma;
    }

    protected String getDescricaoEfeitoEspecial() {
        return "";
    }

    public String getDescricaoPreBatalha() {
        String descricao =
            "Precisao "
                + this.chance
                + "%, critico "
                + this.chanceCritico
                + "%, multiplicador x"
                + this.multiplicadorCritico
                + ", "
                + getDescricaoMunicao();

        String efeitoEspecial = getDescricaoEfeitoEspecial();
        if (!efeitoEspecial.isEmpty()) {
            descricao += ", efeito: " + efeitoEspecial;
        }

        return descricao;
    }

    public int calcularDanoCriticoGarantido(Criatura atacante) {
        int danoBase = this.ataque;
        double multiplicadorFinal = this.multiplicadorCritico;

        if (atacante instanceof Jogador jogador) {
            danoBase += jogador.getBonusDanoComArma(this);
            multiplicadorFinal += jogador.getBonusMultiplicadorCriticoComArma(this);
        }

        atacante.narrar("forcou um acerto critico com " + this.nome + ".");
        return atacante.aplicarPenalidadeDano((int) Math.round(danoBase * multiplicadorFinal));
    }

    public int getBonusBloqueioTotal() {
        return 0;
    }

    public int getBonusParry() {
        return 0;
    }

    public double getMultiplicadorReducaoDefensiva() {
        return 0.5;
    }

    public boolean usaMunicaoLimitada() {
        return false;
    }

    public String getResumoMunicaoAtual() {
        return "";
    }

    protected String getDescricaoMunicao() {
        return "Municao = infinita";
    }

    private int getChanceAcertoFinal(Criatura atacante) {
        int chanceFinal = this.chance;
        if (atacante instanceof Jogador jogador) {
            chanceFinal += jogador.getBonusChanceAcertoComArma(this);
        }

        if (chanceFinal > 100) {
            return 100;
        }

        if (chanceFinal < 0) {
            return 0;
        }

        return chanceFinal;
    }

    private String getDescricaoCategoria() {
        if (this.tipoArma == TipoArma.CURTA_DISTANCIA) {
            return "Curta distancia";
        }
        return "Longa distancia";
    }
}
