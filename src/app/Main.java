package app;

import campanha.Campanha;
import controle.ControladorBatalha;

public class Main {
    public static void main(String[] args) {
        new Campanha(new ControladorBatalha()).iniciar();
    }
}
