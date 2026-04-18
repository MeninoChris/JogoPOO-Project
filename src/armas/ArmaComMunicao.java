package armas;

import personagens.Criatura;

public abstract class ArmaComMunicao extends Arma {
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
        this.municao = municao;
    }

    @Override
    public void golpe(Criatura atacante, Criatura alvo) {
        if (this.municao <= 0) {
            System.out.println(getNome() + " esta sem municao. O ataque causou 0 de dano.");
            alvo.tomaDano(0);
            return;
        }

        this.municao--;
        super.golpe(atacante, alvo);
    }

    @Override
    protected String getDescricaoEfeitoEspecial() {
        return super.getDescricaoEfeitoEspecial();
    }

    @Override
    protected String getDescricaoMunicao() {
        return "Municao = " + this.municao;
    }
}
