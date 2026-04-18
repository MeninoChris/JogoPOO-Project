package armas.curta;

import armas.base.Arma;
import armas.base.TipoArma;

public class MachadoBerserker extends Arma {
    public MachadoBerserker() {
        super("Machado Berserker", TipoArma.CURTA_DISTANCIA, 140, 65, 30, 2.3);
    }

    @Override
    protected String getDescricaoEfeitoEspecial() {
        return "Alto dano bruto e bom critico";
    }
}
