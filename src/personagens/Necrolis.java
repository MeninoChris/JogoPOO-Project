package personagens;

import defesas.DefesaHibrida;
import java.util.Random;

public class Necrolis extends Inimigo {
    private static final Random RD = new Random();
    private boolean drenouEssencia;

    public Necrolis() {
        super("Necrolis", 650, 80, new DefesaHibrida(20, 30));
    }

    @Override
    public void fazAtaque(Criatura alvo) {
        ativarFaseDoisSeNecessario(50, "entrou em Fase 2. As sombras se condensaram em uma forma ainda mais letal.");

        if (!this.drenouEssencia && getVidaAtual() <= 220) {
            atacarComDanoNarrado(alvo, "Drenar Essencia", 65);
            curarComNarracao(50, "Drenar Essencia");
            this.drenouEssencia = true;
            return;
        }

        if (estaNaFaseDois() && alvo.getVidaAtual() <= 180) {
            atacarComDanoNarrado(alvo, "Sentenca do Eclipse", 120);
            return;
        }

        int sorteio = RD.nextInt(100);

        if (sorteio < (estaNaFaseDois() ? 20 : 25)) {
            narrar("usou Passo Sombrio e desapareceu nas sombras. Nenhum dano sera causado.");
            return;
        }

        if (sorteio < (estaNaFaseDois() ? 60 : 50)) {
            atacarComDanoNarrado(alvo, "Maldicao do Vazio", 40);
            alvo.aplicarReducaoDano("Maldicao do Vazio", estaNaFaseDois() ? 35 : 25, 2);
            return;
        }

        int dano = 60 + RD.nextInt(81);
        if (estaNaFaseDois()) {
            dano += 20;
        }
        atacarComDanoNarrado(alvo, "Rajada Sombria", dano);
    }

    @Override
    public void fraseApresentacao() {
        falar("Eu sou Necrolis, o vulto que precede o fim.");
    }

    @Override
    public void fraseMorte() {
        falar("As sombras... estao me deixando...");
    }

    @Override
    public String getTitulo() {
        return "Soberano das Sombras";
    }

    @Override
    public String getPerfilCombate() {
        return "Controlador sombrio que enfraquece o jogador, drena essencia e pune alvos vulneraveis";
    }

    @Override
    public int getExperienciaConcedida() {
        return 260;
    }
}
