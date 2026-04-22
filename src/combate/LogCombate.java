package combate;

public final class LogCombate {
    private static final String SEPARADOR = "--------------------------------------------------";

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

    public static void espaco() {
        System.out.println();
    }

    public static void separador() {
        System.out.println(SEPARADOR);
    }

    public static void secao(String titulo) {
        espaco();
        separador();
        System.out.println(titulo);
        separador();
    }

    public static void subtitulo(String titulo) {
        espaco();
        System.out.println("[" + titulo + "]");
    }
}
