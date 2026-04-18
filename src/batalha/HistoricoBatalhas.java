package batalha;

import java.util.ArrayList;
import java.util.List;

public class HistoricoBatalhas {
    private final List<Batalha> batalhas;

    public HistoricoBatalhas() {
        this.batalhas = new ArrayList<>();
    }

    public void registrar(Batalha batalha) {
        this.batalhas.add(batalha);
    }

    public int getQuantidadeBatalhas() {
        return this.batalhas.size();
    }

    public void exibirResumo() {
        System.out.println();
        System.out.println("Historico de batalhas:");
        for (int i = 0; i < this.batalhas.size(); i++) {
            System.out.println((i + 1) + " - " + this.batalhas.get(i).getResumo());
        }
    }
}
