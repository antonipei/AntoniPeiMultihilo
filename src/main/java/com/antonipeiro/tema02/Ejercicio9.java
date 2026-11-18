package com.antonipeiro.tema02;

// simula una carrera de cuatro cohetes y muestra el progreso en la terminal
public class Ejercicio9 {
    private static final String[] NOMBRES = {"Falcon 9", "Soyuz 2", "Ariane 5", "Miura"};
    private static final AnsiColor[] COLORES = {
            AnsiColor.CYAN, AnsiColor.YELLOW, AnsiColor.MAGENTA, AnsiColor.GREEN
    };
    private static final int ANCHO_PISTA = 55;
    private static final int COLUMNA_PISTA = 14;

    // prepara los cohetes, los lanza y muestra el resultado de la carrera
    public static void main(String[] args) throws InterruptedException {
        int distancia = args.length > 0 ? Integer.parseInt(args[0]) : 100;
        int minimo = args.length > 1 ? Integer.parseInt(args[1]) : 1;
        int maximo = args.length > 2 ? Integer.parseInt(args[2]) : 5;
        if (distancia <= 0 || minimo <= 0 || maximo < minimo || maximo == Integer.MAX_VALUE)
            throw new IllegalArgumentException("Usa distancia > 0 y 0 < avance mínimo <= avance máximo.");

        int[] posiciones = new int[4];
        long[] tiempos = new long[4];
        int[] ganador = {-1};
        HiloCohete[] cohetes = new HiloCohete[4];
        Ansi.clearScreen();
        for (int i = 0; i < 4; i++) {
            pintar(i, 0, distancia);
        }
        for (int i = 0; i < cohetes.length; i++) {
            cohetes[i] = new HiloCohete(i, distancia, minimo, maximo, posiciones, tiempos, ganador);
        }
        for (int i = 0; i < cohetes.length; i++) cohetes[i].start();
        for (int i = 0; i < cohetes.length; i++) cohetes[i].join();

        mostrarResultados(tiempos);
    }

    private static class HiloCohete extends Thread {
        private final int id;
        private final int distancia;
        private final int minimo;
        private final int maximo;
        private final int[] posiciones;
        private final long[] tiempos;
        private final int[] ganador;

        // guarda los datos que necesita cada cohete para correr
        public HiloCohete(int id, int distancia, int minimo, int maximo,
                          int[] posiciones, long[] tiempos, int[] ganador) {
            this.id = id;
            this.distancia = distancia;
            this.minimo = minimo;
            this.maximo = maximo;
            this.posiciones = posiciones;
            this.tiempos = tiempos;
            this.ganador = ganador;
        }

        // avanza el cohete hasta la meta y guarda su tiempo
        @Override
        public void run() {
            long inicio = System.nanoTime();
            while (posiciones[id] < distancia) {
                int avance = Lib.random(minimo, maximo);
                posiciones[id] = (int) Math.min(distancia, (long) posiciones[id] + avance);
                pintar(id, posiciones[id], distancia);
                if (posiciones[id] == distancia) {
                    tiempos[id] = System.nanoTime() - inicio;
                    declararGanador(ganador, id);
                    return;
                }
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    // guarda el primer cohete que llega a la meta
    private static synchronized void declararGanador(int[] ganador, int id) {
        if (ganador[0] == -1) ganador[0] = id;
    }

    // actualiza en la terminal la posicion del cohete
    private static synchronized void pintar(int id, int posicion, int distancia) {
        int fila = id + 1;
        int progreso = (int) ((long) posicion * ANCHO_PISTA / distancia);
        Ansi.printTo(String.format("%-12s", NOMBRES[id]), fila, 1, true, COLORES[id], AnsiColor.NONE);
        StringBuilder pista = new StringBuilder();
        for (int i = 0; i < ANCHO_PISTA; i++) {
            pista.append(i < progreso ? '█' : ' ');
        }
        Ansi.printTo(pista.toString(), fila, COLUMNA_PISTA, true, COLORES[id], AnsiColor.NONE);
        if (posicion >= distancia) {
            Ansi.printTo("Aterrizado!", fila, COLUMNA_PISTA + ANCHO_PISTA + 2,
                    true, AnsiColor.GRAY, AnsiColor.NONE);
        }
    }

    // ordena los tiempos y muestra el resultado final de la carrera
    private static void mostrarResultados(long[] tiempos) {
        int[] orden = {0, 1, 2, 3};
        for (int i = 0; i < orden.length - 1; i++) {
            for (int j = 0; j < orden.length - 1 - i; j++) {
                if (tiempos[orden[j]] > tiempos[orden[j + 1]]) {
                    int auxiliar = orden[j];
                    orden[j] = orden[j + 1];
                    orden[j + 1] = auxiliar;
                }
            }
        }

        Ansi.printTo("Resultado de la carrera:", 6, 1, false, AnsiColor.GRAY, AnsiColor.NONE);
        for (int i = 0; i < orden.length; i++) {
            int id = orden[i];
            String resultado = (i + 1) + ".  " + NOMBRES[id] + " ("
                    + String.format("%.4f", tiempos[id] / 1_000_000_000.0) + " s)";
            Ansi.printTo(resultado, 7 + i, 1, false, COLORES[id], AnsiColor.NONE);
        }
    }
}
