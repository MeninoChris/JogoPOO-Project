package defesas;

public class BarreiraMagica extends Defesa {
    private final int limiteDano;

    public BarreiraMagica(int limiteDano) {
        this.limiteDano = limiteDano;
    }

    @Override
    public int danoReduzido(int danoOriginal) {
        if (danoOriginal > this.limiteDano) {
            narrar("A barreira magica conteve parte do impacto.");
            return this.limiteDano;
        }
        return danoOriginal;
    }

    @Override
    public String getNomeExibicao() {
        return "Barreira Magica";
    }

    @Override
    public String getDescricao() {
        return getNomeExibicao() + " (limita cada golpe a " + this.limiteDano + " de dano)";
    }
}
