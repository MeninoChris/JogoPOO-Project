package personagens;

import defesas.Armadura;

public class Malignus extends Inimigo {
    public Malignus() {
        super("Malignus", 1000, 50, new Armadura(20));
    }

    @Override
    public void fraseApresentacao() {
        falar("Eu vou te matar...");
    }

    @Override
    public void fraseMorte() {
        falar("Naoooooooo...");
    }

    @Override
    protected String getNomeAtaqueBasico() {
        return "Garra Maldita";
    }
}
