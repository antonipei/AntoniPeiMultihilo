package com.antonipeiro.tema02;

public class Ejercicio8 {

    // crea un array de ejemplo y muestra cuantos primos hay
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

        // coge el numero de procesadores disponibles
        int numeroHilos = Runtime.getRuntime().availableProcessors();
        // no crea mas hilos que elementos tiene el array
        if (numeroHilos > numeros.length) {
            numeroHilos = numeros.length;
        }

        // guarda los hilos que van a contar los numeros primos
        ContadorPrimos[] hilos = new ContadorPrimos[numeroHilos];

        // divide el array y arranca un hilo para cada parte
        for (int i = 0; i < numeroHilos; i++) {
            // calcula el inicio y el final de la parte que revisa este hilo
            int inicio = i * numeros.length / numeroHilos;
            int fin = (i + 1) * numeros.length / numeroHilos;
            hilos[i] = new ContadorPrimos(numeros, inicio, fin);
            hilos[i].start();
        }

        // suma los resultados de los hilos cuando hayan terminado
        int cantidadPrimos = 0;
        for (int i = 0; i < numeroHilos; i++) {
            // espera a que termine este hilo
            hilos[i].join();
            cantidadPrimos += hilos[i].getCantidadPrimos();
        }

        // devuelve la cantidad total de primos
        return cantidadPrimos;
    }

    private static class ContadorPrimos extends Thread {
        private final int[] numeros;
        private final int inicio;
        private final int fin;
        private int cantidadPrimos;

        // guarda el array y el tramo que tiene que contar este hilo
        public ContadorPrimos(int[] numeros, int inicio, int fin) {
            this.numeros = numeros;
            this.inicio = inicio;
            this.fin = fin;
            cantidadPrimos = 0;
        }

        // cuenta los primos del tramo asignado
        @Override
        public void run() {
            for (int i = inicio; i < fin; i++) {
                if (Lib.esPrimo(numeros[i])) {
                    cantidadPrimos++;
                }
            }
        }

        // devuelve cuantos primos conto este hilo
        public int getCantidadPrimos() {
            return cantidadPrimos;
        }
    }
}
