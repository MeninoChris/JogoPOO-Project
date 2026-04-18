package personagens;

import defesas.BarreiraMagica;

public class Demonium extends Inimigo {
    public Demonium() {
        super("Demonium", 500, 120, new BarreiraMagica(90));
    }

    @Override
    public void fraseApresentacao() {
        falar("Eu sou a morte, doce, nua e crua");
    }

    @Override
    public void fraseMorte() {
        falar("Matar a morte e bem ironico, nao?");
    }

    @Override
    protected String getNomeAtaqueBasico() {
        return "Explosao Infernal";
    }
}
