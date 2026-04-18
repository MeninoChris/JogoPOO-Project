package personagens;

import combate.LogCombate;

public abstract class Criatura {
    private final String nome;
    private final int vidaMaxima;
    private int vida;

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

    public void tomaDano(int dano, Criatura atacante) {
        tomaDano(dano);
    }
}
