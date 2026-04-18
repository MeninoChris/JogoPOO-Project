package armas.longa;

import armas.base.ArmaComMunicao;
import armas.base.TipoArma;

public class Pistola extends ArmaComMunicao {
    public Pistola() {
        super("Pistola Deagle", TipoArma.LONGA_DISTANCIA, 150, 50, 35, 2.5, 6);
    }

    @Override
    protected String getDescricaoEfeitoEspecial() {
        return "Disparo preciso com alto potencial critico";
    }
}
