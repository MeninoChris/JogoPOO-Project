package personagens;

import armas.Arco_e_Flecha;
import armas.Arma;
import armas.Espada;
import armas.Faca;
import armas.Pistola;
import inventario.Inventario;
import java.util.Scanner;

public class Jogador extends Criatura {
    private static final Scanner SCANNER = new Scanner(System.in);
    private final Inventario inventario;

    public Jogador(String nome) {
        super(nome, 900);
        this.inventario = new Inventario(
            new Arma[] {
                new Faca(),
                new Pistola(),
                new Espada(),
                new Arco_e_Flecha()
            }
        );
    }

    @Override
    public void fazAtaque(Criatura alvo) {
        System.out.println("Escolha sua arma:");
        this.inventario.mostrarArmas();

        int escolha = lerEscolhaValida(this.inventario.getQuantidadeArmas());
        this.inventario.getArma(escolha - 1).golpe(alvo);

    }

    private int lerEscolhaValida(int quantidadeArmas) {
        while (true) {
            if (!SCANNER.hasNextInt()) {
                System.out.println("Entrada invalida. Digite um numero.");
                SCANNER.next();
                continue;
            }

            int escolha = SCANNER.nextInt();
            if (escolha >= 1 && escolha <= quantidadeArmas) {
                return escolha;
            }

            System.out.println("Numero invalido, escolha outro:");
        }
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
