package personagens;

import armas.AdagaSombria;
import armas.Arco_e_Flecha;
import armas.Arma;
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

    public Jogador(String nome) {
        this(
            nome,
            new Arma[] {
                new Faca(),
                new AdagaSombria(),
                new Pistola(),
                new Espada(),
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
                new Fruta("Maca", 35),
                new Fruta("Coco", 50),
                new PocaoCura(120),
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
        narrar(
            "usou Postura Defensiva. Chance de bloqueio total: "
                + CHANCE_BLOQUEIO_TOTAL
                + "%, chance de parry: "
                + CHANCE_PARRY
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
        restaurarVidaTotal();
    }

    @Override
    public void tomaDano(int dano) {
        tomaDano(dano, null);
    }

    @Override
    public void tomaDano(int dano, Criatura atacante) {
        if (this.defendendo) {
            this.defendendo = false;

            if (sortearChance(CHANCE_PARRY) && atacante != null) {
                narrar("executou um parry perfeito. Bloqueou todo o dano e respondera com contra-ataque critico.");
                executarParry(atacante);
                return;
            }

            if (sortearChance(CHANCE_BLOQUEIO_TOTAL)) {
                narrar("bloqueou completamente o ataque e nao sofreu dano.");
                return;
            }

            int danoReduzido = (int) Math.ceil(dano * 0.5);
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
}
