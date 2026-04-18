package defesas;

import combate.LogCombate;

public class Defesa {
    public int danoReduzido(int danoOriginal) {
        return danoOriginal;
    }

    protected void narrar(String mensagem) {
        LogCombate.evento(mensagem);
    }
}
