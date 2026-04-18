package armas.curta;

import armas.base.Arma;
import armas.base.TipoArma;

public class Faca extends Arma {
    public Faca() {
        super("Faca", TipoArma.CURTA_DISTANCIA, 40, 100, 10, 1.5);
    }

    @Override
    protected String getDescricaoEfeitoEspecial() {
        return "Simples e precisa";
    }
}
