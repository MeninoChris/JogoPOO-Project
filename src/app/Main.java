package app;

import batalha.Batalha;
import batalha.HistoricoBatalhas;
import combate.LogCombate;
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

            controladorBatalha.exibirPreviewProximoTier(jogador);
            controladorBatalha.processarEvolucao(jogador);

            if (i < inimigos.length - 1) {
                controladorBatalha.configurarArmaPrincipalEntreBatalhas(jogador);
                LogCombate.secao("Transicao de Batalha");
                LogCombate.evento("O jogador venceu e sera preparado para a proxima batalha.");
                jogador.prepararParaNovaBatalha();
            }
        }

        LogCombate.secao("Encerramento");
        historicoBatalhas.exibirResumo();
        LogCombate.evento("Estado final do jogador: " + jogador.getResumoProgressao());
        LogCombate.evento("Talentos finais: " + jogador.getResumoTalentos());
    }
}
