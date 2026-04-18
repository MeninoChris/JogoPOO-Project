package armas;

public class Arco_e_Flecha extends ArmaComMunicao {
    public Arco_e_Flecha() {
        super("Arco e Flecha", TipoArma.LONGA_DISTANCIA, 150, 40, 25, 2.2, 8);
    }

    @Override
    protected String getDescricaoEfeitoEspecial() {
        return "Alcance alto e chance boa de critico";
    }
}
