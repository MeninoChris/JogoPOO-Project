package batalha;

import combate.LogCombate;
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
        LogCombate.evento("");
        LogCombate.evento("Historico de batalhas:");
        for (int i = 0; i < this.batalhas.size(); i++) {
            LogCombate.evento((i + 1) + " - " + this.batalhas.get(i).getResumo());
        }
    }
}
