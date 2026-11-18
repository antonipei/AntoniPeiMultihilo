package com.antonipeiro.tema02;

public class Ejercicio4 {
    public static void main(String[] args) throws InterruptedException {
        Thread hilo = new Thread(() -> {
            for (int numero = 1; numero <= 100; numero++) {
                System.out.println(numero);
            }
        });

        hilo.setDaemon(true);
        hilo.start();
        hilo.join();
        System.out.println("fin de programa");
    }
}
