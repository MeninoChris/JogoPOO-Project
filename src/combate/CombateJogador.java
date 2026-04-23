package combate;

import armas.base.Arma;
import inventario.Inventario;
import personagens.Criatura;
import personagens.Jogador;
import progressao.ProgressaoJogador;

public class CombateJogador {
    private static final int CHANCE_BLOQUEIO_TOTAL_BASE = 20;
    private static final int CHANCE_PARRY_BASE = 10;
    private static final int BONUS_DANO_ARMA_PRINCIPAL = 15;
    private static final int BONUS_ACERTO_ARMA_PRINCIPAL = 8;
    private static final int BONUS_CRITICO_ARMA_PRINCIPAL = 5;
    private static final int BONUS_BLOQUEIO_ARMA_PRINCIPAL = 6;
    private static final int BONUS_PARRY_ARMA_PRINCIPAL = 4;

    private boolean defendendo;
    private int recargaHabilidadeEspecial;
    private int indiceUltimaArmaUsada;
    private int indiceArmaDeGuarda;
    private int escudoTemporario;

    public CombateJogador() {
        this.indiceArmaDeGuarda = 0;
    }

    public void atacarComArma(Jogador jogador, Inventario inventario, int indiceArma, Criatura alvo) {
        this.indiceUltimaArmaUsada = indiceArma;
        Arma armaEscolhida = inventario.getArma(indiceArma);
        armaEscolhida.golpe(jogador, alvo);
        if (armaEscolhida.usaMunicaoLimitada()) {
            jogador.narrar("municao restante de " + armaEscolhida.getNomeExibicao() + ": " + armaEscolhida.getResumoMunicaoAtual() + ".");
        }
    }

    public void prepararDefesa(Jogador jogador, Inventario inventario, ProgressaoJogador progressao, int indiceArma) {
        this.defendendo = true;
        this.indiceArmaDeGuarda = indiceArma;
        Arma armaDefensiva = getArmaDefensivaAtiva(inventario);
        int chanceBloqueio = getChanceBloqueioTotalComArma(inventario, progressao, armaDefensiva);
        int chanceParry = getChanceParryComArma(inventario, progressao, armaDefensiva);
        jogador.narrar(
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

    public void usarHabilidadeEspecial(Jogador jogador, ProgressaoJogador progressao, Criatura alvo) {
        int danoEspecial = 180 + progressao.getBonusDano() + progressao.getBonusDanoEspecial();
        jogador.narrar("usou Golpe Heroico. Dano previsto: " + danoEspecial + ". Recarga: 3 turnos.");
        alvo.tomaDano(danoEspecial, jogador);
        this.recargaHabilidadeEspecial = 3;
    }

    public String getDescricaoHabilidadeEspecial() {
        return "Golpe Heroico - causa 180 + bonus de dano + bonus especial e entra em recarga por 3 turnos";
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

    public void prepararParaNovaBatalha(Jogador jogador) {
        this.defendendo = false;
        this.recargaHabilidadeEspecial = 0;
        this.indiceArmaDeGuarda = 0;
        this.escudoTemporario = 0;
        jogador.removerEfeitosNegativos();
        jogador.restaurarVidaTotal();
    }

    public void processarDanoRecebido(
        Jogador jogador,
        Inventario inventario,
        ProgressaoJogador progressao,
        int dano,
        Criatura atacante
    ) {
        if (this.escudoTemporario > 0) {
            int absorvido = Math.min(dano, this.escudoTemporario);
            this.escudoTemporario -= absorvido;
            dano -= absorvido;
            jogador.narrar("teve " + absorvido + " de dano absorvido pela energia restauradora.");
            if (dano == 0) {
                jogador.narrar("nao sofreu dano gracas ao escudo temporario.");
                return;
            }
        }

        if (this.defendendo) {
            this.defendendo = false;
            Arma armaDefensiva = getArmaDefensivaAtiva(inventario);
            int chanceParry = getChanceParryComArma(inventario, progressao, armaDefensiva);
            int chanceBloqueioTotal = getChanceBloqueioTotalComArma(inventario, progressao, armaDefensiva);

            if (sortearChance(chanceParry) && atacante != null) {
                jogador.narrar("executou um parry perfeito. Bloqueou todo o dano e respondera com contra-ataque critico.");
                executarParry(jogador, inventario, progressao, atacante);
                return;
            }

            if (sortearChance(chanceBloqueioTotal)) {
                jogador.narrar("bloqueou completamente o ataque e nao sofreu dano.");
                return;
            }

            double multiplicador = armaDefensiva.getMultiplicadorReducaoDefensiva() - progressao.getBonusReducaoDefensiva();
            if (multiplicador < 0.05) {
                multiplicador = 0.05;
            }
            int danoReduzido = (int) Math.ceil(dano * multiplicador);
            jogador.narrar("reduziu o dano usando a postura defensiva.");
            jogador.sofrerDanoDireto(danoReduzido);
            return;
        }

        jogador.sofrerDanoDireto(dano);
    }

    public void aplicarEscudoTemporario(Jogador jogador, ProgressaoJogador progressao, int valor) {
        int valorFinal = valor + progressao.getBonusEscudoTemporario();
        this.escudoTemporario += valorFinal;
        jogador.narrar("recebeu um escudo temporario de " + valorFinal + ".");
    }

    public void aplicarRecuperacao(Jogador jogador, ProgressaoJogador progressao, int curaBase, int escudoBase) {
        int curaFinal = curaBase + progressao.getBonusCura();
        jogador.curar(curaFinal);
        aplicarEscudoTemporario(jogador, progressao, escudoBase);
        if (progressao.isCuraPurificadora()) {
            jogador.removerEfeitosNegativos();
        }
    }

    public int getBonusDanoComArma(Inventario inventario, ProgressaoJogador progressao, Arma arma) {
        return progressao.getBonusDano() + (recebeBonusArmaPrincipal(inventario, arma) ? BONUS_DANO_ARMA_PRINCIPAL : 0);
    }

    public int getBonusChanceAcertoComArma(Inventario inventario, Arma arma) {
        if (recebeBonusArmaPrincipal(inventario, arma)) {
            return BONUS_ACERTO_ARMA_PRINCIPAL;
        }
        return 0;
    }

    public int getBonusChanceCriticoComArma(Inventario inventario, ProgressaoJogador progressao, Arma arma) {
        return progressao.getBonusChanceCritico() + (recebeBonusArmaPrincipal(inventario, arma) ? BONUS_CRITICO_ARMA_PRINCIPAL : 0);
    }

    public double getBonusMultiplicadorCriticoComArma(ProgressaoJogador progressao) {
        return progressao.getBonusMultiplicadorCritico();
    }

    private boolean sortearChance(int chance) {
        return Math.random() * 100 < chance;
    }

    private void executarParry(Jogador jogador, Inventario inventario, ProgressaoJogador progressao, Criatura alvo) {
        Arma armaParry = inventario.getArma(this.indiceUltimaArmaUsada);
        int danoParry = armaParry.calcularDanoCriticoGarantido(jogador) + progressao.getBonusDanoParry();
        jogador.narrar(
            "contra-atacou com "
                + armaParry.getNomeExibicao()
                + " e causara "
                + danoParry
                + " de dano critico."
        );
        alvo.tomaDano(danoParry, jogador);
    }

    private Arma getArmaDefensivaAtiva(Inventario inventario) {
        if (this.indiceArmaDeGuarda >= 0 && this.indiceArmaDeGuarda < inventario.getQuantidadeArmas()) {
            return inventario.getArma(this.indiceArmaDeGuarda);
        }

        return inventario.getArma(this.indiceUltimaArmaUsada);
    }

    private int getChanceBloqueioTotalComArma(Inventario inventario, ProgressaoJogador progressao, Arma armaDefensiva) {
        int bonusPrincipal = recebeBonusArmaPrincipal(inventario, armaDefensiva) ? BONUS_BLOQUEIO_ARMA_PRINCIPAL : 0;
        return CHANCE_BLOQUEIO_TOTAL_BASE + progressao.getBonusBloqueioTotal() + armaDefensiva.getBonusBloqueioTotal() + bonusPrincipal;
    }

    private int getChanceParryComArma(Inventario inventario, ProgressaoJogador progressao, Arma armaDefensiva) {
        int bonusPrincipal = recebeBonusArmaPrincipal(inventario, armaDefensiva) ? BONUS_PARRY_ARMA_PRINCIPAL : 0;
        return CHANCE_PARRY_BASE + progressao.getBonusParry() + armaDefensiva.getBonusParry() + bonusPrincipal;
    }

    private boolean recebeBonusArmaPrincipal(Inventario inventario, Arma arma) {
        return inventario.isArmaPrincipal(arma);
    }
}
