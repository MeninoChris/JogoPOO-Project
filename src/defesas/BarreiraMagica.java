package defesas;

public class BarreiraMagica extends Defesa {
    private final int limiteDano;

    public BarreiraMagica(int limiteDano) {
        this.limiteDano = limiteDano;
    }

    @Override
    public int danoReduzido(int danoOriginal) {
        if (danoOriginal > this.limiteDano) {
            System.out.println("A barreira magica conteve parte do impacto.");
            return this.limiteDano;
        }
        return danoOriginal;
    }
}
