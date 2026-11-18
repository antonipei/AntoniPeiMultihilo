package com.antonipeiro.tema02;

public class Ejercicio8 {

    public static void main(String[] args) throws InterruptedException {
        int[] numeros = {2, 3, 4, 5, 9, 11, 17, 20, 23, 29, 1};
        int cantidad = contarPrimos(numeros);

        System.out.println("cantidad de números primos: " + cantidad);
    }

    // reparte el array entre hilos y suma los primos encontrados
    public static int contarPrimos(int[] numeros) throws InterruptedException {
        
        //comprobar
        if (numeros == null) {
            throw new IllegalArgumentException("El array no puede ser null");
        }
        if (numeros.length == 0) {
            return 0;
        }

        int n = Runtime.getRuntime().availableProcessors();
        if (n > numeros.length) {
            n = numeros.length;
        }

        ContadorPrimos[] hilos = new ContadorPrimos[n];

        for (int i = 0; i < n; i++) {
            // calcula el inicio y el final de la parte que revisa este hilo
            int inicio = i * numeros.length / n;
            int fin = (i + 1) * numeros.length / n;
            hilos[i] = new ContadorPrimos(numeros, inicio, fin);
            hilos[i].start();
        }

        // suma los resultados de los hilos cuando hayan terminado
        int cantidadPrimos = 0;

        for (int i = 0; i < n; i++) {
            hilos[i].join();
            cantidadPrimos += hilos[i].getCantidadPrimos();
        }

        return cantidadPrimos;
    }

    private static class ContadorPrimos extends Thread {
        private final int[] numeros;
        private final int inicio;
        private final int fin;
        private int cantidadPrimos;

        public ContadorPrimos(int[] numeros, int inicio, int fin) {
            this.numeros = numeros;
            this.inicio = inicio;
            this.fin = fin;
            cantidadPrimos = 0;
        }

        @Override
        public void run() {
            for (int i = inicio; i < fin; i++) {
                if (Lib.esPrimo(numeros[i])) {
                    cantidadPrimos++;
                }
            }
        }

        public int getCantidadPrimos() {
            return cantidadPrimos;
        }
    }
}
