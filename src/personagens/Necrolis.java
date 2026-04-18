package personagens;

import defesas.DefesaHibrida;
import java.util.Random;

public class Necrolis extends Inimigo {
    private static final Random RD = new Random();

    public Necrolis() {
        super("Necrolis", 650, 80, new DefesaHibrida(20, 30));
    }

    @Override
    public void fazAtaque(Criatura alvo) {
        int sorteio = RD.nextInt(100);

        if (sorteio < 25) {
            System.out.println(getNome() + " desapareceu nas sombras e nao atacou.");
            return;
        }

        int dano = 60 + RD.nextInt(81);
        System.out.println(getNome() + " lanca uma rajada sombria de " + dano + " de dano.");
        alvo.tomaDano(dano);
    }

    @Override
    public void fraseApresentacao() {
        System.out.println("Eu sou Necrolis, o vulto que precede o fim.");
    }

    @Override
    public void fraseMorte() {
        System.out.println("As sombras... estao me deixando...");
    }
}
