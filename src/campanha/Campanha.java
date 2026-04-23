package campanha;

import batalha.Batalha;
import batalha.HistoricoBatalhas;
import combate.LogCombate;
import controle.ControladorBatalha;
import personagens.Aetherion;
import personagens.Demonium;
import personagens.Inimigo;
import personagens.Jogador;
import personagens.Malignus;
import personagens.Necrolis;
import personagens.Vorgrath;

public class Campanha {
    private final ControladorBatalha controladorBatalha;

    public Campanha(ControladorBatalha controladorBatalha) {
        this.controladorBatalha = controladorBatalha;
    }

    public void iniciar() {
        Jogador jogador = this.controladorBatalha.configurarJogador();
        executar(jogador, criarInimigosPadrao());
    }

    public void executar(Jogador jogador, Inimigo[] inimigos) {
        HistoricoBatalhas historicoBatalhas = new HistoricoBatalhas();

        for (int i = 0; i < inimigos.length; i++) {
            Batalha batalha = new Batalha(jogador, inimigos[i]);
            historicoBatalhas.registrar(batalha);
            batalha.executar(this.controladorBatalha);

            if (!jogador.estaVivo()) {
                break;
            }

            this.controladorBatalha.exibirPreviewProximoTier(jogador);
            this.controladorBatalha.processarEvolucao(jogador);

            if (i < inimigos.length - 1) {
                this.controladorBatalha.configurarArmaPrincipalEntreBatalhas(jogador);
                LogCombate.secao("Transicao de Batalha");
                LogCombate.evento("O jogador venceu.");
                LogCombate.evento("A vida sera restaurada para a proxima batalha e a municao restante sera mantida.");
                this.controladorBatalha.aguardarConfirmacao("seguir para a proxima batalha");
                jogador.prepararParaNovaBatalha();
            }
        }

        LogCombate.secao("Encerramento");
        historicoBatalhas.exibirResumo();
        LogCombate.evento("Estado final do jogador: " + jogador.getResumoProgressao());
        LogCombate.evento("Talentos finais: " + jogador.getResumoTalentos());
    }

    public static Inimigo[] criarInimigosPadrao() {
        return new Inimigo[] {
            new Malignus(),
            new Demonium(),
            new Necrolis(),
            new Vorgrath(),
            new Aetherion()
        };
    }
}
