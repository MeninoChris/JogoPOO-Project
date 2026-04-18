package armas.curta;

import armas.base.Arma;
import armas.base.TipoArma;
import personagens.Criatura;

public class LaminasGemeas extends Arma {
    public LaminasGemeas() {
        super("Laminas Gemeas", TipoArma.CURTA_DISTANCIA, 45, 92, 20, 1.7);
    }

    @Override
    public void golpe(Criatura atacante, Criatura alvo) {
        atacante.narrar("usou " + getNome() + ". Efeito: dois golpes rapidos.");
        for (int i = 0; i < 2; i++) {
            if (!alvo.estaVivo()) {
                atacante.narrar("encerrou a sequencia porque o alvo ja foi derrotado.");
                break;
            }
            atacante.narrar("prepara o golpe " + (i + 1) + ".");
            super.golpe(atacante, alvo);
        }
    }

    @Override
    protected String getDescricaoEfeitoEspecial() {
        return "Ataca duas vezes";
    }
}
