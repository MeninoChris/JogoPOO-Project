package armas;

import personagens.Criatura;

public class LaminasGemeas extends Arma {
    public LaminasGemeas() {
        super("Laminas Gemeas", 45, 92, 20, 1.7);
    }

    @Override
    public void golpe(Criatura atacante, Criatura alvo) {
        System.out.println(getNome() + " executa dois golpes rapidos.");
        for (int i = 0; i < 2; i++) {
            System.out.println("Golpe " + (i + 1) + ":");
            super.golpe(atacante, alvo);
        }
    }

    @Override
    protected String getDescricaoEfeitoEspecial() {
        return "Ataca duas vezes";
    }
}
