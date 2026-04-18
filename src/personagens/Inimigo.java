package personagens;

import defesas.Defesa;

public abstract class Inimigo extends Criatura {
    private final int ataque;
    private final Defesa defesa;

    public Inimigo(String nome, int vida, int ataque) {
        this(nome, vida, ataque, new Defesa());
    }

    public Inimigo(String nome, int vida, int ataque, Defesa defesa) {
        super(nome, vida);
        this.ataque = ataque;
        this.defesa = defesa;
    }

    @Override
    public void fazAtaque(Criatura alvo) {
        alvo.tomaDano(this.ataque);
    }

    @Override
    public void tomaDano(int dano) {
        int danoReduzido = this.defesa.danoReduzido(dano);
        super.tomaDano(danoReduzido);
    }

    public void tomaDanoIgnorandoDefesa(int dano) {
        System.out.println("A defesa de " + this.getNome() + " foi ignorada.");
        super.tomaDano(dano);
    }
}
