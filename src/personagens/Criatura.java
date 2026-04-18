package personagens;

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

    public abstract void fazAtaque(Criatura alvo);

    public abstract void fraseApresentacao();

    public abstract void fraseMorte();

    public void tomaDano(int dano) {
        System.out.println(this.nome + " toma dano de " + dano);
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
        System.out.println(
            this.nome + " recuperou " + (this.vida - vidaAntes) + " de vida."
        );
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    public void mostrarVida() {
        System.out.println(this.nome + " / Vida = " + this.vida);
    }
}
