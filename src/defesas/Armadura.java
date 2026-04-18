package defesas;

public class Armadura extends Defesa {
    private final int porcentagemReducao;

    public Armadura(int porcentagemReducao) {
        this.porcentagemReducao = porcentagemReducao;
    }

    @Override
    public int danoReduzido(int danoOriginal) {
        int danoReduzido = danoOriginal - (danoOriginal * this.porcentagemReducao / 100);
        System.out.println("A armadura reduziu parte do dano.");
        return danoReduzido;
    }
}
