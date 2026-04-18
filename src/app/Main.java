package app;

import batalha.Batalha;
import controle.ControladorBatalha;
import personagens.Inimigo;
import personagens.Jogador;
import personagens.Malignus;

public class Main {
    public static void main(String[] args) {
        ControladorBatalha controladorBatalha = new ControladorBatalha();
        Jogador jogador = controladorBatalha.configurarJogador();
        Inimigo inimigo = new Malignus();
        Batalha batalha = new Batalha(jogador, inimigo);
        batalha.executar(controladorBatalha);
    }
}
