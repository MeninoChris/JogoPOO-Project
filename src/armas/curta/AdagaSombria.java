package armas.curta;

import armas.base.Arma;
import armas.base.TipoArma;

public class AdagaSombria extends Arma {
    public AdagaSombria() {
        super("Adaga Sombria", TipoArma.CURTA_DISTANCIA, 55, 95, 45, 2.8);
    }

    @Override
    protected String getDescricaoEfeitoEspecial() {
        return "Alta chance de critico";
    }
}
