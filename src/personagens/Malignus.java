package personagens;

import defesas.Armadura;

public class Malignus extends Inimigo {
    public Malignus() {
        super("Malignus", 1000, 50, new Armadura(20));
    }

    @Override
    public void fraseApresentacao() {
        System.out.println("Eu vou te matar...");
    }

    @Override
    public void fraseMorte() {
        System.out.println("Naoooooooo...");
    }
}
