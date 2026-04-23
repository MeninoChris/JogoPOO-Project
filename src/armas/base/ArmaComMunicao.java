package armas.base;

import personagens.Criatura;

public abstract class ArmaComMunicao extends Arma {
    private final int municaoMaxima;
    private int municao;

    public ArmaComMunicao(
        String nome,
        TipoArma tipoArma,
        int ataque,
        int chance,
        int chanceCritico,
        double multiplicadorCritico,
        int municao
    ) {
        super(nome, tipoArma, ataque, chance, chanceCritico, multiplicadorCritico);
        this.municaoMaxima = municao;
        this.municao = municao;
    }

    @Override
    public void golpe(Criatura atacante, Criatura alvo) {
        if (this.municao <= 0) {
            atacante.narrar("tentou usar " + getNome() + ", mas a municao acabou. Dano previsto: 0.");
            alvo.tomaDano(0, atacante);
            return;
        }

        this.municao--;
        super.golpe(atacante, alvo);
    }

    @Override
    protected String getDescricaoMunicao() {
        return "Municao = " + this.municao + "/" + this.municaoMaxima;
    }

    @Override
    public boolean usaMunicaoLimitada() {
        return true;
    }

    @Override
    public String getResumoMunicaoAtual() {
        return this.municao + "/" + this.municaoMaxima;
    }

}
