package armas;

public class MachadoBerserker extends Arma {
    public MachadoBerserker() {
        super("Machado Berserker", 140, 65, 30, 2.3);
    }

    @Override
    protected String getDescricaoEfeitoEspecial() {
        return "Alto dano bruto e bom critico";
    }
}
