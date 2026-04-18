package app;

import controle.ControladorBatalha;
import personagens.Inimigo;
import personagens.Jogador;
import personagens.Malignus;

public class Main {
    public static void main(String[] args) {
        Jogador jogador = new Jogador("MeninoChris");
        Inimigo inimigo = new Malignus();
        ControladorBatalha controladorBatalha = new ControladorBatalha();
        controladorBatalha.executar(jogador, inimigo);
    }
}
