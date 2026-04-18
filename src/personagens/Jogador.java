package personagens;

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
import inventario.Inventario;
import itens.Consumivel;
import itens.Fruta;
import itens.PergaminhoCritico;
import itens.PergaminhoForca;
import itens.PocaoCura;

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

    private final Inventario inventario;
    private int nivel;
    private int experienciaAtual;
    private int experienciaTotal;
    private int experienciaProximoNivel;
    private int bonusDano;
    private int bonusChanceCritico;
    private double bonusMultiplicadorCritico;
    private int bonusBloqueioTotal;
    private int bonusParry;
    private int bonusEscudoTemporario;
    private boolean defendendo;
    private int recargaHabilidadeEspecial;
    private int indiceUltimaArmaUsada;
    private int indiceArmaDeGuarda;
    private int escudoTemporario;

    public Jogador(String nome) {
        this(
            nome,
            new Arma[] {
                new Faca(),
                new AdagaSombria(),
                new Pistola(),
                new Espada(),
                new EscudoGuardiao(),
                new Arco_e_Flecha(),
                new LaminasGemeas(),
                new LancaPerfurante(),
                new MachadoBerserker()
            }
        );
    }

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
        this.inventario.getArma(indiceArma).golpe(this, alvo);
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
        int danoEspecial = 180 + this.bonusDano;
        narrar("usou Golpe Heroico. Dano previsto: " + danoEspecial + ". Recarga: 3 turnos.");
        alvo.tomaDano(danoEspecial, this);
        this.recargaHabilidadeEspecial = 3;
    }

    public String getDescricaoHabilidadeEspecial() {
        return "Golpe Heroico - causa 180 + bonus de dano e entra em recarga por 3 turnos";
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
            + "%";
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

            int danoReduzido = (int) Math.ceil(dano * armaDefensiva.getMultiplicadorReducaoDefensiva());
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

    private boolean sortearChance(int chance) {
        return Math.random() * 100 < chance;
    }

    private void executarParry(Criatura alvo) {
        Arma armaParry = this.inventario.getArma(this.indiceUltimaArmaUsada);
        int danoParry = armaParry.calcularDanoCriticoGarantido(this);
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
        aumentarVidaMaxima(AUMENTO_VIDA_POR_NIVEL);
        restaurarVidaTotal();
        this.bonusDano += AUMENTO_DANO_POR_NIVEL;
        this.bonusChanceCritico += AUMENTO_CRITICO_POR_NIVEL;
        this.bonusMultiplicadorCritico += AUMENTO_MULTIPLICADOR_CRITICO_POR_NIVEL;
        this.bonusBloqueioTotal += AUMENTO_BLOQUEIO_POR_NIVEL;
        this.bonusParry += AUMENTO_PARRY_POR_NIVEL;
        this.bonusEscudoTemporario += AUMENTO_ESCUDO_TEMPORARIO_POR_NIVEL;
        narrar(
            "subiu para o nivel "
                + this.nivel
                + ". Atributos aumentados e vida restaurada completamente."
        );
    }

    private int calcularExperienciaProximoNivel() {
        return EXPERIENCIA_INICIAL_PROXIMO_NIVEL + ((this.nivel - 1) * 80);
    }

    private int getChanceBloqueioTotalComArma(Arma armaDefensiva) {
        return CHANCE_BLOQUEIO_TOTAL_BASE + this.bonusBloqueioTotal + armaDefensiva.getBonusBloqueioTotal();
    }

    private int getChanceParryComArma(Arma armaDefensiva) {
        return CHANCE_PARRY_BASE + this.bonusParry + armaDefensiva.getBonusParry();
    }
}
