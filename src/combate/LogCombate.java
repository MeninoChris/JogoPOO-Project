package combate;

public final class LogCombate {
    private LogCombate() {}

    public static void titulo(String texto) {
        System.out.println(texto);
    }

    public static void evento(String texto) {
        System.out.println(texto);
    }

    public static void fala(String codinome, String fala) {
        System.out.println(codinome + ": \"" + fala + "\"");
    }

    public static void narracao(String codinome, String mensagem) {
        System.out.println(codinome + " " + mensagem);
    }
}
