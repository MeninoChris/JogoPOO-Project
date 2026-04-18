package inventario;

import armas.Arma;

public class Inventario {
    private final Arma[] armas;

    public Inventario(Arma[] armas) {
        this.armas = armas;
    }

    public int getQuantidadeArmas() {
        return this.armas.length;
    }

    public Arma getArma(int indice) {
        return this.armas[indice];
    }

    public void mostrarArmas() {
        for (int i = 0; i < this.armas.length; i++) {
            System.out.print((i + 1) + " - ");
            this.armas[i].descricao();
        }
    }
}
