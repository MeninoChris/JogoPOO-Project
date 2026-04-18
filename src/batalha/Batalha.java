package batalha;

import controle.ControladorBatalha;
import personagens.Criatura;
import personagens.Inimigo;
import personagens.Jogador;

public class Batalha {
    private final Jogador jogador;
    private final Inimigo inimigo;
    private Criatura vencedor;

    public Batalha(Jogador jogador, Inimigo inimigo) {
        this.jogador = jogador;
        this.inimigo = inimigo;
    }

    public void executar(ControladorBatalha controladorBatalha) {
        System.out.println("#####################");
        System.out.println("A Grande Batalha");
        System.out.println("#####################");

        this.jogador.fraseApresentacao();
        this.inimigo.fraseApresentacao();

        int rodada = 0;

        while (this.jogador.estaVivo() && this.inimigo.estaVivo()) {
            rodada++;
            System.out.println("Rodada numero: " + rodada);

            this.jogador.mostrarVida();
            this.inimigo.mostrarVida();

            controladorBatalha.executarTurnoJogador(this.jogador, this.inimigo);

            if (this.inimigo.estaVivo()) {
                this.inimigo.fazAtaque(this.jogador);
            }

            this.jogador.avancarTurno();
            System.out.println();
        }

        definirVencedor();
        exibirResultado();
    }

    public Criatura getVencedor() {
        return this.vencedor;
    }

    private void definirVencedor() {
        if (this.jogador.estaVivo()) {
            this.vencedor = this.jogador;
            return;
        }

        this.vencedor = this.inimigo;
    }

    private void exibirResultado() {
        if (this.vencedor == this.jogador) {
            this.inimigo.fraseMorte();
            System.out.println(this.jogador.getNome() + " venceu!");
            return;
        }

        this.jogador.fraseMorte();
        System.out.println(this.inimigo.getNome() + " venceu!");
    }
}
