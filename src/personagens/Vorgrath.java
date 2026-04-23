package personagens;

import defesas.Armadura;
import java.util.Random;

public class Vorgrath extends Inimigo {
    private static final Random RD = new Random();

    private int contadorTurnos;
    private int recargaCataclismo;
    private boolean absorveuEscoria;

    public Vorgrath() {
        super("Vorgrath", 1200, 90, new Armadura(30));
    }

    @Override
    public void fazAtaque(Criatura alvo) {
        this.contadorTurnos++;
        ativarFaseDoisSeNecessario(40, "entrou em Fase 2. A carcaca de pedra rachou e o magma tomou seus punhos.");

        if (this.recargaCataclismo > 0) {
            this.recargaCataclismo--;
        }

        if (!this.absorveuEscoria && getVidaAtual() <= 320) {
            atacarComDanoNarrado(alvo, "Impacto de Escoria", 85);
            curarComNarracao(75, "Impacto de Escoria");
            this.absorveuEscoria = true;
            return;
        }

        if (estaNaFaseDois() && this.recargaCataclismo == 0 && alvo.getVidaAtual() > 260) {
            atacarComDanoNarrado(alvo, "Cataclismo Tectonico", 150);
            alvo.aplicarReducaoDano("Abalo Tectonico", 30, 2);
            this.recargaCataclismo = 2;
            return;
        }

        if (this.contadorTurnos % 3 == 0) {
            atacarComDanoNarrado(alvo, "Muralha de Escombros", 95);
            alvo.aplicarDanoContinuo("Estilhacos de Rocha", 22, 2);
            return;
        }

        if (RD.nextInt(100) < (estaNaFaseDois() ? 55 : 35)) {
            atacarComDanoNarrado(alvo, "Martelo Vulcanico", estaNaFaseDois() ? 120 : 100);
            return;
        }

        super.fazAtaque(alvo);
    }

    @Override
    public void fraseApresentacao() {
        falar("Eu sou Vorgrath. Cada passo meu transforma coragem em ruina.");
    }

    @Override
    public void fraseMorte() {
        falar("A montanha... tombou...");
    }

    @Override
    public String getTitulo() {
        return "Colosso da Ruina";
    }

    @Override
    public String getPerfilCombate() {
        return "Tanque tectonico que pressiona com escombros, debuffs de dano e explosoes de magma";
    }

    @Override
    public int getExperienciaConcedida() {
        return 320;
    }

    @Override
    protected String getNomeAtaqueBasico() {
        return "Punho Basaltico";
    }
}
