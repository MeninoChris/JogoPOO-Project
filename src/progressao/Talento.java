package progressao;

import java.util.Set;

public enum Talento {
    FEROCIDADE(
        "Ferocidade",
        "Caminho ofensivo. Aumenta dano e fortalece a habilidade especial.",
        2
    ),
    BASTIAO(
        "Bastiao",
        "Caminho defensivo. Aumenta bloqueio, parry e melhora a postura defensiva.",
        2
    ),
    ALQUIMISTA(
        "Alquimista",
        "Caminho de sustain. Aumenta cura e a protecao gerada pelos itens.",
        2
    ),
    EXECUTOR(
        "Executor",
        "Segunda camada ofensiva. Aumenta critico e dano de finalizacao.",
        4,
        FEROCIDADE
    ),
    PAREDE_DE_ACO(
        "Parede de Aco",
        "Segunda camada defensiva. Fortalece bloqueio, parry e reducao defensiva.",
        4,
        BASTIAO
    ),
    TRANSMUTADOR(
        "Transmutador",
        "Segunda camada de sustain. Aumenta muito cura e faz itens dissiparem efeitos.",
        4,
        ALQUIMISTA
    ),
    LAMINA_ASCENDENTE(
        "Lamina Ascendente",
        "Topo ofensivo. Aumenta muito dano, critico e habilidade especial.",
        6,
        EXECUTOR
    ),
    PARRY_LENDARIO(
        "Parry Lendario",
        "Topo defensivo. Eleva muito parry e transforma contra-ataques em resposta brutal.",
        6,
        PAREDE_DE_ACO
    ),
    ELIXIR_DA_FENIX(
        "Elixir da Fenix",
        "Topo de sustain. Itens curam muito mais, protegem mais e limpam efeitos.",
        6,
        TRANSMUTADOR
    );

    private final String nomeExibicao;
    private final String descricao;
    private final int nivelMinimo;
    private final Talento preRequisito;

    Talento(String nomeExibicao, String descricao, int nivelMinimo) {
        this(nomeExibicao, descricao, nivelMinimo, null);
    }

    Talento(String nomeExibicao, String descricao, int nivelMinimo, Talento preRequisito) {
        this.nomeExibicao = nomeExibicao;
        this.descricao = descricao;
        this.nivelMinimo = nivelMinimo;
        this.preRequisito = preRequisito;
    }

    public String getNomeExibicao() {
        return this.nomeExibicao;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public int getNivelMinimo() {
        return this.nivelMinimo;
    }

    public boolean estaDisponivel(int nivelJogador, Set<Talento> talentosAtuais) {
        if (talentosAtuais.contains(this)) {
            return false;
        }
        if (nivelJogador < this.nivelMinimo) {
            return false;
        }
        return this.preRequisito == null || talentosAtuais.contains(this.preRequisito);
    }
}
