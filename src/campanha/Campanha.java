package campanha;

import batalha.Batalha;
import batalha.HistoricoBatalhas;
import combate.LogCombate;
import controle.ControladorBatalha;
import itens.Consumivel;
import itens.Fruta;
import itens.PergaminhoCritico;
import itens.PergaminhoForca;
import itens.PocaoCura;
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
        executarCampanhaPadrao(jogador);
    }

    public void executarCampanhaPadrao(Jogador jogador) {
        CapituloCampanha[] capitulos = criarCapitulosPadrao();
        HistoricoBatalhas historicoBatalhas = new HistoricoBatalhas();

        apresentarAbertura(jogador, capitulos.length);

        for (int i = 0; i < capitulos.length; i++) {
            CapituloCampanha capitulo = capitulos[i];
            apresentarCapitulo(i + 1, capitulos.length, capitulo);

            Batalha batalha = new Batalha(jogador, capitulo.inimigo);
            historicoBatalhas.registrar(batalha);
            batalha.executar(this.controladorBatalha);

            if (!jogador.estaVivo()) {
                break;
            }

            exibirDesfechoCapitulo(capitulo);
            concederRecompensasCapitulo(jogador, capitulo);
            this.controladorBatalha.exibirPreviewProximoTier(jogador);
            this.controladorBatalha.processarEvolucao(jogador);

            if (i < capitulos.length - 1) {
                this.controladorBatalha.configurarArmaPrincipalEntreBatalhas(jogador);
                LogCombate.secao("Travessia");
                LogCombate.evento("O jogador consolidou a vitoria e segue para o proximo territorio hostil.");
                LogCombate.evento("A vida sera restaurada para a proxima batalha e a municao restante sera mantida.");
                this.controladorBatalha.aguardarConfirmacao("seguir para o proximo capitulo");
                jogador.prepararParaNovaBatalha();
            }
        }

        exibirEncerramento(historicoBatalhas, jogador);
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

    private void apresentarAbertura(Jogador jogador, int totalCapitulos) {
        LogCombate.secao("Campanha");
        LogCombate.evento("A campanha acompanha a marcha de " + jogador.getNome() + " contra a linhagem dos grandes bosses.");
        LogCombate.evento("Cada capitulo traz um novo territorio, um novo chefe e recompensas para sustentar a jornada.");
        LogCombate.evento("Total de capitulos: " + totalCapitulos + ".");
        LogCombate.espaco();
    }

    private void apresentarCapitulo(int numeroCapitulo, int totalCapitulos, CapituloCampanha capitulo) {
        LogCombate.secao("Capitulo " + numeroCapitulo + " de " + totalCapitulos + " - " + capitulo.titulo);
        LogCombate.evento("Objetivo: " + capitulo.objetivo);
        for (String linha : capitulo.introducao) {
            LogCombate.evento(linha);
        }
        if (capitulo.recompensas.length > 0) {
            LogCombate.evento("Espolio esperado: " + capitulo.getResumoRecompensas());
        }
    }

    private void exibirDesfechoCapitulo(CapituloCampanha capitulo) {
        LogCombate.subtitulo("Desfecho do Capitulo");
        for (String linha : capitulo.desfechoVitoria) {
            LogCombate.evento(linha);
        }
    }

    private void concederRecompensasCapitulo(Jogador jogador, CapituloCampanha capitulo) {
        if (capitulo.recompensas.length == 0) {
            return;
        }

        LogCombate.subtitulo("Recompensas da Campanha");
        for (Consumivel consumivel : capitulo.recompensas) {
            jogador.getInventario().adicionarConsumivel(consumivel);
            LogCombate.evento("Recebeu: " + consumivel.getDescricaoCompleta());
        }
        LogCombate.evento("As armas continuam sendo incorporadas gradualmente conforme o jogador sobe de nivel.");
    }

    private void exibirEncerramento(HistoricoBatalhas historicoBatalhas, Jogador jogador) {
        LogCombate.secao("Encerramento");
        historicoBatalhas.exibirResumo();
        LogCombate.evento("Estado final do jogador: " + jogador.getResumoProgressao());
        LogCombate.evento("Talentos finais: " + jogador.getResumoTalentos());

        if (jogador.estaVivo()) {
            LogCombate.evento("A campanha foi concluida e a linha dos bosses foi quebrada.");
            return;
        }

        LogCombate.evento("A campanha foi interrompida antes do desfecho final.");
    }

    private static CapituloCampanha[] criarCapitulosPadrao() {
        return new CapituloCampanha[] {
            new CapituloCampanha(
                "Sangue no Portao Negro",
                "Derrubar Malignus e recuperar a rota de acesso ao interior das ruinas.",
                new Malignus(),
                new String[] {
                    "Os batedores cairam antes do amanhecer e so sobraram trilhas de sangue no portao.",
                    "Malignus domina a entrada da fortaleza e precisa ser removido para abrir a campanha."
                },
                new String[] {
                    "O portao foi retomado e a marcha do jogador finalmente ganhou terreno.",
                    "Entre os escombros do forte, surgiram suprimentos suficientes para manter a ofensiva."
                },
                new Consumivel[] {
                    new PocaoCura(180, 80),
                    new Fruta("Racao do Forte", 60, 25)
                }
            ),
            new CapituloCampanha(
                "Brasas do Abismo",
                "Atravessar o claustro incendiado e silenciar Demonium antes que o cerco desmorone.",
                new Demonium(),
                new String[] {
                    "Com o portao aberto, o caminho leva ao claustro onde o calor nao para de subir.",
                    "Demonium alimenta as fornalhas do abismo e transforma cada corredor em uma emboscada viva."
                },
                new String[] {
                    "As chamas recuaram e as fornalhas perderam o pulso maligno que movia o setor.",
                    "Runas de poder foram recolhidas das cinzas e convertidas em reforcos para a jornada."
                },
                new Consumivel[] {
                    new PergaminhoForca(18),
                    new PocaoCura(200, 90)
                }
            ),
            new CapituloCampanha(
                "Eclipse das Criptas",
                "Entrar nas catacumbas inferiores e resistir ao dominio sombrio de Necrolis.",
                new Necrolis(),
                new String[] {
                    "Sob o claustro, as criptas se movem como se tivessem vontade propria.",
                    "Necrolis comanda o vazio entre os corredores e enfraquece qualquer invasor antes do golpe final."
                },
                new String[] {
                    "As sombras perderam coesao e as criptas voltaram ao silencio.",
                    "Fragmentos lunares e textos antigos foram resgatados para ampliar o arsenal do jogador."
                },
                new Consumivel[] {
                    new PergaminhoCritico(12, 0.3),
                    new Fruta("Essencia Lunar", 95, 45)
                }
            ),
            new CapituloCampanha(
                "Muralha em Ruina",
                "Romper a defesa tectonica de Vorgrath e abrir passagem ate o santuario celeste.",
                new Vorgrath(),
                new String[] {
                    "Depois das criptas, a trilha sobe por uma muralha rachada por magma e impacto.",
                    "Vorgrath guarda a ultima passagem terrestre e converte a propria carcaca em fortaleza."
                },
                new String[] {
                    "A muralha cedeu e a ultima barreira material entre o jogador e o santuario foi destruida.",
                    "No coracao da ruina, havia reservas de campo suficientes para a ofensiva final."
                },
                new Consumivel[] {
                    new PergaminhoForca(22),
                    new PocaoCura(220, 110)
                }
            ),
            new CapituloCampanha(
                "Firmamento Partido",
                "Enfrentar Aetherion e impedir que o cosmo rachado desabe sobre o mundo.",
                new Aetherion(),
                new String[] {
                    "No alto do santuario, o ceu parece aberto por dentro e estrelas mortas circulam a arena.",
                    "Aetherion governa o ultimo capitulo da campanha e concentra toda a pressao da jornada final."
                },
                new String[] {
                    "Com a queda de Aetherion, o santuario estabilizou e o firmamento deixou de colapsar.",
                    "A campanha chegou ao fim com a trilha completa de bosses derrotados e o heroi de pe."
                },
                new Consumivel[0]
            )
        };
    }

    private static final class CapituloCampanha {
        private final String titulo;
        private final String objetivo;
        private final Inimigo inimigo;
        private final String[] introducao;
        private final String[] desfechoVitoria;
        private final Consumivel[] recompensas;

        private CapituloCampanha(
            String titulo,
            String objetivo,
            Inimigo inimigo,
            String[] introducao,
            String[] desfechoVitoria,
            Consumivel[] recompensas
        ) {
            this.titulo = titulo;
            this.objetivo = objetivo;
            this.inimigo = inimigo;
            this.introducao = introducao;
            this.desfechoVitoria = desfechoVitoria;
            this.recompensas = recompensas;
        }

        private String getResumoRecompensas() {
            String[] descricoes = new String[this.recompensas.length];
            for (int i = 0; i < this.recompensas.length; i++) {
                descricoes[i] = this.recompensas[i].getNomeExibicao();
            }
            return String.join(", ", descricoes);
        }
    }
}
