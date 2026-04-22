package progressao;

public enum Talento {
    FEROCIDADE(
        "Ferocidade",
        "Caminho ofensivo. Aumenta dano e fortalece a habilidade especial.",
        "+18 dano e +30 dano especial.",
        2,
        1,
        RamoTalento.OFENSIVO
    ),
    BASTIAO(
        "Bastiao",
        "Caminho defensivo. Aumenta bloqueio, parry e melhora a postura defensiva.",
        "+10 bloqueio, +10 parry e +0.08 de reducao defensiva.",
        2,
        1,
        RamoTalento.DEFENSIVO
    ),
    ALQUIMISTA(
        "Alquimista",
        "Caminho de sustain. Aumenta cura e a protecao gerada pelos itens.",
        "+40 cura e +25 escudo temporario.",
        2,
        1,
        RamoTalento.SUSTENTACAO
    ),
    EXECUTOR(
        "Executor",
        "Segunda camada ofensiva. Aumenta critico e dano de finalizacao.",
        "+8% critico, +0.25 multiplicador critico e +35 dano especial.",
        3,
        2,
        RamoTalento.OFENSIVO
    ),
    PAREDE_DE_ACO(
        "Parede de Aco",
        "Segunda camada defensiva. Fortalece bloqueio, parry e reducao defensiva.",
        "+15 bloqueio, +12 parry e +0.12 de reducao defensiva.",
        3,
        2,
        RamoTalento.DEFENSIVO
    ),
    TRANSMUTADOR(
        "Transmutador",
        "Segunda camada de sustain. Aumenta muito cura e faz itens dissiparem efeitos.",
        "+70 cura, +40 escudo temporario e purificacao de efeitos negativos.",
        3,
        2,
        RamoTalento.SUSTENTACAO
    ),
    LAMINA_ASCENDENTE(
        "Lamina Ascendente",
        "Topo ofensivo. Aumenta muito dano, critico e habilidade especial.",
        "+25 dano, +10% critico e +60 dano especial.",
        4,
        3,
        RamoTalento.OFENSIVO
    ),
    PARRY_LENDARIO(
        "Parry Lendario",
        "Topo defensivo. Eleva muito parry e transforma contra-ataques em resposta brutal.",
        "+20 bloqueio, +18 parry, +80 dano de parry e +0.12 de reducao defensiva.",
        4,
        3,
        RamoTalento.DEFENSIVO
    ),
    ELIXIR_DA_FENIX(
        "Elixir da Fenix",
        "Topo de sustain. Itens curam muito mais, protegem mais e limpam efeitos.",
        "+100 cura, +60 escudo temporario, purificacao e +80 vida maxima.",
        4,
        3,
        RamoTalento.SUSTENTACAO
    );

    private final String nomeExibicao;
    private final String descricao;
    private final String resumoEfeito;
    private final int nivelMinimo;
    private final int camada;
    private final RamoTalento ramo;

    Talento(
        String nomeExibicao,
        String descricao,
        String resumoEfeito,
        int nivelMinimo,
        int camada,
        RamoTalento ramo
    ) {
        this.nomeExibicao = nomeExibicao;
        this.descricao = descricao;
        this.resumoEfeito = resumoEfeito;
        this.nivelMinimo = nivelMinimo;
        this.camada = camada;
        this.ramo = ramo;
    }

    public String getNomeExibicao() {
        return this.nomeExibicao;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public String getResumoEfeito() {
        return this.resumoEfeito;
    }

    public int getNivelMinimo() {
        return this.nivelMinimo;
    }

    public int getCamada() {
        return this.camada;
    }

    public RamoTalento getRamo() {
        return this.ramo;
    }

    public String getNomeRamo() {
        return this.ramo.getNomeExibicao();
    }

    public boolean estaDisponivel(int nivelJogador) {
        return nivelJogador >= this.nivelMinimo;
    }

    public enum RamoTalento {
        OFENSIVO("Ofensivo"),
        DEFENSIVO("Defensivo"),
        SUSTENTACAO("Sustentacao");

        private final String nomeExibicao;

        RamoTalento(String nomeExibicao) {
            this.nomeExibicao = nomeExibicao;
        }

        public String getNomeExibicao() {
            return this.nomeExibicao;
        }
    }
}
