package controle;

import combate.LogCombate;
import armas.AdagaSombria;
import armas.Arco_e_Flecha;
import armas.Arma;
import armas.EscudoGuardiao;
import armas.Espada;
import armas.Faca;
import armas.LaminasGemeas;
import armas.LancaPerfurante;
import armas.MachadoBerserker;
import armas.Pistola;
import armas.TipoArma;
import inventario.Inventario;
import itens.Consumivel;
import java.util.List;
import java.util.Scanner;
import personagens.Inimigo;
import personagens.Jogador;

public class ControladorBatalha {
    private final Scanner scanner;

    public ControladorBatalha() {
        this.scanner = new Scanner(System.in);
    }

    public Jogador configurarJogador() {
        LogCombate.evento("Digite o nome do jogador:");
        String nome = this.scanner.nextLine().trim();
        if (nome.isEmpty()) {
            nome = "MeninoChris";
        }

        Arma armaCurta = escolherArmaInicial(TipoArma.CURTA_DISTANCIA);
        Arma armaLonga = escolherArmaInicial(TipoArma.LONGA_DISTANCIA);

        LogCombate.evento("Jogador " + nome + " preparado para a batalha.");
        Jogador jogador = new Jogador(nome, new Arma[] { armaCurta, armaLonga });
        exibirPreparacaoPreBatalha(jogador);
        return jogador;
    }

    public void executarTurnoJogador(Jogador jogador, Inimigo inimigo) {
        boolean turnoConcluido = false;

        while (!turnoConcluido) {
            LogCombate.evento("Escolha uma acao:");
            LogCombate.evento("1 - Atacar");
            LogCombate.evento("2 - Defender");
            LogCombate.evento("3 - Curar");
            LogCombate.evento("4 - Habilidade especial");

            int escolha = lerEscolhaValida(4);

            switch (escolha) {
                case 1:
                    executarAtaque(jogador, inimigo);
                    turnoConcluido = true;
                    break;
                case 2:
                    jogador.defender();
                    turnoConcluido = true;
                    break;
                case 3:
                    turnoConcluido = executarCura(jogador);
                    break;
                case 4:
                    turnoConcluido = executarEspecial(jogador, inimigo);
                    break;
                default:
                    break;
            }
        }
    }

    private void executarAtaque(Jogador jogador, Inimigo inimigo) {
        Inventario inventario = jogador.getInventario();
        LogCombate.evento("Escolha sua arma:");
        inventario.mostrarArmas();

        int escolha = lerEscolhaValida(inventario.getQuantidadeArmas());
        jogador.atacarComArma(escolha - 1, inimigo);
    }

    private boolean executarCura(Jogador jogador) {
        List<Consumivel> itensDeCura = jogador.getInventario().getConsumiveisDeCura();
        if (itensDeCura.isEmpty()) {
            LogCombate.evento("Voce nao possui itens de cura.");
            return false;
        }

        LogCombate.evento("Escolha um item de cura:");
        mostrarConsumiveis(itensDeCura);

        int escolha = lerEscolhaValida(itensDeCura.size());
        jogador.usarConsumivel(itensDeCura.get(escolha - 1));
        return true;
    }

    private boolean executarEspecial(Jogador jogador, Inimigo inimigo) {
        LogCombate.evento("Escolha a habilidade especial:");
        LogCombate.evento("1 - Golpe Heroico");
        LogCombate.evento("2 - Usar pergaminho");

        int escolha = lerEscolhaValida(2);
        if (escolha == 1) {
            if (!jogador.podeUsarHabilidadeEspecial()) {
                LogCombate.evento("A habilidade especial ainda esta em recarga.");
                return false;
            }

            jogador.usarHabilidadeEspecial(inimigo);
            return true;
        }

        List<Consumivel> itensDeBuff = jogador.getInventario().getConsumiveisDeAprimoramento();
        if (itensDeBuff.isEmpty()) {
            LogCombate.evento("Voce nao possui pergaminhos ou itens de aprimoramento.");
            return false;
        }

        LogCombate.evento("Escolha um item de aprimoramento:");
        mostrarConsumiveis(itensDeBuff);

        int itemEscolhido = lerEscolhaValida(itensDeBuff.size());
        jogador.usarConsumivel(itensDeBuff.get(itemEscolhido - 1));
        return true;
    }

    private void mostrarConsumiveis(List<Consumivel> consumiveis) {
        for (int i = 0; i < consumiveis.size(); i++) {
            LogCombate.evento((i + 1) + " - " + consumiveis.get(i).getDescricaoCompleta());
        }
    }

    private Arma escolherArmaInicial(TipoArma tipoArma) {
        Arma[] opcoes = criarOpcoesDeArma(tipoArma);

        if (tipoArma == TipoArma.CURTA_DISTANCIA) {
            LogCombate.evento("Escolha uma arma de curta distancia:");
        } else {
            LogCombate.evento("Escolha uma arma de longa distancia:");
        }

        for (int i = 0; i < opcoes.length; i++) {
            LogCombate.evento((i + 1) + " - " + opcoes[i].getDescricaoSelecao());
        }

        int escolha = lerEscolhaValida(opcoes.length);
        return opcoes[escolha - 1];
    }

    private void exibirPreparacaoPreBatalha(Jogador jogador) {
        LogCombate.evento("");
        LogCombate.evento("Resumo de preparacao do jogador:");
        LogCombate.evento("Habilidade especial:");
        LogCombate.evento("1 - " + jogador.getDescricaoHabilidadeEspecial());

        LogCombate.evento("Armas escolhidas:");
        Inventario inventario = jogador.getInventario();
        for (int i = 0; i < inventario.getQuantidadeArmas(); i++) {
            Arma arma = inventario.getArma(i);
            LogCombate.evento((i + 1) + " - " + arma.getDescricaoSelecao());
            LogCombate.evento("    Descricao tatica: " + arma.getDescricaoPreBatalha());
        }

        LogCombate.evento("Consumiveis iniciais:");
        List<Consumivel> consumiveis = inventario.getConsumiveis();
        for (int i = 0; i < consumiveis.size(); i++) {
            Consumivel consumivel = consumiveis.get(i);
            LogCombate.evento((i + 1) + " - " + consumivel.getNomeExibicao() + ": " + consumivel.getDescricaoPreBatalha());
        }
        LogCombate.evento("");
    }

    private Arma[] criarOpcoesDeArma(TipoArma tipoArma) {
        if (tipoArma == TipoArma.CURTA_DISTANCIA) {
            return new Arma[] {
                new Faca(),
                new Espada(),
                new EscudoGuardiao(),
                new AdagaSombria(),
                new LaminasGemeas(),
                new MachadoBerserker()
            };
        }

        return new Arma[] {
            new Arco_e_Flecha(),
            new Pistola(),
            new LancaPerfurante()
        };
    }

    private int lerEscolhaValida(int limite) {
        while (true) {
            if (!this.scanner.hasNextInt()) {
                LogCombate.evento("Entrada invalida. Digite um numero.");
                this.scanner.next();
                continue;
            }

            int escolha = this.scanner.nextInt();
            if (escolha >= 1 && escolha <= limite) {
                return escolha;
            }

            LogCombate.evento("Numero invalido, escolha outro:");
        }
    }
}
