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
    private final Inventario inventario;
    private int bonusDano;
    private int bonusChanceCritico;
    private double bonusMultiplicadorCritico;
    private boolean defendendo;
    private int recargaHabilidadeEspecial;

    public Jogador(String nome) {
        super(nome, 900);
        this.inventario = new Inventario(
            new Arma[] {
                new Faca(),
                new AdagaSombria(),
                new Pistola(),
                new Espada(),
                new Arco_e_Flecha(),
                new LaminasGemeas(),
                new LancaPerfurante(),
                new MachadoBerserker()
            },
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

    public Inventario getInventario() {
        return this.inventario;
    }

    public void atacarComArma(int indiceArma, Criatura alvo) {
        this.inventario.getArma(indiceArma).golpe(this, alvo);
    }

    public void usarConsumivel(Consumivel consumivel) {
        this.inventario.removerConsumivel(consumivel);
        consumivel.usar(this);
    }

    public void defender() {
        this.defendendo = true;
        System.out.println(this.getNome() + " assumiu postura defensiva.");
    }

    public boolean podeUsarHabilidadeEspecial() {
        return this.recargaHabilidadeEspecial == 0;
    }

    public void usarHabilidadeEspecial(Criatura alvo) {
        int danoEspecial = 180 + this.bonusDano;
        System.out.println(this.getNome() + " usou Golpe Heroico!");
        alvo.tomaDano(danoEspecial);
        this.recargaHabilidadeEspecial = 3;
    }

    public void avancarTurno() {
        if (this.recargaHabilidadeEspecial > 0) {
            this.recargaHabilidadeEspecial--;
        }
    }

    @Override
    public void tomaDano(int dano) {
        if (this.defendendo) {
            int danoReduzido = (int) Math.ceil(dano * 0.5);
            System.out.println(this.getNome() + " reduziu o dano ao defender.");
            this.defendendo = false;
            super.tomaDano(danoReduzido);
            return;
        }

        super.tomaDano(dano);
    }

    public void adicionarBonusDano(int bonusDano) {
        this.bonusDano += bonusDano;
        System.out.println(this.getNome() + " recebeu +" + bonusDano + " de dano.");
    }

    public void adicionarBonusCritico(int bonusChanceCritico, double bonusMultiplicadorCritico) {
        this.bonusChanceCritico += bonusChanceCritico;
        this.bonusMultiplicadorCritico += bonusMultiplicadorCritico;
        System.out.println(
            this.getNome()
                + " recebeu +"
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
        System.out.println("Nao contava com minha astucia!");
    }

    @Override
    public void fraseMorte() {
        System.out.println("Eu vou voltar pra te arrasar!");
    }
}
