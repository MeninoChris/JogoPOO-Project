package personagens;

import defesas.Armadura;
import java.util.Random;

public class Malignus extends Inimigo {
    private static final Random RD = new Random();
    private int contadorTurnos;

    public Malignus() {
        super("Malignus", 1000, 50, new Armadura(20));
    }

    @Override
    public void fazAtaque(Criatura alvo) {
        this.contadorTurnos++;
        ativarFaseDoisSeNecessario(45, "entrou em Fase 2 e foi tomado por uma furia assassina.");

        if (estaNaFaseDois() && alvo.getVidaAtual() <= 220) {
            atacarComDanoNarrado(alvo, "Execucao Carniceira", 130);
            return;
        }

        if (estaNaFaseDois() && this.contadorTurnos % 2 == 0) {
            atacarComDanoNarrado(alvo, "Dilaceracao Frenetica", 95);
            alvo.aplicarDanoContinuo("Hemorragia", 24, 2);
            return;
        }

        if (this.contadorTurnos % 3 == 0) {
            atacarComDanoNarrado(alvo, "Rasgo Sanguinario", 75);
            alvo.aplicarDanoContinuo("Sangramento", 18, 2);
            return;
        }

        if (RD.nextInt(100) < (estaNaFaseDois() ? 55 : 35)) {
            atacarComDanoNarrado(alvo, "Investida Brutal", 90);
            return;
        }

        super.fazAtaque(alvo);
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
    public String getTitulo() {
        return "Governante do Mal";
    }

    @Override
    public String getPerfilCombate() {
        return "Berserker sangrento que pressiona com golpes brutais e efeitos de hemorragia";
    }

    @Override
    public int getExperienciaConcedida() {
        return 180;
    }

    @Override
    protected String getNomeAtaqueBasico() {
        return "Garra Maldita";
    }
}
