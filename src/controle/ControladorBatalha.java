package controle;

import combate.LogCombate;
import armas.base.Arma;
import armas.base.TipoArma;
import armas.curta.AdagaSombria;
import armas.curta.EscudoGuardiao;
import armas.curta.Espada;
import armas.curta.Faca;
import armas.curta.LaminasGemeas;
import armas.curta.MachadoBerserker;
import armas.longa.Arco_e_Flecha;
import armas.longa.LancaPerfurante;
import armas.longa.Pistola;
import inventario.Inventario;
import itens.Consumivel;
import java.util.List;
import java.util.Scanner;
import personagens.Inimigo;
import personagens.Jogador;
import progressao.AtributoEvolutivo;
import progressao.Talento;

public class ControladorBatalha {
    private final Scanner scanner;

    public ControladorBatalha() {
        this.scanner = new Scanner(System.in);
    }

    public Jogador configurarJogador() {
        LogCombate.evento("Digite o nome do jogador:");
        String nome = lerLinhaOuPadrao("MeninoChris", "Sem entrada para nome. Usando nome padrao.");
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
                    executarDefesa(jogador);
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

    public void processarEvolucao(Jogador jogador) {
        if (jogador.getPontosAtributoDisponiveis() == 0 && jogador.getPontosTalentoDisponiveis() == 0) {
            return;
        }

        LogCombate.evento("");
        LogCombate.evento("Evolucao do jogador:");
        LogCombate.evento(jogador.getResumoProgressao());

        while (jogador.getPontosAtributoDisponiveis() > 0) {
            escolherAtributo(jogador);
        }

        while (jogador.getPontosTalentoDisponiveis() > 0) {
            if (!escolherTalento(jogador)) {
                break;
            }
        }

        LogCombate.evento("Progressao atualizada:");
        LogCombate.evento(jogador.getResumoProgressao());
        LogCombate.evento("Talentos: " + jogador.getResumoTalentos());
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

    private void executarDefesa(Jogador jogador) {
        Inventario inventario = jogador.getInventario();
        LogCombate.evento("Escolha a arma de guarda para defender:");
        inventario.mostrarArmas();
        int escolha = lerEscolhaValida(inventario.getQuantidadeArmas());
        jogador.prepararDefesa(escolha - 1);
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
        LogCombate.evento("Progressao:");
        LogCombate.evento(jogador.getResumoProgressao());
        LogCombate.evento("Talentos:");
        LogCombate.evento(jogador.getResumoTalentos());
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

    private void escolherAtributo(Jogador jogador) {
        LogCombate.evento("Escolha um atributo para evoluir:");
        AtributoEvolutivo[] atributos = AtributoEvolutivo.values();
        for (int i = 0; i < atributos.length; i++) {
            LogCombate.evento(
                (i + 1)
                    + " - "
                    + atributos[i].getNomeExibicao()
                    + ": "
                    + atributos[i].getDescricao()
            );
        }

        int escolha = lerEscolhaValida(atributos.length);
        jogador.investirAtributo(atributos[escolha - 1]);
    }

    private boolean escolherTalento(Jogador jogador) {
        List<Talento> talentosDisponiveis = jogador.getTalentosDisponiveis();
        if (talentosDisponiveis.isEmpty()) {
            LogCombate.evento("Nenhum talento disponivel no momento, ponto de talento sera mantido.");
            return false;
        }

        LogCombate.evento("Escolha um talento:");
        for (int i = 0; i < talentosDisponiveis.size(); i++) {
            Talento talento = talentosDisponiveis.get(i);
            LogCombate.evento(
                (i + 1)
                    + " - "
                    + talento.getNomeExibicao()
                    + " (nivel minimo "
                    + talento.getNivelMinimo()
                    + "): "
                    + talento.getDescricao()
            );
        }

        int escolha = lerEscolhaValida(talentosDisponiveis.size());
        jogador.aprenderTalento(talentosDisponiveis.get(escolha - 1));
        return true;
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
            if (!this.scanner.hasNextLine()) {
                LogCombate.evento("Entrada encerrada. Selecionando a primeira opcao automaticamente.");
                return 1;
            }

            String entrada = normalizarTexto(this.scanner.nextLine());
            if (entrada.isEmpty()) {
                LogCombate.evento("Entrada invalida. Digite um numero.");
                continue;
            }

            try {
                int escolha = Integer.parseInt(entrada);
                if (escolha >= 1 && escolha <= limite) {
                    return escolha;
                }
            } catch (NumberFormatException ex) {
                LogCombate.evento("Entrada invalida. Digite um numero.");
                continue;
            }

            LogCombate.evento("Numero invalido, escolha outro:");
        }
    }

    private String lerLinhaOuPadrao(String valorPadrao, String mensagemPadrao) {
        if (!this.scanner.hasNextLine()) {
            LogCombate.evento(mensagemPadrao);
            return valorPadrao;
        }

        return normalizarTexto(this.scanner.nextLine());
    }

    private String normalizarTexto(String texto) {
        String textoNormalizado = texto.replace("\uFEFF", "").trim();
        return textoNormalizado.replaceAll("\\p{Cntrl}", "");
    }
}
