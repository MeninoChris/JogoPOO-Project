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
        narrar("usou " + getNomeAtaqueBasico() + " e causara " + this.ataque + " de dano.");
        alvo.tomaDano(this.ataque, this);
    }

    @Override
    public void tomaDano(int dano) {
        int danoReduzido = this.defesa.danoReduzido(dano);
        super.tomaDano(danoReduzido);
    }

    public void tomaDanoIgnorandoDefesa(int dano) {
        narrar("teve a defesa ignorada.");
        super.tomaDano(dano);
    }

    @Override
    public String getCodinome() {
        return "[INIMIGO " + getNome() + "]";
    }

    protected String getNomeAtaqueBasico() {
        return "Ataque Sombrio";
    }

    protected int getAtaqueBase() {
        return this.ataque;
    }
}
