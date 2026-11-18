package com.antonipeiro.tema02;

public class Ejercicio3 {
    public static void main(String[] args) {
        Thread hilo = new Thread(() -> {
            for (int numero = 1; numero <= 100; numero++) {
                System.out.println(numero);
            }
        });

        hilo.setDaemon(true);
        hilo.start();
        System.out.println("fin de programa");
    }
}
