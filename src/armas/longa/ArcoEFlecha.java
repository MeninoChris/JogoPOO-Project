package armas.longa;

import armas.base.ArmaComMunicao;
import armas.base.TipoArma;

public class ArcoEFlecha extends ArmaComMunicao {
    public ArcoEFlecha() {
        super("Arco e Flecha", TipoArma.LONGA_DISTANCIA, 150, 40, 25, 2.2, 8);
    }

    @Override
    protected String getDescricaoEfeitoEspecial() {
        return "Alcance alto e chance boa de critico";
    }
}
