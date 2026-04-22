package progressao;

public enum Talento {
    FEROCIDADE(
        "Ferocidade",
        "Caminho ofensivo. Aumenta dano e fortalece a habilidade especial.",
        2,
        1
    ),
    BASTIAO(
        "Bastiao",
        "Caminho defensivo. Aumenta bloqueio, parry e melhora a postura defensiva.",
        2,
        1
    ),
    ALQUIMISTA(
        "Alquimista",
        "Caminho de sustain. Aumenta cura e a protecao gerada pelos itens.",
        2,
        1
    ),
    EXECUTOR(
        "Executor",
        "Segunda camada ofensiva. Aumenta critico e dano de finalizacao.",
        3,
        2
    ),
    PAREDE_DE_ACO(
        "Parede de Aco",
        "Segunda camada defensiva. Fortalece bloqueio, parry e reducao defensiva.",
        3,
        2
    ),
    TRANSMUTADOR(
        "Transmutador",
        "Segunda camada de sustain. Aumenta muito cura e faz itens dissiparem efeitos.",
        3,
        2
    ),
    LAMINA_ASCENDENTE(
        "Lamina Ascendente",
        "Topo ofensivo. Aumenta muito dano, critico e habilidade especial.",
        4,
        3
    ),
    PARRY_LENDARIO(
        "Parry Lendario",
        "Topo defensivo. Eleva muito parry e transforma contra-ataques em resposta brutal.",
        4,
        3
    ),
    ELIXIR_DA_FENIX(
        "Elixir da Fenix",
        "Topo de sustain. Itens curam muito mais, protegem mais e limpam efeitos.",
        4,
        3
    );

    private final String nomeExibicao;
    private final String descricao;
    private final int nivelMinimo;
    private final int camada;

    Talento(String nomeExibicao, String descricao, int nivelMinimo, int camada) {
        this.nomeExibicao = nomeExibicao;
        this.descricao = descricao;
        this.nivelMinimo = nivelMinimo;
        this.camada = camada;
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

    public int getCamada() {
        return this.camada;
    }

    public boolean estaDisponivel(int nivelJogador) {
        return nivelJogador >= this.nivelMinimo;
    }
}
