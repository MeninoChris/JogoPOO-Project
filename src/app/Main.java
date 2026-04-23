package app;

import batalha.ArenaBatalhas;
import campanha.Campanha;
import controle.ControladorBatalha;

public class Main {
    public static void main(String[] args) {
        ControladorBatalha controladorBatalha = new ControladorBatalha();
        int modoJogo = controladorBatalha.escolherModoJogo();

        if (modoJogo == ControladorBatalha.MODO_BATALHAS) {
            new ArenaBatalhas(controladorBatalha).iniciar();
            return;
        }

        new Campanha(controladorBatalha).iniciar();
    }
}
