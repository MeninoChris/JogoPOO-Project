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
            narrar("usou Passo Sombrio e desapareceu nas sombras. Nenhum dano sera causado.");
            return;
        }

        int dano = 60 + RD.nextInt(81);
        narrar("usou Rajada Sombria. Dano previsto: " + dano + ".");
        alvo.tomaDano(dano);
    }

    @Override
    public void fraseApresentacao() {
        falar("Eu sou Necrolis, o vulto que precede o fim.");
    }

    @Override
    public void fraseMorte() {
        falar("As sombras... estao me deixando...");
    }
}
