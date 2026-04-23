package defesas;

import combate.LogCombate;

public class Defesa {
    public int danoReduzido(int danoOriginal) {
        return danoOriginal;
    }

    public String getNomeExibicao() {
        return "Defesa Simples";
    }

    public String getDescricao() {
        return getNomeExibicao() + " (sem efeito adicional)";
    }

    protected void narrar(String mensagem) {
        LogCombate.evento(mensagem);
    }
}
