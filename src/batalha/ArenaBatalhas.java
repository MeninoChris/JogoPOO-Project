package batalha;

import campanha.Campanha;
import combate.LogCombate;
import controle.ControladorBatalha;
import personagens.Inimigo;
import personagens.Jogador;

public class ArenaBatalhas {
    private final ControladorBatalha controladorBatalha;

    public ArenaBatalhas(ControladorBatalha controladorBatalha) {
        this.controladorBatalha = controladorBatalha;
    }

    public void iniciar() {
        Jogador jogador = this.controladorBatalha.configurarJogador();
        executar(jogador);
    }

    public void executar(Jogador jogador) {
        HistoricoBatalhas historicoBatalhas = new HistoricoBatalhas();
        boolean[] chefesDerrotados = new boolean[Campanha.criarInimigosPadrao().length];

        LogCombate.secao("Arena de Batalhas");
        LogCombate.evento("Neste modo, voce escolhe a ordem dos bosses e decide quando encerrar a run.");
        LogCombate.evento("A cada vitoria, sua progressao permanece e o proximo confronto fica por sua conta.");

        while (jogador.estaVivo()) {
            Inimigo[] inimigosDisponiveis = Campanha.criarInimigosPadrao();
            int indiceInimigo = this.controladorBatalha.escolherDesafioArena(inimigosDisponiveis, chefesDerrotados);
            if (indiceInimigo < 0) {
                break;
            }

            Batalha batalha = new Batalha(jogador, inimigosDisponiveis[indiceInimigo]);
            historicoBatalhas.registrar(batalha);
            batalha.executar(this.controladorBatalha);

            if (!jogador.estaVivo()) {
                break;
            }

            chefesDerrotados[indiceInimigo] = true;
            this.controladorBatalha.exibirPreviewProximoTier(jogador);
            this.controladorBatalha.processarEvolucao(jogador);

            int totalDerrotados = contarChefesDerrotados(chefesDerrotados);
            if (totalDerrotados == chefesDerrotados.length) {
                LogCombate.secao("Arena Concluida");
                LogCombate.evento("Todos os bosses da arena foram derrotados.");
                break;
            }

            this.controladorBatalha.configurarArmaPrincipalEntreBatalhas(jogador);
            if (!this.controladorBatalha.desejaContinuarNaArena(totalDerrotados, chefesDerrotados.length)) {
                break;
            }

            LogCombate.secao("Transicao da Arena");
            LogCombate.evento("O jogador venceu e se prepara para escolher o proximo desafiante.");
            LogCombate.evento("A vida sera restaurada para a proxima luta e a municao restante sera mantida.");
            this.controladorBatalha.aguardarConfirmacao("seguir para o proximo desafio");
            jogador.prepararParaNovaBatalha();
        }

        LogCombate.secao("Encerramento da Arena");
        historicoBatalhas.exibirResumo();
        LogCombate.evento("Estado final do jogador: " + jogador.getResumoProgressao());
        LogCombate.evento("Talentos finais: " + jogador.getResumoTalentos());
    }

    private int contarChefesDerrotados(boolean[] chefesDerrotados) {
        int total = 0;
        for (boolean chefeDerrotado : chefesDerrotados) {
            if (chefeDerrotado) {
                total++;
            }
        }
        return total;
    }
}
