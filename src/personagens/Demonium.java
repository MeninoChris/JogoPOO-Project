package personagens;

import defesas.BarreiraMagica;

public class Demonium extends Inimigo {
    public Demonium() {
        super("Demonium", 500, 120, new BarreiraMagica(90));
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
