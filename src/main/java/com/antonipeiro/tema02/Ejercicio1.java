package com.antonipeiro.tema02;

public class Ejercicio1 {
    public static void main(String[] args) {
        new Thread(() -> {
            for (int numero = 1; numero <= 100; numero++) {
                System.out.println(numero);
            }
        }).start();

        System.out.println("fin de programa");
    }
}
