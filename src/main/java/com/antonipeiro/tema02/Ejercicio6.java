package com.antonipeiro.tema02;

public class Ejercicio6 {
    public static void main(String[] args) {
        int n = Runtime.getRuntime().availableProcessors();

        for (int i = 0; i < n; i++) {
            Thread hilo = new Thread(new HiloNumeros(i));
            hilo.start();
        }
    }

    private static class HiloNumeros implements Runnable {
        private final int i;

        private HiloNumeros(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            for (int numero = 0; numero < 10000; numero++) {
                System.out.println("Hilo: " + i + " (" + numero + ")");
            }
        }
    }
}