package progressao;

import armas.base.Arma;
import armas.curta.AdagaSombria;
import armas.curta.EscudoGuardiao;
import armas.curta.LaminasGemeas;
import armas.curta.MachadoBerserker;
import armas.longa.LancaPerfurante;
import armas.longa.Pistola;
import inventario.Inventario;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import personagens.Jogador;

public class ProgressaoJogador {
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

    private final EnumSet<Talento> talentos;
    private int nivel;
    private int experienciaAtual;
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

    public ProgressaoJogador() {
        this.talentos = EnumSet.noneOf(Talento.class);
        this.nivel = 1;
        this.experienciaProximoNivel = EXPERIENCIA_INICIAL_PROXIMO_NIVEL;
    }

    public String getResumoProgressao(Jogador jogador) {
        return "Nivel "
            + this.nivel
            + " | XP "
            + this.experienciaAtual
            + "/"
            + this.experienciaProximoNivel
            + " | Vida maxima "
            + jogador.getVidaMaxima()
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

    public void adicionarBonusDano(Jogador jogador, int bonusDano) {
        this.bonusDano += bonusDano;
        jogador.narrar("teve o dano aumentado em +" + bonusDano + ".");
    }

    public void adicionarBonusCritico(Jogador jogador, int bonusChanceCritico, double bonusMultiplicadorCritico) {
        this.bonusChanceCritico += bonusChanceCritico;
        this.bonusMultiplicadorCritico += bonusMultiplicadorCritico;
        jogador.narrar(
            "teve +"
                + bonusChanceCritico
                + "% de chance critica e +"
                + bonusMultiplicadorCritico
                + " no multiplicador critico."
        );
    }

    public int ganharExperiencia(Jogador jogador, Inventario inventario, int experiencia) {
        int niveisGanhos = 0;
        this.experienciaAtual += experiencia;
        jogador.narrar("ganhou " + experiencia + " de experiencia.");

        while (this.experienciaAtual >= this.experienciaProximoNivel) {
            this.experienciaAtual -= this.experienciaProximoNivel;
            subirNivel(jogador, inventario);
            niveisGanhos++;
        }

        jogador.narrar("agora esta em " + getResumoProgressao(jogador) + ".");
        return niveisGanhos;
    }

    public int getNivel() {
        return this.nivel;
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

    public void investirAtributo(Jogador jogador, AtributoEvolutivo atributo) {
        if (this.pontosAtributoDisponiveis <= 0) {
            return;
        }

        this.pontosAtributoDisponiveis--;
        switch (atributo) {
            case VIGOR:
                jogador.aumentarVidaMaxima(60);
                jogador.restaurarVidaTotal();
                jogador.narrar("investiu em Vigor.");
                break;
            case FORCA:
                this.bonusDano += 10;
                this.bonusDanoEspecial += 20;
                jogador.narrar("investiu em Forca.");
                break;
            case PRECISAO:
                this.bonusChanceCritico += 5;
                this.bonusMultiplicadorCritico += 0.10;
                jogador.narrar("investiu em Precisao.");
                break;
            case GUARDA:
                this.bonusBloqueioTotal += 6;
                this.bonusParry += 4;
                this.bonusEscudoTemporario += 10;
                jogador.narrar("investiu em Guarda.");
                break;
            default:
                break;
        }
    }

    public void aprenderTalento(Jogador jogador, Talento talento) {
        if (this.pontosTalentoDisponiveis <= 0 || !getTalentosDisponiveis().contains(talento)) {
            return;
        }

        this.pontosTalentoDisponiveis--;
        this.talentos.add(talento);
        aplicarEfeitoTalento(jogador, talento);
        jogador.narrar("aprendeu o talento " + talento.getNomeExibicao() + ".");
    }

    public int getBonusDano() {
        return this.bonusDano;
    }

    public int getBonusDanoEspecial() {
        return this.bonusDanoEspecial;
    }

    public int getBonusChanceCritico() {
        return this.bonusChanceCritico;
    }

    public double getBonusMultiplicadorCritico() {
        return this.bonusMultiplicadorCritico;
    }

    public int getBonusBloqueioTotal() {
        return this.bonusBloqueioTotal;
    }

    public int getBonusParry() {
        return this.bonusParry;
    }

    public int getBonusEscudoTemporario() {
        return this.bonusEscudoTemporario;
    }

    public int getBonusCura() {
        return this.bonusCura;
    }

    public double getBonusReducaoDefensiva() {
        return this.bonusReducaoDefensiva;
    }

    public int getBonusDanoParry() {
        return this.bonusDanoParry;
    }

    public boolean isCuraPurificadora() {
        return this.curaPurificadora;
    }

    private void subirNivel(Jogador jogador, Inventario inventario) {
        this.nivel++;
        this.experienciaProximoNivel = calcularExperienciaProximoNivel();
        this.pontosAtributoDisponiveis++;
        this.pontosTalentoDisponiveis++;
        jogador.aumentarVidaMaxima(AUMENTO_VIDA_POR_NIVEL);
        jogador.restaurarVidaTotal();
        this.bonusDano += AUMENTO_DANO_POR_NIVEL;
        this.bonusDanoEspecial += AUMENTO_DANO_ESPECIAL_POR_NIVEL;
        this.bonusChanceCritico += AUMENTO_CRITICO_POR_NIVEL;
        this.bonusMultiplicadorCritico += AUMENTO_MULTIPLICADOR_CRITICO_POR_NIVEL;
        this.bonusBloqueioTotal += AUMENTO_BLOQUEIO_POR_NIVEL;
        this.bonusParry += AUMENTO_PARRY_POR_NIVEL;
        this.bonusEscudoTemporario += AUMENTO_ESCUDO_TEMPORARIO_POR_NIVEL;
        this.bonusCura += AUMENTO_CURA_POR_NIVEL;
        desbloquearArmasPorNivel(jogador, inventario);
        jogador.narrar(
            "subiu para o nivel "
                + this.nivel
                + ". Atributos base aumentados, vida restaurada e novas escolhas disponiveis."
        );
    }

    private int calcularExperienciaProximoNivel() {
        return EXPERIENCIA_INICIAL_PROXIMO_NIVEL + ((this.nivel - 1) * 80);
    }

    private void aplicarEfeitoTalento(Jogador jogador, Talento talento) {
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
                jogador.aumentarVidaMaxima(80);
                jogador.restaurarVidaTotal();
                break;
            default:
                break;
        }
    }

    private void desbloquearArmasPorNivel(Jogador jogador, Inventario inventario) {
        switch (this.nivel) {
            case 2:
                desbloquearArma(jogador, inventario, new AdagaSombria());
                break;
            case 3:
                desbloquearArma(jogador, inventario, new EscudoGuardiao());
                break;
            case 4:
                desbloquearArma(jogador, inventario, new LancaPerfurante());
                break;
            case 5:
                desbloquearArma(jogador, inventario, new LaminasGemeas());
                break;
            case 6:
                desbloquearArma(jogador, inventario, new MachadoBerserker());
                break;
            case 7:
                desbloquearArma(jogador, inventario, new Pistola());
                break;
            default:
                break;
        }
    }

    private void desbloquearArma(Jogador jogador, Inventario inventario, Arma arma) {
        int quantidadeAntes = inventario.getQuantidadeArmas();
        inventario.adicionarArmaSeAusente(arma);
        if (inventario.getQuantidadeArmas() > quantidadeAntes) {
            jogador.narrar("desbloqueou a arma " + arma.getNomeExibicao() + ".");
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
}
