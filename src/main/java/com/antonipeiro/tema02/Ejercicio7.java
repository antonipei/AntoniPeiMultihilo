package com.antonipeiro.tema02;

public class Ejercicio7 {

    // prepara el array y muestra si esta el numero buscado
    public static void main(String[] args) throws InterruptedException {
        int[] numeros = {4, 9, 12, 17, 23, 31, 42, 58};
        int numeroBuscado = 23;

        if (estaEnArray(numeros, numeroBuscado)) {
            System.out.println("El número " + numeroBuscado + " está en el array.");
        } else {
            System.out.println("El número " + numeroBuscado + " no está en el array.");
        }
    }

    // reparte el array entre varios hilos y devuelve si aparece el numero
    public static boolean estaEnArray(int[] numeros, int numeroBuscado) throws InterruptedException {
        
        //comprobar
        if (numeros == null) {
            throw new IllegalArgumentException("El array no puede ser null");
        }
        if (numeros.length == 0) {
            return false;
        }

        // coge el numero de procesadores disponibles
        int n = Runtime.getRuntime().availableProcessors();
        // no crea mas hilos que elementos tiene el array
        if (n > numeros.length) {
            n = numeros.length;
        }

        Buscador[] hilos = new Buscador[n];

        for (int i = 0; i < n; i++) {

            // calcula el inicio y el final de la parte que revisa este hilo
            int inicio = i * numeros.length / n;
            int fin = (i + 1) * numeros.length / n;
            hilos[i] = new Buscador(numeros, numeroBuscado, inicio, fin);
            hilos[i].start();
        }

        for (int i = 0; i < n; i++) {
            hilos[i].join();
        }

        // mira si alguno de los hilos encontro el numero
        for (int i = 0; i < n; i++) {
            if (hilos[i].isEncontrado()) {
                return true;
            }
        }
        return false;
    }

    private static class Buscador extends Thread {
        private final int[] numeros;
        private final int numeroBuscado;
        private final int inicio;
        private final int fin;
        private boolean encontrado;

        public Buscador(int[] numeros, int numeroBuscado, int inicio, int fin) {
            this.numeros = numeros;
            this.numeroBuscado = numeroBuscado;
            this.inicio = inicio;
            this.fin = fin;
            encontrado = false;
        }

        @Override
        public void run() {
            for (int i = inicio; i < fin; i++) {
                if (numeros[i] == numeroBuscado) {
                    encontrado = true;
                }
            }
        }
        public boolean isEncontrado() {
            return encontrado;
        }
    }
}
