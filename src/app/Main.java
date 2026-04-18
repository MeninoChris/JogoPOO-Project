package app;

import batalha.Batalha;
import batalha.HistoricoBatalhas;
import controle.ControladorBatalha;
import personagens.Demonium;
import personagens.Inimigo;
import personagens.Jogador;
import personagens.Malignus;
import personagens.Necrolis;

public class Main {
    public static void main(String[] args) {
        ControladorBatalha controladorBatalha = new ControladorBatalha();
        Jogador jogador = controladorBatalha.configurarJogador();
        Inimigo[] inimigos = new Inimigo[] {
            new Malignus(),
            new Demonium(),
            new Necrolis()
        };
        HistoricoBatalhas historicoBatalhas = new HistoricoBatalhas();

        for (int i = 0; i < inimigos.length; i++) {
            Batalha batalha = new Batalha(jogador, inimigos[i]);
            historicoBatalhas.registrar(batalha);
            batalha.executar(controladorBatalha);

            if (!jogador.estaVivo()) {
                break;
            }

            if (i < inimigos.length - 1) {
                System.out.println("O jogador venceu e sera preparado para a proxima batalha.");
                jogador.prepararParaNovaBatalha();
            }
        }

        historicoBatalhas.exibirResumo();
    }
}
