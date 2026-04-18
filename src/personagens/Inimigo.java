package personagens;

import defesas.Defesa;

public abstract class Inimigo extends Criatura {
    private final int ataque;
    private final Defesa defesa;
    private boolean faseDoisAtivada;

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
        atacarComDanoNarrado(alvo, getNomeAtaqueBasico(), this.ataque);
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

    public abstract String getTitulo();

    public abstract String getPerfilCombate();

    public String getFichaCombate() {
        return getNome()
            + " - "
            + getTitulo()
            + " | Perfil: "
            + getPerfilCombate();
    }

    protected String getNomeAtaqueBasico() {
        return "Ataque Sombrio";
    }

    protected int getAtaqueBase() {
        return this.ataque;
    }

    protected void atacarComDanoNarrado(Criatura alvo, String nomeAtaque, int danoBase) {
        int danoFinal = aplicarPenalidadeDano(danoBase);
        narrar("usou " + nomeAtaque + " e causara " + danoFinal + " de dano.");
        alvo.tomaDano(danoFinal, this);
    }

    protected void curarComNarracao(int cura, String nomeHabilidade) {
        narrar("usou " + nomeHabilidade + " e recuperara " + cura + " de vida.");
        curar(cura);
    }

    protected boolean ativarFaseDoisSeNecessario(int percentualVidaLimite, String mensagem) {
        int vidaPercentual = getVidaAtual() * 100 / getVidaMaxima();
        if (!this.faseDoisAtivada && vidaPercentual <= percentualVidaLimite) {
            this.faseDoisAtivada = true;
            narrar(mensagem);
            return true;
        }
        return false;
    }

    protected boolean estaNaFaseDois() {
        return this.faseDoisAtivada;
    }
}
