package batalha;

import combate.LogCombate;
import controle.ControladorBatalha;
import personagens.Criatura;
import personagens.Inimigo;
import personagens.Jogador;

public class Batalha {
    private final Jogador jogador;
    private final Inimigo inimigo;
    private Criatura vencedor;
    private int rodadas;
    private int experienciaRecebida;
    private int niveisGanhos;
    private int nivelInicialJogador;
    private int nivelFinalJogador;

    public Batalha(Jogador jogador, Inimigo inimigo) {
        this.jogador = jogador;
        this.inimigo = inimigo;
    }

    public void executar(ControladorBatalha controladorBatalha) {
        this.nivelInicialJogador = this.jogador.getNivel();
        LogCombate.titulo("#####################");
        LogCombate.titulo("A Grande Batalha");
        LogCombate.titulo("#####################");
        LogCombate.evento("Desafiante inimigo: " + this.inimigo.getFichaCombate());
        LogCombate.evento("Status do jogador: " + this.jogador.getResumoProgressao());

        this.jogador.fraseApresentacao();
        this.inimigo.fraseApresentacao();

        while (this.jogador.estaVivo() && this.inimigo.estaVivo()) {
            this.rodadas++;
            LogCombate.evento("Rodada numero: " + this.rodadas);

            this.jogador.processarInicioTurno();
            if (!this.jogador.estaVivo()) {
                break;
            }

            this.jogador.mostrarVida();
            this.inimigo.mostrarVida();

            controladorBatalha.executarTurnoJogador(this.jogador, this.inimigo);
            this.jogador.processarFimTurno();

            if (this.inimigo.estaVivo()) {
                this.inimigo.processarInicioTurno();
                if (!this.inimigo.estaVivo()) {
                    break;
                }
                this.inimigo.fazAtaque(this.jogador);
                this.inimigo.processarFimTurno();
            }

            this.jogador.avancarTurno();
            LogCombate.evento("");
        }

        definirVencedor();
        aplicarRecompensas();
        exibirResultado();
    }

    public Criatura getVencedor() {
        return this.vencedor;
    }

    public String getResumo() {
        String nomeVencedor = this.vencedor == null ? "Sem vencedor" : this.vencedor.getNome();
        return "Batalha contra "
            + this.inimigo.getNome()
            + " - vencedor: "
            + nomeVencedor
            + " - rodadas: "
            + this.rodadas
            + " - XP: "
            + this.experienciaRecebida
            + " - nivel: "
            + this.nivelInicialJogador
            + " -> "
            + this.nivelFinalJogador;
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
            LogCombate.evento(this.jogador.getNome() + " venceu!");
            LogCombate.evento(
                "Recompensa: "
                    + this.experienciaRecebida
                    + " XP"
                    + (this.niveisGanhos > 0 ? " | Niveis ganhos: " + this.niveisGanhos : "")
            );
            return;
        }

        this.jogador.fraseMorte();
        LogCombate.evento(this.inimigo.getNome() + " venceu!");
    }

    private void aplicarRecompensas() {
        this.nivelFinalJogador = this.jogador.getNivel();
        if (this.vencedor != this.jogador) {
            this.experienciaRecebida = 0;
            return;
        }

        this.experienciaRecebida = this.inimigo.getExperienciaConcedida();
        this.niveisGanhos = this.jogador.ganharExperiencia(this.experienciaRecebida);
        this.nivelFinalJogador = this.jogador.getNivel();
    }
}
