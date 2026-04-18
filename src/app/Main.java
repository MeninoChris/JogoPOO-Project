package app;

import personagens.Criatura;
import personagens.Jogador;
import personagens.Malignus;

public class Main {
    public static void main(String[] args) {
        Criatura jogador = new Jogador("MeninoChris");
        Criatura inimigo = new Malignus();

        System.out.println("#####################");
        System.out.println("A Grande Batalha");
        System.out.println("#####################");

        jogador.fraseApresentacao();
        inimigo.fraseApresentacao();

        int cont = 0;

        while (true) {
            cont++;
            System.out.println("Rodada numero: " + cont);

            jogador.mostrarVida();
            inimigo.mostrarVida();

            jogador.fazAtaque(inimigo);
            if (inimigo.estaVivo()) {
                inimigo.fazAtaque(jogador);
            }

            if (!jogador.estaVivo()) {
                jogador.fraseMorte();
                System.out.println(inimigo.getNome() + " venceu!");
                break;
            }

            if (!inimigo.estaVivo()) {
                inimigo.fraseMorte();
                System.out.println(jogador.getNome() + " venceu!");
                break;
            }
        }
    }
}
