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
        LogCombate.secao("Criacao do Jogador");
        LogCombate.evento("Digite o nome do jogador:");
        String nome = lerLinhaOuPadrao("MeninoChris", "Sem entrada para nome. Usando nome padrao.");
        if (nome.isEmpty()) {
            nome = "MeninoChris";
        }

        LogCombate.subtitulo("Escolha das Armas Iniciais");
        Arma armaCurta = escolherArmaInicial(TipoArma.CURTA_DISTANCIA);
        Arma armaLonga = escolherArmaInicial(TipoArma.LONGA_DISTANCIA);

        LogCombate.evento("Jogador " + nome + " preparado para a batalha.");
        Jogador jogador = new Jogador(nome, new Arma[] { armaCurta, armaLonga });
        exibirPreparacaoPreBatalha(jogador);
        return jogador;
    }

    public void executarTurnoJogador(Jogador jogador, Inimigo inimigo) {
        boolean turnoConcluido = false;

        LogCombate.subtitulo("Turno do Jogador");
        while (!turnoConcluido) {
            LogCombate.evento("Escolha uma acao:");
            LogCombate.evento("1 - Atacar");
            LogCombate.evento("2 - Defender");
            LogCombate.evento("3 - Curar");
            LogCombate.evento("4 - Habilidade especial");
            mostrarPromptEscolha(false);

            int escolha = lerEscolhaValida(1, 4);

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

    public void exibirPreviewProximoTier(Jogador jogador) {
        List<Talento> talentosDisponiveis = jogador.getTalentosDisponiveis();
        if (talentosDisponiveis.isEmpty()) {
            return;
        }

        LogCombate.secao("Preview dos Talentos");
        LogCombate.evento("Proximos avancos liberados em cada ramo:");
        LogCombate.evento("Cada ramo sobe no proprio ritmo, sem puxar os outros para o mesmo tier.");
        mostrarTalentosDisponiveis(talentosDisponiveis);
    }

    public void processarEvolucao(Jogador jogador) {
        if (jogador.getPontosAtributoDisponiveis() == 0 && jogador.getPontosTalentoDisponiveis() == 0) {
            return;
        }

        LogCombate.secao("Evolucao do Jogador");
        LogCombate.evento("Estado antes da distribuicao:");
        LogCombate.evento(jogador.getResumoProgressao());
        exibirTalentosAprendidos(jogador);

        while (jogador.getPontosAtributoDisponiveis() > 0) {
            if (!escolherAtributo(jogador)) {
                break;
            }
        }

        while (jogador.getPontosTalentoDisponiveis() > 0) {
            if (!escolherTalento(jogador)) {
                break;
            }
        }

        LogCombate.subtitulo("Resumo Atualizado");
        LogCombate.evento(jogador.getResumoProgressao());
        exibirTalentosAprendidos(jogador);
    }

    public void configurarArmaPrincipalEntreBatalhas(Jogador jogador) {
        Inventario inventario = jogador.getInventario();
        if (inventario.getQuantidadeArmas() <= 1) {
            return;
        }

        boolean concluido = false;
        LogCombate.secao("Preparacao da Proxima Batalha");

        while (!concluido) {
            LogCombate.evento("Arsenal atual:");
            inventario.mostrarArmas();
            LogCombate.evento("Bonus da arma principal: " + jogador.getResumoBonusArmaPrincipal());
            LogCombate.espaco();
            LogCombate.evento("1 - Manter arma principal atual");
            LogCombate.evento("2 - Escolher nova arma principal");
            LogCombate.evento("3 - Ver detalhes dos talentos");
            mostrarPromptEscolha(false);

            int escolha = lerEscolhaValida(1, 3);
            switch (escolha) {
                case 1:
                    concluido = true;
                    break;
                case 2:
                    escolherArmaPrincipal(jogador);
                    concluido = true;
                    break;
                case 3:
                    exibirTalentosAprendidos(jogador);
                    break;
                default:
                    break;
            }
        }
    }

    private void executarAtaque(Jogador jogador, Inimigo inimigo) {
        Inventario inventario = jogador.getInventario();
        LogCombate.evento("Escolha sua arma para atacar:");
        inventario.mostrarArmas();
        LogCombate.evento("A arma principal recebe: " + jogador.getResumoBonusArmaPrincipal());
        mostrarPromptEscolha(false);

        int escolha = lerEscolhaValida(1, inventario.getQuantidadeArmas());
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
        mostrarPromptEscolha(false);

        int escolha = lerEscolhaValida(1, itensDeCura.size());
        jogador.usarConsumivel(itensDeCura.get(escolha - 1));
        return true;
    }

    private void executarDefesa(Jogador jogador) {
        Inventario inventario = jogador.getInventario();
        LogCombate.evento("Escolha a arma de guarda para defender:");
        inventario.mostrarArmas();
        LogCombate.evento("A arma principal recebe: " + jogador.getResumoBonusArmaPrincipal());
        mostrarPromptEscolha(false);

        int escolha = lerEscolhaValida(1, inventario.getQuantidadeArmas());
        jogador.prepararDefesa(escolha - 1);
    }

    private boolean executarEspecial(Jogador jogador, Inimigo inimigo) {
        LogCombate.evento("Escolha a habilidade especial:");
        LogCombate.evento("1 - Golpe Heroico");
        LogCombate.evento("2 - Usar pergaminho");
        mostrarPromptEscolha(false);

        int escolha = lerEscolhaValida(1, 2);
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
        mostrarPromptEscolha(false);

        int itemEscolhido = lerEscolhaValida(1, itensDeBuff.size());
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
        String descricaoSlot;

        if (tipoArma == TipoArma.CURTA_DISTANCIA) {
            descricaoSlot = "principal";
            LogCombate.evento("Escolha uma arma de curta distancia para o slot principal:");
        } else {
            descricaoSlot = "secundario";
            LogCombate.evento("Escolha uma arma de longa distancia para o slot secundario:");
        }

        for (int i = 0; i < opcoes.length; i++) {
            LogCombate.evento((i + 1) + " - " + opcoes[i].getDescricaoSelecao());
        }
        mostrarPromptEscolha(false);

        int escolha = lerEscolhaValida(1, opcoes.length);
        LogCombate.evento("Arma do slot " + descricaoSlot + " definida: " + opcoes[escolha - 1].getNomeExibicao() + ".");
        return opcoes[escolha - 1];
    }

    private void exibirPreparacaoPreBatalha(Jogador jogador) {
        LogCombate.secao("Preparacao Inicial");
        LogCombate.evento("Progressao:");
        LogCombate.evento(jogador.getResumoProgressao());
        exibirTalentosAprendidos(jogador);
        LogCombate.evento("Habilidade especial:");
        LogCombate.evento("1 - " + jogador.getDescricaoHabilidadeEspecial());
        LogCombate.evento("Bonus da arma principal:");
        LogCombate.evento(jogador.getResumoBonusArmaPrincipal());

        LogCombate.subtitulo("Armas Escolhidas");
        Inventario inventario = jogador.getInventario();
        for (int i = 0; i < inventario.getQuantidadeArmas(); i++) {
            Arma arma = inventario.getArma(i);
            LogCombate.evento(
                inventario.getDescricaoArmaOrdenada(i).replace(arma.getDescricaoCombate(), arma.getDescricaoSelecao())
            );
            LogCombate.evento("    Descricao tatica: " + arma.getDescricaoPreBatalha());
        }

        LogCombate.subtitulo("Consumiveis Iniciais");
        List<Consumivel> consumiveis = inventario.getConsumiveis();
        for (int i = 0; i < consumiveis.size(); i++) {
            Consumivel consumivel = consumiveis.get(i);
            LogCombate.evento((i + 1) + " - " + consumivel.getNomeExibicao() + ": " + consumivel.getDescricaoPreBatalha());
        }
    }

    private boolean escolherAtributo(Jogador jogador) {
        LogCombate.subtitulo("Distribuicao de Atributos");
        LogCombate.evento("Pontos de atributo disponiveis: " + jogador.getPontosAtributoDisponiveis());
        LogCombate.evento("0 - Manter pontos para depois");

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
        mostrarPromptEscolha(true);

        int escolha = lerEscolhaValida(0, atributos.length);
        if (escolha == 0) {
            LogCombate.evento("Pontos de atributo mantidos para depois.");
            return false;
        }

        jogador.investirAtributo(atributos[escolha - 1]);
        return true;
    }

    private boolean escolherTalento(Jogador jogador) {
        List<Talento> talentosDisponiveis = jogador.getTalentosDisponiveis();
        if (talentosDisponiveis.isEmpty()) {
            LogCombate.evento("Nenhum talento disponivel no momento, ponto de talento sera mantido.");
            return false;
        }

        LogCombate.subtitulo("Escolha de Talento");
        LogCombate.evento("Pontos de talento disponiveis: " + jogador.getPontosTalentoDisponiveis());
        LogCombate.evento("Cada ramo sobe de forma independente.");
        LogCombate.evento("0 - Manter pontos para depois");
        mostrarTalentosDisponiveis(talentosDisponiveis);
        mostrarPromptEscolha(true);

        int escolha = lerEscolhaValida(0, talentosDisponiveis.size());
        if (escolha == 0) {
            LogCombate.evento("Pontos de talento mantidos para depois.");
            return false;
        }

        jogador.aprenderTalento(talentosDisponiveis.get(escolha - 1));
        exibirTalentosAprendidos(jogador);
        return true;
    }

    private void mostrarTalentosDisponiveis(List<Talento> talentosDisponiveis) {
        for (int i = 0; i < talentosDisponiveis.size(); i++) {
            Talento talento = talentosDisponiveis.get(i);
            LogCombate.evento(
                (i + 1)
                    + " - "
                    + talento.getNomeExibicao()
                    + " ["
                    + talento.getNomeRamo()
                    + "] (tier "
                    + talento.getCamada()
                    + ", nivel minimo "
                    + talento.getNivelMinimo()
                    + ")"
            );
            LogCombate.evento("    Tema: " + talento.getDescricao());
            LogCombate.evento("    Efeito: " + talento.getResumoEfeito());
        }
    }

    private void exibirTalentosAprendidos(Jogador jogador) {
        LogCombate.subtitulo("Talentos Ativos");
        List<Talento> talentosAprendidos = jogador.getTalentosAprendidos();
        if (talentosAprendidos.isEmpty()) {
            LogCombate.evento("Nenhum talento aprendido.");
            return;
        }

        for (Talento talento : talentosAprendidos) {
            LogCombate.evento(
                "- "
                    + talento.getNomeExibicao()
                    + " ["
                    + talento.getNomeRamo()
                    + " tier "
                    + talento.getCamada()
                    + "]: "
                    + talento.getResumoEfeito()
            );
        }
    }

    private void escolherArmaPrincipal(Jogador jogador) {
        Inventario inventario = jogador.getInventario();

        LogCombate.subtitulo("Escolher Nova Arma Principal");
        LogCombate.evento("0 - Voltar sem alterar");
        inventario.mostrarArmas();
        LogCombate.evento("Bonus da arma principal: " + jogador.getResumoBonusArmaPrincipal());
        mostrarPromptEscolha(true);

        int escolha = lerEscolhaValida(0, inventario.getQuantidadeArmas());
        if (escolha == 0) {
            LogCombate.evento("Arma principal mantida.");
            return;
        }

        inventario.definirArmaPrincipal(escolha - 1);
        LogCombate.evento("Nova arma principal definida com sucesso.");
        LogCombate.evento("Bonus da arma principal: " + jogador.getResumoBonusArmaPrincipal());
        inventario.mostrarArmas();
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

    private void mostrarPromptEscolha(boolean permitirVoltar) {
        if (permitirVoltar) {
            LogCombate.evento("Digite o numero desejado ou 0 para voltar:");
            return;
        }

        LogCombate.evento("Digite o numero desejado:");
    }

    private int lerEscolhaValida(int minimo, int maximo) {
        while (true) {
            if (!this.scanner.hasNextLine()) {
                int escolhaPadrao = minimo == 0 ? 0 : 1;
                LogCombate.evento("Entrada encerrada. Selecionando a opcao padrao automaticamente.");
                LogCombate.espaco();
                return escolhaPadrao;
            }

            String entrada = normalizarTexto(this.scanner.nextLine());
            if (entrada.isEmpty()) {
                LogCombate.evento("Entrada invalida. Digite um numero.");
                continue;
            }

            try {
                int escolha = Integer.parseInt(entrada);
                if (escolha >= minimo && escolha <= maximo) {
                    LogCombate.espaco();
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
