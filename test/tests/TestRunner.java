package tests;

import armas.base.Arma;
import armas.base.TipoArma;
import armas.curta.Faca;
import armas.curta.LaminasGemeas;
import armas.longa.LancaPerfurante;
import armas.longa.Pistola;
import campanha.Campanha;
import controle.ControladorBatalha;
import defesas.Armadura;
import defesas.BarreiraMagica;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import personagens.Aetherion;
import personagens.Criatura;
import personagens.Demonium;
import personagens.Inimigo;
import personagens.Jogador;
import personagens.Malignus;
import personagens.Necrolis;
import personagens.Vorgrath;
import progressao.Talento;

public final class TestRunner {
    private TestRunner() {}

    public static void main(String[] args) {
        TestCase[] tests = new TestCase[] {
            new TestCase("progressao_e_talentos_por_ramo", TestRunner::testarProgressaoETalentosPorRamo),
            new TestCase("reset_entre_batalhas", TestRunner::testarResetEntreBatalhas),
            new TestCase("campanha_completa_e_historico", TestRunner::testarCampanhaCompletaEHistorico),
            new TestCase("novos_inimigos", TestRunner::testarNovosInimigos),
            new TestCase("efeitos_de_status", TestRunner::testarEfeitosDeStatus),
            new TestCase("defesas_basicas", TestRunner::testarDefesasBasicas),
            new TestCase("armas_especiais", TestRunner::testarArmasEspeciais),
            new TestCase("arma_principal_e_municao_visivel", TestRunner::testarArmaPrincipalEMunicaoVisivel),
            new TestCase("critico_garantido", TestRunner::testarCriticoGarantido)
        };

        int aprovados = 0;

        for (TestCase test : tests) {
            try {
                test.run();
                System.out.println("[PASS] " + test.name);
                aprovados++;
            } catch (Throwable ex) {
                System.err.println("[FAIL] " + test.name);
                ex.printStackTrace(System.err);
            }
        }

        if (aprovados != tests.length) {
            System.err.println("Falhas: " + (tests.length - aprovados) + " de " + tests.length + " testes.");
            System.exit(1);
        }

        System.out.println("Todos os testes passaram: " + aprovados);
    }

    private static void testarProgressaoETalentosPorRamo() {
        Jogador jogador = novoJogadorPadrao();

        Assertions.assertEquals(1, jogador.getNivel(), "O jogador deve iniciar no nivel 1.");
        Assertions.assertEquals(0, jogador.getTalentosDisponiveis().size(), "Nao deve haver talentos no nivel 1.");

        jogador.ganharExperiencia(180);

        Assertions.assertEquals(2, jogador.getNivel(), "Apos o primeiro boss o jogador deve chegar ao nivel 2.");
        Assertions.assertEquals(1, jogador.getPontosTalentoDisponiveis(), "Deve haver 1 ponto de talento no nivel 2.");
        Assertions.assertTalentosDisponiveis(
            jogador,
            Talento.FEROCIDADE,
            Talento.BASTIAO,
            Talento.ALQUIMISTA
        );

        jogador.aprenderTalento(Talento.FEROCIDADE);

        Assertions.assertEquals(
            "Ferocidade",
            jogador.getResumoTalentos(),
            "A primeira escolha de talento deve ser registrada."
        );
        Assertions.assertEquals(0, jogador.getPontosTalentoDisponiveis(), "O ponto de talento deve ser consumido.");
        Assertions.assertTalentosDisponiveis(
            jogador,
            Talento.BASTIAO,
            Talento.ALQUIMISTA
        );

        jogador.ganharExperiencia(220);

        Assertions.assertEquals(3, jogador.getNivel(), "Apos o segundo boss o jogador deve chegar ao nivel 3.");
        Assertions.assertEquals(1, jogador.getPontosTalentoDisponiveis(), "Deve haver nova escolha de talento.");
        Assertions.assertTalentosDisponiveis(
            jogador,
            Talento.EXECUTOR,
            Talento.BASTIAO,
            Talento.ALQUIMISTA
        );

        jogador.aprenderTalento(Talento.EXECUTOR);
        Assertions.assertTrue(
            jogador.getResumoTalentos().contains("Ferocidade") && jogador.getResumoTalentos().contains("Executor"),
            "O segundo talento deve continuar o mesmo ramo quando o jogador quiser."
        );

        jogador.ganharExperiencia(280);
        Assertions.assertEquals(4, jogador.getNivel(), "Apos a progressao seguinte o jogador deve chegar ao nivel 4.");
        Assertions.assertTalentosDisponiveis(
            jogador,
            Talento.LAMINA_ASCENDENTE,
            Talento.BASTIAO,
            Talento.ALQUIMISTA
        );
    }

    private static void testarResetEntreBatalhas() {
        Jogador jogador = new Jogador("Reset", new Arma[] { new Faca(), new Pistola() });
        TestCreature alvo = new TestCreature("Alvo", 2000);

        for (int i = 0; i < 6; i++) {
            jogador.atacarComArma(1, alvo);
        }

        Assertions.assertContains(
            jogador.getInventario().getArma(1).getDescricaoCombate(),
            "Municao = 0",
            "A pistola deve ficar sem municao apos 6 usos."
        );

        jogador.usarHabilidadeEspecial(alvo);
        Assertions.assertFalse(
            jogador.podeUsarHabilidadeEspecial(),
            "A habilidade especial deve entrar em recarga apos o uso."
        );

        jogador.aplicarDanoContinuo("Sangramento", 12, 2);
        jogador.aplicarReducaoDano("Maldicao", 7, 2);
        jogador.tomaDano(120);

        Assertions.assertTrue(
            jogador.getVidaAtual() < jogador.getVidaMaxima(),
            "O jogador precisa estar ferido antes do reset de batalha."
        );
        Assertions.assertEquals(
            23,
            jogador.aplicarPenalidadeDano(30),
            "A reducao de dano temporaria deve estar ativa antes do reset."
        );

        jogador.prepararParaNovaBatalha();

        Assertions.assertEquals(
            jogador.getVidaMaxima(),
            jogador.getVidaAtual(),
            "A vida deve ser restaurada totalmente entre batalhas."
        );
        Assertions.assertTrue(
            jogador.podeUsarHabilidadeEspecial(),
            "A recarga da habilidade especial deve ser limpa entre batalhas."
        );
        Assertions.assertContains(
            jogador.getInventario().getArma(1).getDescricaoCombate(),
            "Municao = 0/6",
            "A municao nao deve ser restaurada automaticamente entre batalhas."
        );
        Assertions.assertEquals(
            30,
            jogador.aplicarPenalidadeDano(30),
            "A reducao de dano deve ser removida entre batalhas."
        );

        int vidaAposReset = jogador.getVidaAtual();
        jogador.processarInicioTurno();
        Assertions.assertEquals(
            vidaAposReset,
            jogador.getVidaAtual(),
            "Efeitos de dano continuo devem ser removidos entre batalhas."
        );
    }

    private static void testarCampanhaCompletaEHistorico() throws Exception {
        Jogador jogador = new Jogador("Campanha", new Arma[] { new ArmaFatal("Espada de Teste", 2000), new Faca() });
        Inimigo[] inimigos = Campanha.criarInimigosPadrao();
        ControladorBatalha controlador = new ControladorBatalhaProgramado();
        Campanha campanha = new Campanha(controlador);

        String saida = capturarSaida(() -> campanha.executar(jogador, inimigos));

        Assertions.assertTrue(jogador.estaVivo(), "O jogador deve sobreviver ate o fim da campanha de teste.");
        Assertions.assertContains(saida, "Historico de batalhas:", "O historico final deve ser impresso.");
        Assertions.assertContains(saida, "1 - Batalha contra Malignus", "A primeira batalha deve aparecer no resumo.");
        Assertions.assertContains(saida, "2 - Batalha contra Demonium", "A segunda batalha deve aparecer no resumo.");
        Assertions.assertContains(saida, "3 - Batalha contra Necrolis", "A terceira batalha deve aparecer no resumo.");
        Assertions.assertContains(saida, "4 - Batalha contra Vorgrath", "A quarta batalha deve aparecer no resumo.");
        Assertions.assertContains(saida, "5 - Batalha contra Aetherion", "A quinta batalha deve aparecer no resumo.");
        Assertions.assertContains(saida, "vencedor: Campanha", "O vencedor de cada batalha deve ser registrado.");
        Assertions.assertContains(saida, "Vida inicial: 1000", "A ficha dos inimigos deve expor a vida inicial.");
        Assertions.assertContains(saida, "Ataque base: 50", "A ficha dos inimigos deve expor o ataque base.");
        Assertions.assertContains(saida, "Defesa: Armadura", "A ficha dos inimigos deve expor o tipo de defesa.");
        Assertions.assertContains(saida, "Pressione Enter para iniciar a batalha contra Malignus.", "A batalha deve pedir confirmacao antes de comecar.");
        Assertions.assertContains(saida, "Pressione Enter para iniciar a rodada 1.", "Cada rodada deve pedir confirmacao.");
        Assertions.assertContains(saida, "Pressione Enter para seguir para a proxima batalha.", "A campanha deve pedir confirmacao nas transicoes.");
    }

    private static void testarNovosInimigos() {
        Inimigo vorgrath = new Vorgrath();
        Inimigo aetherion = new Aetherion();

        Assertions.assertContains(vorgrath.getFichaCombate(), "Colosso da Ruina", "Vorgrath deve expor seu titulo.");
        Assertions.assertContains(vorgrath.getFichaCombate(), "Defesa: Armadura", "Vorgrath deve expor sua defesa.");
        Assertions.assertContains(aetherion.getFichaCombate(), "Arconte do Cosmo Partido", "Aetherion deve expor seu titulo.");
        Assertions.assertContains(aetherion.getFichaCombate(), "Defesa: Barreira Magica", "Aetherion deve expor sua defesa.");
    }

    private static void testarEfeitosDeStatus() {
        TestCreature criatura = new TestCreature("Status", 100);

        criatura.aplicarDanoContinuo("Sangramento", 10, 2);
        criatura.processarInicioTurno();
        Assertions.assertEquals(90, criatura.getVidaAtual(), "O primeiro tick de dano continuo deve ser aplicado.");

        criatura.processarInicioTurno();
        Assertions.assertEquals(80, criatura.getVidaAtual(), "O segundo tick de dano continuo deve ser aplicado.");

        criatura.processarInicioTurno();
        Assertions.assertEquals(80, criatura.getVidaAtual(), "O dano continuo deve expirar apos a duracao.");

        criatura.aplicarReducaoDano("Maldicao", 7, 2);
        Assertions.assertEquals(13, criatura.aplicarPenalidadeDano(20), "A reducao de dano deve ser aplicada.");

        criatura.processarFimTurno();
        Assertions.assertEquals(13, criatura.aplicarPenalidadeDano(20), "A reducao deve durar pelo segundo turno.");

        criatura.processarFimTurno();
        Assertions.assertEquals(20, criatura.aplicarPenalidadeDano(20), "A reducao deve expirar apos a duracao.");
    }

    private static void testarDefesasBasicas() {
        DefensiveEnemy inimigoComArmadura = new DefensiveEnemy("Armadura", 200, new Armadura(20));
        inimigoComArmadura.tomaDano(100);
        Assertions.assertEquals(120, inimigoComArmadura.getVidaAtual(), "A armadura deve reduzir 20% do dano.");

        DefensiveEnemy inimigoComBarreira = new DefensiveEnemy("Barreira", 200, new BarreiraMagica(90));
        inimigoComBarreira.tomaDano(150);
        Assertions.assertEquals(110, inimigoComBarreira.getVidaAtual(), "A barreira deve limitar o dano ao teto.");
    }

    private static void testarArmasEspeciais() {
        TestCreature atacante = new TestCreature("Atacante", 100);
        TestCreature alvoDuplo = new TestCreature("Alvo Duplo", 100);
        LaminasGemeasDeterministicas laminasGemeas = new LaminasGemeasDeterministicas(10);

        laminasGemeas.golpe(atacante, alvoDuplo);

        Assertions.assertEquals(80, alvoDuplo.getVidaAtual(), "As laminas gemeas devem acertar duas vezes.");
        Assertions.assertEquals(2, alvoDuplo.getEventosDeDano(), "As laminas gemeas devem gerar dois eventos de dano.");

        DefensiveEnemy alvoComDefesa = new DefensiveEnemy("Alvo Blindado", 100, new Armadura(50));
        LancaPerfuranteDeterministica lanca = new LancaPerfuranteDeterministica(40);

        lanca.golpe(atacante, alvoComDefesa);

        Assertions.assertEquals(60, alvoComDefesa.getVidaAtual(), "A lanca perfurante deve ignorar a defesa quando ativada.");
    }

    private static void testarArmaPrincipalEMunicaoVisivel() {
        Jogador jogador = novoJogadorPadrao();
        Arma faca = jogador.getInventario().getArma(0);

        Assertions.assertContains(
            jogador.getInventario().getDescricaoArmaOrdenada(1),
            "Municao = 6/6",
            "Armas com municao devem exibir o valor atual e o maximo."
        );
        Assertions.assertContains(
            jogador.getResumoBonusArmaPrincipal(),
            "+15 dano",
            "O resumo do bonus da arma principal deve ser exibido ao jogador."
        );

        int danoComFacaPrincipal = faca.calcularDanoCriticoGarantido(jogador);

        jogador.getInventario().definirArmaPrincipal(1);

        int danoComFacaReserva = faca.calcularDanoCriticoGarantido(jogador);

        Assertions.assertEquals(
            83,
            danoComFacaPrincipal,
            "A arma principal deve receber bonus real de dano."
        );
        Assertions.assertEquals(
            60,
            danoComFacaReserva,
            "Ao sair do slot principal a arma nao deve manter o bonus."
        );
        Assertions.assertContains(
            jogador.getInventario().getDescricaoArmaOrdenada(0),
            "[Principal]",
            "A arma promovida deve aparecer como principal no inventario."
        );
        Assertions.assertContains(
            jogador.getInventario().getDescricaoArmaOrdenada(0),
            "Municao = 6/6",
            "A pistola promovida a principal deve continuar exibindo a municao."
        );
    }

    private static void testarCriticoGarantido() {
        Jogador jogador = novoJogadorPadrao();
        jogador.adicionarBonusDano(10);
        jogador.adicionarBonusCritico(0, 0.5);

        int danoCritico = jogador.getInventario().getArma(0).calcularDanoCriticoGarantido(jogador);
        Assertions.assertEquals(
            130,
            danoCritico,
            "O critico garantido deve considerar bonus de dano, multiplicador e o bonus da arma principal."
        );
    }

    private static Jogador novoJogadorPadrao() {
        return new Jogador("Teste", new Arma[] { new Faca(), new Pistola() });
    }

    private static String capturarSaida(ThrowingRunnable runnable) throws Exception {
        PrintStream saidaOriginal = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try (PrintStream captura = new PrintStream(buffer)) {
            System.setOut(captura);
            runnable.run();
        } finally {
            System.setOut(saidaOriginal);
        }

        return buffer.toString();
    }

    private static final class TestCase {
        private final String name;
        private final ThrowingRunnable runnable;

        private TestCase(String name, ThrowingRunnable runnable) {
            this.name = name;
            this.runnable = runnable;
        }

        private void run() throws Exception {
            this.runnable.run();
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    private static final class Assertions {
        private Assertions() {}

        private static void assertEquals(int expected, int actual, String message) {
            if (expected != actual) {
                throw new AssertionError(message + " Esperado: " + expected + ", atual: " + actual + ".");
            }
        }

        private static void assertEquals(String expected, String actual, String message) {
            if (!expected.equals(actual)) {
                throw new AssertionError(message + " Esperado: " + expected + ", atual: " + actual + ".");
            }
        }

        private static void assertTrue(boolean condition, String message) {
            if (!condition) {
                throw new AssertionError(message);
            }
        }

        private static void assertFalse(boolean condition, String message) {
            if (condition) {
                throw new AssertionError(message);
            }
        }

        private static void assertContains(String actual, String expectedFragment, String message) {
            if (!actual.contains(expectedFragment)) {
                throw new AssertionError(message + " Valor atual: " + actual + ".");
            }
        }

        private static void assertTalentosDisponiveis(Jogador jogador, Talento... esperados) {
            java.util.List<Talento> atuais = jogador.getTalentosDisponiveis();
            if (atuais.size() != esperados.length) {
                throw new AssertionError("Quantidade inesperada de talentos disponiveis: " + atuais);
            }

            for (Talento esperado : esperados) {
                if (!atuais.contains(esperado)) {
                    throw new AssertionError("Talento esperado ausente: " + esperado + ". Atuais: " + atuais);
                }
            }
        }
    }

    private static class TestCreature extends Criatura {
        private int eventosDeDano;

        private TestCreature(String nome, int vida) {
            super(nome, vida);
        }

        @Override
        public String getCodinome() {
            return "[TESTE " + getNome() + "]";
        }

        @Override
        public void fazAtaque(Criatura alvo) {}

        @Override
        public void fraseApresentacao() {}

        @Override
        public void fraseMorte() {}

        @Override
        public void tomaDano(int dano) {
            this.eventosDeDano++;
            super.tomaDano(dano);
        }

        @Override
        public void tomaDano(int dano, Criatura atacante) {
            tomaDano(dano);
        }

        private int getEventosDeDano() {
            return this.eventosDeDano;
        }
    }

    private static final class DefensiveEnemy extends Inimigo {
        private DefensiveEnemy(String nome, int vida, defesas.Defesa defesa) {
            super(nome, vida, 0, defesa);
        }

        @Override
        public void fazAtaque(Criatura alvo) {}

        @Override
        public void fraseApresentacao() {}

        @Override
        public void fraseMorte() {}

        @Override
        public String getTitulo() {
            return "Dummy";
        }

        @Override
        public String getPerfilCombate() {
            return "Dummy";
        }

        @Override
        public int getExperienciaConcedida() {
            return 0;
        }
    }

    private static final class LaminasGemeasDeterministicas extends LaminasGemeas {
        private final int danoFixo;

        private LaminasGemeasDeterministicas(int danoFixo) {
            this.danoFixo = danoFixo;
        }

        @Override
        protected boolean tentouAcertar() {
            return true;
        }

        @Override
        protected int calcularDano(Criatura atacante) {
            return this.danoFixo;
        }
    }

    private static final class LancaPerfuranteDeterministica extends LancaPerfurante {
        private final int danoFixo;

        private LancaPerfuranteDeterministica(int danoFixo) {
            this.danoFixo = danoFixo;
        }

        @Override
        protected boolean tentouAcertar() {
            return true;
        }

        @Override
        protected int calcularDano(Criatura atacante) {
            return this.danoFixo;
        }

        @Override
        protected boolean sortearChance(int chancePercentual) {
            return true;
        }
    }

    private static final class ArmaFatal extends Arma {
        private final int danoFixo;

        private ArmaFatal(String nome, int danoFixo) {
            super(nome, TipoArma.CURTA_DISTANCIA, danoFixo, 100, 0, 1.0);
            this.danoFixo = danoFixo;
        }

        @Override
        protected boolean tentouAcertar() {
            return true;
        }

        @Override
        protected int calcularDano(Criatura atacante) {
            return this.danoFixo;
        }

        @Override
        protected void aplicarDano(Criatura atacante, Criatura alvo, int dano) {
            aplicarDanoIgnorandoDefesa(alvo, dano);
        }
    }

    private static final class ControladorBatalhaProgramado extends ControladorBatalha {
        @Override
        public void executarTurnoJogador(Jogador jogador, Inimigo inimigo) {
            jogador.atacarComArma(0, inimigo);
        }
    }
}
