package personagens;

import defesas.BarreiraMagica;
import java.util.Random;

public class Aetherion extends Inimigo {
    private static final Random RD = new Random();

    private int cargasEstelares;
    private boolean drenouNucleo;

    public Aetherion() {
        super("Aetherion", 980, 105, new BarreiraMagica(115));
    }

    @Override
    public void fazAtaque(Criatura alvo) {
        this.cargasEstelares++;
        ativarFaseDoisSeNecessario(45, "entrou em Fase 2. Fendas celestiais rasgaram a arena e o vazio respondeu ao chamado.");

        if (!this.drenouNucleo && getVidaAtual() <= 360) {
            atacarComDanoNarrado(alvo, "Dreno Astral", 90);
            curarComNarracao(65, "Dreno Astral");
            this.drenouNucleo = true;
            return;
        }

        if ((estaNaFaseDois() && this.cargasEstelares >= 2) || this.cargasEstelares >= 3) {
            atacarComDanoNarrado(alvo, "Supernova do Vazio", estaNaFaseDois() ? 170 : 145);
            alvo.aplicarDanoContinuo("Queimadura Cosmica", estaNaFaseDois() ? 36 : 28, 2);
            this.cargasEstelares = 0;
            return;
        }

        int sorteio = RD.nextInt(100);

        if (sorteio < (estaNaFaseDois() ? 25 : 18)) {
            narrar("usou Dobra Celeste e converteu seu turno ofensivo em mais energia estelar.");
            this.cargasEstelares++;
            return;
        }

        if (sorteio < (estaNaFaseDois() ? 60 : 45)) {
            atacarComDanoNarrado(alvo, "Prisma Gravitacional", 75);
            alvo.aplicarReducaoDano("Pressao Gravitacional", estaNaFaseDois() ? 35 : 25, 2);
            return;
        }

        int dano = 80 + RD.nextInt(51);
        if (estaNaFaseDois()) {
            dano += 25;
        }
        atacarComDanoNarrado(alvo, "Lanca de Cometa", dano);
    }

    @Override
    public void fraseApresentacao() {
        falar("Eu sou Aetherion. O firmamento rachado caira sobre voce.");
    }

    @Override
    public void fraseMorte() {
        falar("Nem mesmo as estrelas... permanecem...");
    }

    @Override
    public String getTitulo() {
        return "Arconte do Cosmo Partido";
    }

    @Override
    public String getPerfilCombate() {
        return "Mestre astral que acumula cargas para supernovas, enfraquece o alvo e escala na fase 2";
    }

    @Override
    public int getExperienciaConcedida() {
        return 380;
    }

    @Override
    protected String getNomeAtaqueBasico() {
        return "Raio Sideral";
    }
}
