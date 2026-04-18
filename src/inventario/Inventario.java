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
            LogCombate.evento((i + 1) + " - " + this.armas.get(i).getDescricaoCombate());
        }
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

    public List<Consumivel> getConsumiveis() {
        return new ArrayList<>(this.consumiveis);
    }

    public void prepararParaNovaBatalha() {
        for (Arma arma : this.armas) {
            arma.prepararParaNovaBatalha();
        }
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
