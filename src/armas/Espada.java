package armas;

public class Espada extends Arma {
    public Espada() {
        super("Espada", TipoArma.CURTA_DISTANCIA, 70, 90, 15, 1.8);
    }

    @Override
    protected String getDescricaoEfeitoEspecial() {
        return "Equilibrada para combate direto";
    }
}
