package controle;

import inventario.Inventario;
import itens.Consumivel;
import java.util.List;
import java.util.Scanner;
import personagens.Inimigo;
import personagens.Jogador;

public class ControladorBatalha {
    private final Scanner scanner;

    public ControladorBatalha() {
        this.scanner = new Scanner(System.in);
    }

    public void executar(Jogador jogador, Inimigo inimigo) {
        System.out.println("#####################");
        System.out.println("A Grande Batalha");
        System.out.println("#####################");

        jogador.fraseApresentacao();
        inimigo.fraseApresentacao();

        int rodada = 0;

        while (jogador.estaVivo() && inimigo.estaVivo()) {
            rodada++;
            System.out.println("Rodada numero: " + rodada);

            jogador.mostrarVida();
            inimigo.mostrarVida();

            executarTurnoJogador(jogador, inimigo);

            if (inimigo.estaVivo()) {
                inimigo.fazAtaque(jogador);
            }

            jogador.avancarTurno();
            System.out.println();
        }

        if (!jogador.estaVivo()) {
            jogador.fraseMorte();
            System.out.println(inimigo.getNome() + " venceu!");
            return;
        }

        inimigo.fraseMorte();
        System.out.println(jogador.getNome() + " venceu!");
    }

    private void executarTurnoJogador(Jogador jogador, Inimigo inimigo) {
        boolean turnoConcluido = false;

        while (!turnoConcluido) {
            System.out.println("Escolha uma acao:");
            System.out.println("1 - Atacar");
            System.out.println("2 - Defender");
            System.out.println("3 - Curar");
            System.out.println("4 - Habilidade especial");

            int escolha = lerEscolhaValida(4);

            switch (escolha) {
                case 1:
                    executarAtaque(jogador, inimigo);
                    turnoConcluido = true;
                    break;
                case 2:
                    jogador.defender();
                    turnoConcluido = true;
                    break;
                case 3:
                    turnoConcluido = executarCura(jogador);
                    break;
                case 4:
                    turnoConcluido = executarEspecial(jogador, inimigo);
                    break;
                default:
                    break;
            }
        }
    }

    private void executarAtaque(Jogador jogador, Inimigo inimigo) {
        Inventario inventario = jogador.getInventario();
        System.out.println("Escolha sua arma:");
        inventario.mostrarArmas();

        int escolha = lerEscolhaValida(inventario.getQuantidadeArmas());
        jogador.atacarComArma(escolha - 1, inimigo);
    }

    private boolean executarCura(Jogador jogador) {
        List<Consumivel> itensDeCura = jogador.getInventario().getConsumiveisDeCura();
        if (itensDeCura.isEmpty()) {
            System.out.println("Voce nao possui itens de cura.");
            return false;
        }

        System.out.println("Escolha um item de cura:");
        mostrarConsumiveis(itensDeCura);

        int escolha = lerEscolhaValida(itensDeCura.size());
        jogador.usarConsumivel(itensDeCura.get(escolha - 1));
        return true;
    }

    private boolean executarEspecial(Jogador jogador, Inimigo inimigo) {
        System.out.println("Escolha a habilidade especial:");
        System.out.println("1 - Golpe Heroico");
        System.out.println("2 - Usar pergaminho");

        int escolha = lerEscolhaValida(2);
        if (escolha == 1) {
            if (!jogador.podeUsarHabilidadeEspecial()) {
                System.out.println("A habilidade especial ainda esta em recarga.");
                return false;
            }

            jogador.usarHabilidadeEspecial(inimigo);
            return true;
        }

        List<Consumivel> itensDeBuff = jogador.getInventario().getConsumiveisDeAprimoramento();
        if (itensDeBuff.isEmpty()) {
            System.out.println("Voce nao possui pergaminhos ou itens de aprimoramento.");
            return false;
        }

        System.out.println("Escolha um item de aprimoramento:");
        mostrarConsumiveis(itensDeBuff);

        int itemEscolhido = lerEscolhaValida(itensDeBuff.size());
        jogador.usarConsumivel(itensDeBuff.get(itemEscolhido - 1));
        return true;
    }

    private void mostrarConsumiveis(List<Consumivel> consumiveis) {
        for (int i = 0; i < consumiveis.size(); i++) {
            System.out.println((i + 1) + " - " + consumiveis.get(i).getDescricaoCompleta());
        }
    }

    private int lerEscolhaValida(int limite) {
        while (true) {
            if (!this.scanner.hasNextInt()) {
                System.out.println("Entrada invalida. Digite um numero.");
                this.scanner.next();
                continue;
            }

            int escolha = this.scanner.nextInt();
            if (escolha >= 1 && escolha <= limite) {
                return escolha;
            }

            System.out.println("Numero invalido, escolha outro:");
        }
    }
}
