package personagens;

import defesas.BarreiraMagica;
import java.util.Random;

public class Demonium extends Inimigo {
    private static final Random RD = new Random();
    private boolean absorveuVida;
    private int recargaCataclismo;

    public Demonium() {
        super("Demonium", 500, 120, new BarreiraMagica(90));
    }

    @Override
    public void fazAtaque(Criatura alvo) {
        ativarFaseDoisSeNecessario(40, "entrou em Fase 2. As chamas do abismo cresceram ao seu redor.");

        if (this.recargaCataclismo > 0) {
            this.recargaCataclismo--;
        }

        if (!this.absorveuVida && getVidaAtual() <= getVidaMaxima() / 2) {
            atacarComDanoNarrado(alvo, "Absorcao Infernal", 95);
            curarComNarracao(70, "Absorcao Infernal");
            this.absorveuVida = true;
            return;
        }

        if (estaNaFaseDois() && this.recargaCataclismo == 0 && alvo.getVidaAtual() > 200) {
            atacarComDanoNarrado(alvo, "Cataclismo Rubro", 140);
            alvo.aplicarDanoContinuo("Brasa Abissal", 30, 2);
            this.recargaCataclismo = 2;
            return;
        }

        if (getVidaAtual() <= 150 && RD.nextInt(100) < 45) {
            curarComNarracao(55, "Nucleo Profano");
            return;
        }

        if (RD.nextInt(100) < (estaNaFaseDois() ? 60 : 40)) {
            atacarComDanoNarrado(alvo, "Chama Devoradora", 80);
            alvo.aplicarDanoContinuo("Chamas Infernais", estaNaFaseDois() ? 28 : 22, 2);
            return;
        }

        super.fazAtaque(alvo);
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
    public String getTitulo() {
        return "Arauto do Abismo";
    }

    @Override
    public String getPerfilCombate() {
        return "Mago infernal que aplica queimaduras, entra em fase 2 e consegue se sustentar";
    }

    @Override
    protected String getNomeAtaqueBasico() {
        return "Explosao Infernal";
    }
}
