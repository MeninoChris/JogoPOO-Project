package progressao;

public enum AtributoEvolutivo {
    VIGOR("Vigor", "Aumenta bastante a vida maxima e restaura totalmente ao investir"),
    FORCA("Forca", "Aumenta dano geral e fortalece a habilidade especial"),
    PRECISAO("Precisao", "Aumenta chance critica e multiplicador critico"),
    GUARDA("Guarda", "Aumenta bloqueio, parry e a forca do escudo temporario");

    private final String nomeExibicao;
    private final String descricao;

    AtributoEvolutivo(String nomeExibicao, String descricao) {
        this.nomeExibicao = nomeExibicao;
        this.descricao = descricao;
    }

    public String getNomeExibicao() {
        return this.nomeExibicao;
    }

    public String getDescricao() {
        return this.descricao;
    }
}
