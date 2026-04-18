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
    private static final int CHANCE_BLOQUEIO_TOTAL = 20;
    private static final int CHANCE_PARRY = 10;

    private final Inventario inventario;
    private int bonusDano;
    private int bonusChanceCritico;
    private double bonusMultiplicadorCritico;
    private boolean defendendo;
    private int recargaHabilidadeEspecial;
    private int indiceUltimaArmaUsada;
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

    public void defender() {
        this.defendendo = true;
        Arma armaDefensiva = getArmaDefensivaAtiva();
        int chanceBloqueio = CHANCE_BLOQUEIO_TOTAL + armaDefensiva.getBonusBloqueioTotal();
        int chanceParry = CHANCE_PARRY + armaDefensiva.getBonusParry();
        narrar(
            "usou Postura Defensiva. Chance de bloqueio total: "
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
        alvo.tomaDano(danoEspecial);
        this.recargaHabilidadeEspecial = 3;
    }

    public String getDescricaoHabilidadeEspecial() {
        return "Golpe Heroico - causa 180 + bonus de dano e entra em recarga por 3 turnos";
    }

    public void avancarTurno() {
        if (this.recargaHabilidadeEspecial > 0) {
            this.recargaHabilidadeEspecial--;
        }
    }

    public void prepararParaNovaBatalha() {
        this.defendendo = false;
        this.recargaHabilidadeEspecial = 0;
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
            int chanceParry = CHANCE_PARRY + armaDefensiva.getBonusParry();
            int chanceBloqueioTotal = CHANCE_BLOQUEIO_TOTAL + armaDefensiva.getBonusBloqueioTotal();

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
        this.escudoTemporario += valor;
        narrar("recebeu um escudo temporario de " + valor + ".");
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
        for (int i = 0; i < this.inventario.getQuantidadeArmas(); i++) {
            Arma arma = this.inventario.getArma(i);
            if (arma.getBonusBloqueioTotal() > 0 || arma.getBonusParry() > 0) {
                return arma;
            }
        }

        return this.inventario.getArma(this.indiceUltimaArmaUsada);
    }
}
