package personagens;

import armas.Arco_e_Flecha;
import armas.Arma;
import armas.Espada;
import armas.Faca;
import armas.Pistola;
import inventario.Inventario;
import itens.Consumivel;
import itens.Fruta;
import itens.PergaminhoCritico;
import itens.PergaminhoForca;
import itens.PocaoCura;
import java.util.Scanner;

public class Jogador extends Criatura {
    private static final Scanner SCANNER = new Scanner(System.in);

    private final Inventario inventario;
    private int bonusDano;
    private int bonusChanceCritico;
    private double bonusMultiplicadorCritico;

    public Jogador(String nome) {
        super(nome, 900);
        this.inventario = new Inventario(
            new Arma[] {
                new Faca(),
                new Pistola(),
                new Espada(),
                new Arco_e_Flecha()
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
        int acao = lerEscolhaValida(2, "Escolha uma acao:\n1 - Atacar\n2 - Usar consumivel");

        if (acao == 1) {
            atacar(alvo);
            return;
        }

        usarConsumivel();
    }

    private void atacar(Criatura alvo) {
        System.out.println("Escolha sua arma:");
        this.inventario.mostrarArmas();

        int escolha = lerEscolhaValida(this.inventario.getQuantidadeArmas(), "Escolha o numero da arma:");
        this.inventario.getArma(escolha - 1).golpe(this, alvo);
    }

    private void usarConsumivel() {
        if (!this.inventario.temConsumiveis()) {
            System.out.println("Seu inventario nao possui consumiveis.");
            return;
        }

        System.out.println("Escolha um consumivel:");
        this.inventario.mostrarConsumiveis();
        int escolha = lerEscolhaValida(
            this.inventario.getQuantidadeConsumiveis(),
            "Escolha o numero do consumivel:"
        );

        Consumivel consumivel = this.inventario.removerConsumivel(escolha - 1);
        consumivel.usar(this);
    }

    private int lerEscolhaValida(int limite, String mensagemErro) {
        while (true) {
            if (!SCANNER.hasNextInt()) {
                System.out.println("Entrada invalida. Digite um numero.");
                SCANNER.next();
                continue;
            }

            int escolha = SCANNER.nextInt();
            if (escolha >= 1 && escolha <= limite) {
                return escolha;
            }

            System.out.println(mensagemErro);
        }
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
