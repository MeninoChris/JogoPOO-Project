package inventario;

import armas.base.Arma;
import combate.LogCombate;
import itens.Consumivel;
import itens.TipoConsumivel;
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

    public boolean isArmaPrincipal(Arma arma) {
        return !this.armas.isEmpty() && this.armas.get(0) == arma;
    }

    public void adicionarArmaSeAusente(Arma arma) {
        for (Arma armaExistente : this.armas) {
            if (armaExistente.getClass().equals(arma.getClass())) {
                return;
            }
        }
        this.armas.add(arma);
    }

    public void mostrarArmas() {
        for (int i = 0; i < this.armas.size(); i++) {
            LogCombate.evento(getDescricaoArmaOrdenada(i));
        }
    }

    public String getDescricaoArmaOrdenada(int indice) {
        return (indice + 1) + " - [" + getRotuloArma(indice) + "] " + this.armas.get(indice).getDescricaoCombate();
    }

    public String getRotuloArma(int indice) {
        if (indice == 0) {
            return "Principal";
        }
        if (indice == 1) {
            return "Secundaria";
        }
        return "Reserva " + (indice - 1);
    }

    public List<Consumivel> getConsumiveisDeCura() {
        return filtrarConsumiveis(TipoConsumivel.CURA);
    }

    public List<Consumivel> getConsumiveisDeAprimoramento() {
        return filtrarConsumiveis(TipoConsumivel.APRIMORAMENTO);
    }

    public void removerConsumivel(Consumivel consumivel) {
        this.consumiveis.remove(consumivel);
    }

    public void adicionarConsumivel(Consumivel consumivel) {
        this.consumiveis.add(consumivel);
    }

    public List<Consumivel> getConsumiveis() {
        return new ArrayList<>(this.consumiveis);
    }

    public void definirArmaPrincipal(int indice) {
        if (indice <= 0 || indice >= this.armas.size()) {
            return;
        }

        Arma armaPrincipal = this.armas.remove(indice);
        this.armas.add(0, armaPrincipal);
    }

    private List<Consumivel> filtrarConsumiveis(TipoConsumivel tipoConsumivel) {
        List<Consumivel> filtrados = new ArrayList<>();
        for (Consumivel consumivel : this.consumiveis) {
            if (consumivel.getTipoConsumivel() == tipoConsumivel) {
                filtrados.add(consumivel);
            }
        }
        return filtrados;
    }
}
