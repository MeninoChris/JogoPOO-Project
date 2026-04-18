package armas.curta;

import armas.base.Arma;
import armas.base.TipoArma;

public class EscudoGuardiao extends Arma {
    public EscudoGuardiao() {
        super("Escudo Guardiao", TipoArma.CURTA_DISTANCIA, 35, 95, 20, 1.6);
    }

    @Override
    protected String getDescricaoEfeitoEspecial() {
        return "Aumenta bloqueio, parry e melhora a postura defensiva";
    }

    @Override
    public int getBonusBloqueioTotal() {
        return 20;
    }

    @Override
    public int getBonusParry() {
        return 15;
    }

    @Override
    public double getMultiplicadorReducaoDefensiva() {
        return 0.2;
    }
}
