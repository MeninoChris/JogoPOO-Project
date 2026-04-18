package personagens;

import combate.LogCombate;

public abstract class Criatura {
    private final String nome;
    private int vidaMaxima;
    private int vida;
    private String efeitoDanoContinuo;
    private int danoContinuoPorTurno;
    private int duracaoDanoContinuo;
    private String efeitoReducaoDano;
    private int reducaoDanoTemporaria;
    private int duracaoReducaoDano;

    public Criatura(String nome, int vida) {
        this.nome = nome;
        this.vidaMaxima = vida;
        this.vida = vida;
    }

    public String getNome() {
        return this.nome;
    }

    public abstract String getCodinome();

    public abstract void fazAtaque(Criatura alvo);

    public abstract void fraseApresentacao();

    public abstract void fraseMorte();

    protected void falar(String frase) {
        LogCombate.fala(getCodinome(), frase);
    }

    public void narrar(String mensagem) {
        LogCombate.narracao(getCodinome(), mensagem);
    }

    public void tomaDano(int dano) {
        narrar("sofreu " + dano + " de dano.");
        this.vida -= dano;
        if (this.vida < 0) {
            this.vida = 0;
        }
    }

    public void curar(int cura) {
        int vidaAntes = this.vida;
        this.vida += cura;
        if (this.vida > this.vidaMaxima) {
            this.vida = this.vidaMaxima;
        }
        narrar("recuperou " + (this.vida - vidaAntes) + " de vida.");
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    public void mostrarVida() {
        narrar("esta com " + this.vida + " de vida.");
    }

    public void restaurarVidaTotal() {
        this.vida = this.vidaMaxima;
        narrar("teve a vida restaurada para " + this.vidaMaxima + ".");
    }

    public void aumentarVidaMaxima(int aumento) {
        this.vidaMaxima += aumento;
        narrar("teve a vida maxima aumentada em " + aumento + ".");
    }

    public void tomaDano(int dano, Criatura atacante) {
        tomaDano(dano);
    }

    public void aplicarDanoContinuo(String nomeEfeito, int danoPorTurno, int duracao) {
        this.efeitoDanoContinuo = nomeEfeito;
        this.danoContinuoPorTurno = danoPorTurno;
        this.duracaoDanoContinuo = duracao;
        narrar("foi afetado por " + nomeEfeito + " por " + duracao + " turnos.");
    }

    public void aplicarReducaoDano(String nomeEfeito, int reducao, int duracao) {
        this.efeitoReducaoDano = nomeEfeito;
        this.reducaoDanoTemporaria = reducao;
        this.duracaoReducaoDano = duracao;
        narrar("foi afetado por " + nomeEfeito + " e perdera " + reducao + " de dano por " + duracao + " turnos.");
    }

    public void processarInicioTurno() {
        if (this.duracaoDanoContinuo > 0) {
            narrar("esta sofrendo com " + this.efeitoDanoContinuo + " e recebera " + this.danoContinuoPorTurno + " de dano.");
            tomaDano(this.danoContinuoPorTurno);
            this.duracaoDanoContinuo--;
            if (this.duracaoDanoContinuo == 0) {
                narrar("se livrou do efeito " + this.efeitoDanoContinuo + ".");
                this.efeitoDanoContinuo = null;
                this.danoContinuoPorTurno = 0;
            }
        }
    }

    public void processarFimTurno() {
        if (this.duracaoReducaoDano > 0) {
            this.duracaoReducaoDano--;
            if (this.duracaoReducaoDano == 0) {
                narrar("nao esta mais sob efeito de " + this.efeitoReducaoDano + ".");
                this.efeitoReducaoDano = null;
                this.reducaoDanoTemporaria = 0;
            }
        }
    }

    public int aplicarPenalidadeDano(int danoBase) {
        int danoFinal = danoBase;
        if (this.duracaoReducaoDano > 0) {
            danoFinal -= this.reducaoDanoTemporaria;
            narrar("teve seu dano reduzido por " + this.efeitoReducaoDano + ".");
        }
        if (danoFinal < 0) {
            danoFinal = 0;
        }
        return danoFinal;
    }

    public int getVidaAtual() {
        return this.vida;
    }

    public int getVidaMaxima() {
        return this.vidaMaxima;
    }
}
