package inventario;

import armas.Arma;
import itens.Consumivel;
import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private final List<Arma> armas;
    private final List<Consumivel> consumiveis;

    public Inventario(Arma[] armas, Consumivel[] consumiveis) {
        this.armas = new ArrayList<>();
        this.consumiveis = new ArrayList<>();

        for (Arma arma : armas) {
            this.armas.add(arma);
        }

        for (Consumivel consumivel : consumiveis) {
            this.consumiveis.add(consumivel);
        }
    }

    public int getQuantidadeArmas() {
        return this.armas.size();
    }

    public Arma getArma(int indice) {
        return this.armas.get(indice);
    }

    public void mostrarArmas() {
        for (int i = 0; i < this.armas.size(); i++) {
            System.out.print((i + 1) + " - ");
            this.armas.get(i).descricao();
        }
    }

    public boolean temConsumiveis() {
        return !this.consumiveis.isEmpty();
    }

    public int getQuantidadeConsumiveis() {
        return this.consumiveis.size();
    }

    public Consumivel removerConsumivel(int indice) {
        return this.consumiveis.remove(indice);
    }

    public void mostrarConsumiveis() {
        for (int i = 0; i < this.consumiveis.size(); i++) {
            System.out.println((i + 1) + " - " + this.consumiveis.get(i).getDescricaoCompleta());
        }
    }
}
