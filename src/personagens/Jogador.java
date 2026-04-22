package personagens;

import armas.base.Arma;
import armas.curta.AdagaSombria;
import armas.curta.EscudoGuardiao;
import armas.curta.LaminasGemeas;
import armas.curta.MachadoBerserker;
import armas.longa.LancaPerfurante;
import armas.longa.Pistola;
import inventario.Inventario;
import itens.Consumivel;
import itens.Fruta;
import itens.PergaminhoCritico;
import itens.PergaminhoForca;
import itens.PocaoCura;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import progressao.AtributoEvolutivo;
import progressao.Talento;

public class Jogador extends Criatura {
    private static final int CHANCE_BLOQUEIO_TOTAL_BASE = 20;
    private static final int CHANCE_PARRY_BASE = 10;
    private static final int EXPERIENCIA_INICIAL_PROXIMO_NIVEL = 120;
    private static final int AUMENTO_VIDA_POR_NIVEL = 70;
    private static final int AUMENTO_DANO_POR_NIVEL = 12;
    private static final int AUMENTO_CRITICO_POR_NIVEL = 4;
    private static final double AUMENTO_MULTIPLICADOR_CRITICO_POR_NIVEL = 0.15;
    private static final int AUMENTO_BLOQUEIO_POR_NIVEL = 3;
    private static final int AUMENTO_PARRY_POR_NIVEL = 2;
    private static final int AUMENTO_ESCUDO_TEMPORARIO_POR_NIVEL = 8;
    private static final int AUMENTO_DANO_ESPECIAL_POR_NIVEL = 10;
    private static final int AUMENTO_CURA_POR_NIVEL = 12;
    private static final int BONUS_DANO_ARMA_PRINCIPAL = 15;
    private static final int BONUS_ACERTO_ARMA_PRINCIPAL = 8;
    private static final int BONUS_CRITICO_ARMA_PRINCIPAL = 5;
    private static final int BONUS_BLOQUEIO_ARMA_PRINCIPAL = 6;
    private static final int BONUS_PARRY_ARMA_PRINCIPAL = 4;

    private final Inventario inventario;
    private final EnumSet<Talento> talentos;
    private int nivel;
    private int experienciaAtual;
    private int experienciaTotal;
    private int experienciaProximoNivel;
    private int pontosAtributoDisponiveis;
    private int pontosTalentoDisponiveis;
    private int bonusDano;
    private int bonusDanoEspecial;
    private int bonusChanceCritico;
    private double bonusMultiplicadorCritico;
    private int bonusBloqueioTotal;
    private int bonusParry;
    private int bonusEscudoTemporario;
    private int bonusCura;
    private double bonusReducaoDefensiva;
    private int bonusDanoParry;
    private boolean curaPurificadora;
    private boolean defendendo;
    private int recargaHabilidadeEspecial;
    private int indiceUltimaArmaUsada;
    private int indiceArmaDeGuarda;
    private int escudoTemporario;

    public Jogador(String nome, Arma[] armasEscolhidas) {
        super(nome, 900);
        this.inventario = new Inventario(
            armasEscolhidas,
            new Consumivel[] {
                new Fruta("Maca", 45, 20),
                new Fruta("Coco", 80, 35),
                new PocaoCura(150, 60),
                new PergaminhoForca(25),
                new PergaminhoCritico(20, 0.5)
            }
        );
        this.talentos = EnumSet.noneOf(Talento.class);
        this.nivel = 1;
        this.experienciaProximoNivel = EXPERIENCIA_INICIAL_PROXIMO_NIVEL;
        this.indiceArmaDeGuarda = 0;
    }

    @Override
    public void fazAtaque(Criatura alvo) {
        atacarComArma(0, alvo);
    }

    @Override
    public String getCodinome() {
        return "[JOGADOR " + getNome() + "]";
    }

    public Inventario getInventario() {
        return this.inventario;
    }

    public void atacarComArma(int indiceArma, Criatura alvo) {
        this.indiceUltimaArmaUsada = indiceArma;
        Arma armaEscolhida = this.inventario.getArma(indiceArma);
        armaEscolhida.golpe(this, alvo);
        if (armaEscolhida.usaMunicaoLimitada()) {
            narrar("municao restante de " + armaEscolhida.getNomeExibicao() + ": " + armaEscolhida.getResumoMunicaoAtual() + ".");
        }
    }

    public void usarConsumivel(Consumivel consumivel) {
        narrar("usou " + consumivel.getNomeExibicao() + ". Efeito: " + consumivel.getDescricaoPreBatalha() + ".");
        this.inventario.removerConsumivel(consumivel);
        consumivel.usar(this);
    }

    public void prepararDefesa(int indiceArma) {
        this.defendendo = true;
        this.indiceArmaDeGuarda = indiceArma;
        Arma armaDefensiva = getArmaDefensivaAtiva();
        int chanceBloqueio = getChanceBloqueioTotalComArma(armaDefensiva);
        int chanceParry = getChanceParryComArma(armaDefensiva);
        narrar(
            "usou Postura Defensiva com "
                + armaDefensiva.getNomeExibicao()
                + ". Chance de bloqueio total: "
                + chanceBloqueio
                + "%, chance de parry: "
                + chanceParry
                + "%."
        );
    }

    public boolean podeUsarHabilidadeEspecial() {
        return this.recargaHabilidadeEspecial == 0;
    }

    public void usarHabilidadeEspecial(Criatura alvo) {
        int danoEspecial = 180 + this.bonusDano + this.bonusDanoEspecial;
        narrar("usou Golpe Heroico. Dano previsto: " + danoEspecial + ". Recarga: 3 turnos.");
        alvo.tomaDano(danoEspecial, this);
        this.recargaHabilidadeEspecial = 3;
    }

    public String getDescricaoHabilidadeEspecial() {
        return "Golpe Heroico - causa 180 + bonus de dano + bonus especial e entra em recarga por 3 turnos";
    }

    public String getResumoProgressao() {
        return "Nivel "
            + this.nivel
            + " | XP "
            + this.experienciaAtual
            + "/"
            + this.experienciaProximoNivel
            + " | Vida maxima "
            + getVidaMaxima()
            + " | Bonus dano +"
            + this.bonusDano
            + " | Critico +"
            + this.bonusChanceCritico
            + "% | Multiplicador critico +"
            + this.bonusMultiplicadorCritico
            + " | Bloqueio +"
            + this.bonusBloqueioTotal
            + "% | Parry +"
            + this.bonusParry
            + "% | Pontos atributo "
            + this.pontosAtributoDisponiveis
            + " | Pontos talento "
            + this.pontosTalentoDisponiveis;
    }

    public String getResumoTalentos() {
        if (this.talentos.isEmpty()) {
            return "Nenhum talento aprendido";
        }

        List<String> nomes = new ArrayList<>();
        for (Talento talento : this.talentos) {
            nomes.add(talento.getNomeExibicao());
        }
        return String.join(", ", nomes);
    }

    public List<Talento> getTalentosAprendidos() {
        return new ArrayList<>(this.talentos);
    }

    public String getResumoBonusArmaPrincipal() {
        return "+"
            + BONUS_DANO_ARMA_PRINCIPAL
            + " dano, +"
            + BONUS_ACERTO_ARMA_PRINCIPAL
            + "% precisao, +"
            + BONUS_CRITICO_ARMA_PRINCIPAL
            + "% critico, +"
            + BONUS_BLOQUEIO_ARMA_PRINCIPAL
            + "% bloqueio e +"
            + BONUS_PARRY_ARMA_PRINCIPAL
            + "% parry.";
    }

    public void avancarTurno() {
        if (this.recargaHabilidadeEspecial > 0) {
            this.recargaHabilidadeEspecial--;
        }
    }

    public void prepararParaNovaBatalha() {
        this.defendendo = false;
        this.recargaHabilidadeEspecial = 0;
        this.indiceArmaDeGuarda = 0;
        this.escudoTemporario = 0;
        this.inventario.prepararParaNovaBatalha();
        removerEfeitosNegativos();
        restaurarVidaTotal();
    }

    @Override
    public void tomaDano(int dano) {
        tomaDano(dano, null);
    }

    @Override
    public void tomaDano(int dano, Criatura atacante) {
        if (this.escudoTemporario > 0) {
            int absorvido = Math.min(dano, this.escudoTemporario);
            this.escudoTemporario -= absorvido;
            dano -= absorvido;
            narrar("teve " + absorvido + " de dano absorvido pela energia restauradora.");
            if (dano == 0) {
                narrar("nao sofreu dano gracas ao escudo temporario.");
                return;
            }
        }

        if (this.defendendo) {
            this.defendendo = false;
            Arma armaDefensiva = getArmaDefensivaAtiva();
            int chanceParry = getChanceParryComArma(armaDefensiva);
            int chanceBloqueioTotal = getChanceBloqueioTotalComArma(armaDefensiva);

            if (sortearChance(chanceParry) && atacante != null) {
                narrar("executou um parry perfeito. Bloqueou todo o dano e respondera com contra-ataque critico.");
                executarParry(atacante);
                return;
            }

            if (sortearChance(chanceBloqueioTotal)) {
                narrar("bloqueou completamente o ataque e nao sofreu dano.");
                return;
            }

            double multiplicador = armaDefensiva.getMultiplicadorReducaoDefensiva() - this.bonusReducaoDefensiva;
            if (multiplicador < 0.05) {
                multiplicador = 0.05;
            }
            int danoReduzido = (int) Math.ceil(dano * multiplicador);
            narrar("reduziu o dano usando a postura defensiva.");
            super.tomaDano(danoReduzido);
            return;
        }

        super.tomaDano(dano);
    }

    public void adicionarBonusDano(int bonusDano) {
        this.bonusDano += bonusDano;
        narrar("teve o dano aumentado em +" + bonusDano + ".");
    }

    public void adicionarBonusCritico(int bonusChanceCritico, double bonusMultiplicadorCritico) {
        this.bonusChanceCritico += bonusChanceCritico;
        this.bonusMultiplicadorCritico += bonusMultiplicadorCritico;
        narrar(
            "teve +"
                + bonusChanceCritico
                + "% de chance critica e +"
                + bonusMultiplicadorCritico
                + " no multiplicador critico."
        );
    }

    public int getBonusDano() {
        return this.bonusDano;
    }

    public int getBonusChanceCritico() {
        return this.bonusChanceCritico;
    }

    public double getBonusMultiplicadorCritico() {
        return this.bonusMultiplicadorCritico;
    }

    @Override
    public void fraseApresentacao() {
        falar("Nao contava com minha astucia!");
    }

    @Override
    public void fraseMorte() {
        falar("Eu vou voltar pra te arrasar!");
    }

    public void aplicarEscudoTemporario(int valor) {
        int valorFinal = valor + this.bonusEscudoTemporario;
        this.escudoTemporario += valorFinal;
        narrar("recebeu um escudo temporario de " + valorFinal + ".");
    }

    public void aplicarRecuperacao(int curaBase, int escudoBase) {
        int curaFinal = curaBase + this.bonusCura;
        curar(curaFinal);
        aplicarEscudoTemporario(escudoBase);
        if (this.curaPurificadora) {
            removerEfeitosNegativos();
        }
    }

    public int ganharExperiencia(int experiencia) {
        int niveisGanhos = 0;
        this.experienciaAtual += experiencia;
        this.experienciaTotal += experiencia;
        narrar("ganhou " + experiencia + " de experiencia.");

        while (this.experienciaAtual >= this.experienciaProximoNivel) {
            this.experienciaAtual -= this.experienciaProximoNivel;
            subirNivel();
            niveisGanhos++;
        }

        narrar("agora esta em " + getResumoProgressao() + ".");
        return niveisGanhos;
    }

    public int getNivel() {
        return this.nivel;
    }

    public int getExperienciaAtual() {
        return this.experienciaAtual;
    }

    public int getExperienciaProximoNivel() {
        return this.experienciaProximoNivel;
    }

    public int getPontosAtributoDisponiveis() {
        return this.pontosAtributoDisponiveis;
    }

    public int getPontosTalentoDisponiveis() {
        return this.pontosTalentoDisponiveis;
    }

    public List<Talento> getTalentosDisponiveis() {
        List<Talento> disponiveis = new ArrayList<>();
        for (Talento.RamoTalento ramo : Talento.RamoTalento.values()) {
            Talento proximoTalento = getProximoTalentoDoRamo(ramo);
            if (proximoTalento != null && proximoTalento.estaDisponivel(this.nivel)) {
                disponiveis.add(proximoTalento);
            }
        }
        return disponiveis;
    }

    public int getBonusDanoComArma(Arma arma) {
        return this.bonusDano + (recebeBonusArmaPrincipal(arma) ? BONUS_DANO_ARMA_PRINCIPAL : 0);
    }

    public int getBonusChanceAcertoComArma(Arma arma) {
        if (recebeBonusArmaPrincipal(arma)) {
            return BONUS_ACERTO_ARMA_PRINCIPAL;
        }
        return 0;
    }

    public int getBonusChanceCriticoComArma(Arma arma) {
        return this.bonusChanceCritico + (recebeBonusArmaPrincipal(arma) ? BONUS_CRITICO_ARMA_PRINCIPAL : 0);
    }

    public double getBonusMultiplicadorCriticoComArma(Arma arma) {
        return this.bonusMultiplicadorCritico;
    }

    public void investirAtributo(AtributoEvolutivo atributo) {
        if (this.pontosAtributoDisponiveis <= 0) {
            return;
        }

        this.pontosAtributoDisponiveis--;
        switch (atributo) {
            case VIGOR:
                aumentarVidaMaxima(60);
                restaurarVidaTotal();
                narrar("investiu em Vigor.");
                break;
            case FORCA:
                this.bonusDano += 10;
                this.bonusDanoEspecial += 20;
                narrar("investiu em Forca.");
                break;
            case PRECISAO:
                this.bonusChanceCritico += 5;
                this.bonusMultiplicadorCritico += 0.10;
                narrar("investiu em Precisao.");
                break;
            case GUARDA:
                this.bonusBloqueioTotal += 6;
                this.bonusParry += 4;
                this.bonusEscudoTemporario += 10;
                narrar("investiu em Guarda.");
                break;
            default:
                break;
        }
    }

    public void aprenderTalento(Talento talento) {
        if (this.pontosTalentoDisponiveis <= 0 || !getTalentosDisponiveis().contains(talento)) {
            return;
        }

        this.pontosTalentoDisponiveis--;
        this.talentos.add(talento);
        aplicarEfeitoTalento(talento);
        narrar("aprendeu o talento " + talento.getNomeExibicao() + ".");
    }

    private boolean sortearChance(int chance) {
        return Math.random() * 100 < chance;
    }

    private void executarParry(Criatura alvo) {
        Arma armaParry = this.inventario.getArma(this.indiceUltimaArmaUsada);
        int danoParry = armaParry.calcularDanoCriticoGarantido(this) + this.bonusDanoParry;
        narrar(
            "contra-atacou com "
                + armaParry.getNomeExibicao()
                + " e causara "
                + danoParry
                + " de dano critico."
        );
        alvo.tomaDano(danoParry, this);
    }

    private Arma getArmaDefensivaAtiva() {
        if (this.indiceArmaDeGuarda >= 0 && this.indiceArmaDeGuarda < this.inventario.getQuantidadeArmas()) {
            return this.inventario.getArma(this.indiceArmaDeGuarda);
        }

        return this.inventario.getArma(this.indiceUltimaArmaUsada);
    }

    private void subirNivel() {
        this.nivel++;
        this.experienciaProximoNivel = calcularExperienciaProximoNivel();
        this.pontosAtributoDisponiveis++;
        this.pontosTalentoDisponiveis++;
        aumentarVidaMaxima(AUMENTO_VIDA_POR_NIVEL);
        restaurarVidaTotal();
        this.bonusDano += AUMENTO_DANO_POR_NIVEL;
        this.bonusDanoEspecial += AUMENTO_DANO_ESPECIAL_POR_NIVEL;
        this.bonusChanceCritico += AUMENTO_CRITICO_POR_NIVEL;
        this.bonusMultiplicadorCritico += AUMENTO_MULTIPLICADOR_CRITICO_POR_NIVEL;
        this.bonusBloqueioTotal += AUMENTO_BLOQUEIO_POR_NIVEL;
        this.bonusParry += AUMENTO_PARRY_POR_NIVEL;
        this.bonusEscudoTemporario += AUMENTO_ESCUDO_TEMPORARIO_POR_NIVEL;
        this.bonusCura += AUMENTO_CURA_POR_NIVEL;
        desbloquearArmasPorNivel();
        narrar(
            "subiu para o nivel "
                + this.nivel
                + ". Atributos base aumentados, vida restaurada e novas escolhas disponiveis."
        );
    }

    private int calcularExperienciaProximoNivel() {
        return EXPERIENCIA_INICIAL_PROXIMO_NIVEL + ((this.nivel - 1) * 80);
    }

    private int getChanceBloqueioTotalComArma(Arma armaDefensiva) {
        int bonusPrincipal = recebeBonusArmaPrincipal(armaDefensiva) ? BONUS_BLOQUEIO_ARMA_PRINCIPAL : 0;
        return CHANCE_BLOQUEIO_TOTAL_BASE + this.bonusBloqueioTotal + armaDefensiva.getBonusBloqueioTotal() + bonusPrincipal;
    }

    private int getChanceParryComArma(Arma armaDefensiva) {
        int bonusPrincipal = recebeBonusArmaPrincipal(armaDefensiva) ? BONUS_PARRY_ARMA_PRINCIPAL : 0;
        return CHANCE_PARRY_BASE + this.bonusParry + armaDefensiva.getBonusParry() + bonusPrincipal;
    }

    private void aplicarEfeitoTalento(Talento talento) {
        switch (talento) {
            case FEROCIDADE:
                this.bonusDano += 18;
                this.bonusDanoEspecial += 30;
                break;
            case BASTIAO:
                this.bonusBloqueioTotal += 10;
                this.bonusParry += 10;
                this.bonusReducaoDefensiva += 0.08;
                break;
            case ALQUIMISTA:
                this.bonusCura += 40;
                this.bonusEscudoTemporario += 25;
                break;
            case EXECUTOR:
                this.bonusChanceCritico += 8;
                this.bonusMultiplicadorCritico += 0.25;
                this.bonusDanoEspecial += 35;
                break;
            case PAREDE_DE_ACO:
                this.bonusBloqueioTotal += 15;
                this.bonusParry += 12;
                this.bonusReducaoDefensiva += 0.12;
                break;
            case TRANSMUTADOR:
                this.bonusCura += 70;
                this.bonusEscudoTemporario += 40;
                this.curaPurificadora = true;
                break;
            case LAMINA_ASCENDENTE:
                this.bonusDano += 25;
                this.bonusChanceCritico += 10;
                this.bonusDanoEspecial += 60;
                break;
            case PARRY_LENDARIO:
                this.bonusBloqueioTotal += 20;
                this.bonusParry += 18;
                this.bonusDanoParry += 80;
                this.bonusReducaoDefensiva += 0.12;
                break;
            case ELIXIR_DA_FENIX:
                this.bonusCura += 100;
                this.bonusEscudoTemporario += 60;
                this.curaPurificadora = true;
                aumentarVidaMaxima(80);
                restaurarVidaTotal();
                break;
            default:
                break;
        }
    }

    private void desbloquearArmasPorNivel() {
        switch (this.nivel) {
            case 2:
                desbloquearArma(new AdagaSombria());
                break;
            case 3:
                desbloquearArma(new EscudoGuardiao());
                break;
            case 4:
                desbloquearArma(new LancaPerfurante());
                break;
            case 5:
                desbloquearArma(new LaminasGemeas());
                break;
            case 6:
                desbloquearArma(new MachadoBerserker());
                break;
            case 7:
                desbloquearArma(new Pistola());
                break;
            default:
                break;
        }
    }

    private void desbloquearArma(Arma arma) {
        int quantidadeAntes = this.inventario.getQuantidadeArmas();
        this.inventario.adicionarArmaSeAusente(arma);
        if (this.inventario.getQuantidadeArmas() > quantidadeAntes) {
            narrar("desbloqueou a arma " + arma.getNomeExibicao() + ".");
        }
    }

    private Talento getProximoTalentoDoRamo(Talento.RamoTalento ramo) {
        int maiorCamadaAprendida = 0;

        for (Talento talento : this.talentos) {
            if (talento.getRamo() == ramo && talento.getCamada() > maiorCamadaAprendida) {
                maiorCamadaAprendida = talento.getCamada();
            }
        }

        int proximaCamada = maiorCamadaAprendida + 1;
        for (Talento talento : Talento.values()) {
            if (talento.getRamo() == ramo && talento.getCamada() == proximaCamada) {
                return talento;
            }
        }

        return null;
    }

    private boolean recebeBonusArmaPrincipal(Arma arma) {
        return this.inventario.isArmaPrincipal(arma);
    }
}
