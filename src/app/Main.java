package app;

import controle.ControladorBatalha;
import personagens.Inimigo;
import personagens.Jogador;
import personagens.Malignus;

public class Main {
    public static void main(String[] args) {
        ControladorBatalha controladorBatalha = new ControladorBatalha();
        Jogador jogador = controladorBatalha.configurarJogador();
        Inimigo inimigo = new Malignus();
        controladorBatalha.executar(jogador, inimigo);
    }
}
