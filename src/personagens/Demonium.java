package personagens;

import defesas.Escudo;

public class Demonium extends Inimigo {
    public Demonium() {
        super("Demonium", 500, 120, new Escudo(15));
    }

    @Override
    public void fraseApresentacao() {
        System.out.println("Eu sou a morte, doce, nua e crua");
    }

    @Override
    public void fraseMorte() {
        System.out.println("Matar a morte e bem ironico, nao?");
    }
}
