package com.antonipeiro.tema02;

import java.io.PrintStream;

/** Utilidades ANSI proporcionadas para dibujar texto coloreado en terminal. */
public class Ansi {
    public enum ColorType { FOREGROUND, BACKGROUND }

    public static final String ESC = "\u001b[";
    public static final String RESET = ESC + "0m";

    public static AnsiColor randomColor() {
        return AnsiColor.values()[Lib.random(0, AnsiColor.values().length - 1)];
    }

    public static void println(String s, boolean bold, AnsiColor fg, AnsiColor bg) {
        println(System.out, s, bold, fg, bg);
    }

    public static void println(PrintStream out, String s, boolean bold, AnsiColor fg, AnsiColor bg) {
        print(out, s, bold, fg, bg);
        out.println();
    }

    public static void print(PrintStream out, String s, boolean bold, AnsiColor fg, AnsiColor bg) {
        for (char c : s.toCharArray()) print(out, c, bold, fg, bg);
    }

    public static void print(String s, boolean bold, AnsiColor fg, AnsiColor bg) {
        print(System.out, s, bold, fg, bg);
    }

    public static void print(PrintStream out, char c, boolean bold, AnsiColor fg, AnsiColor bg) {
        printTo(out, c, -1, -1, bold, fg, bg);
    }

    public static void print(char c, boolean bold, AnsiColor fg, AnsiColor bg) {
        printTo(System.out, c, -1, -1, bold, fg, bg);
    }

    public static void printTo(String s, int row, int col, boolean bold, AnsiColor fg, AnsiColor bg) {
        for (char c : s.toCharArray()) printTo(c, row, col++, bold, fg, bg);
    }

    public static void printTo(char c, int row, int col, boolean bold, AnsiColor fg, AnsiColor bg) {
        printTo(System.out, c, row, col, bold, fg, bg);
    }

    public static void printTo(PrintStream out, char c, int row, int col,
                               boolean bold, AnsiColor fg, AnsiColor bg) {
        String fgColor = getColor(fg, ColorType.FOREGROUND);
        String bgColor = getColor(bg, ColorType.BACKGROUND);
        String color = "";
        if (!fgColor.isEmpty() || !bgColor.isEmpty()) {
            color = ESC + (bold ? "1" : "0");
            if (!fgColor.isEmpty()) color += ";" + fgColor;
            if (!bgColor.isEmpty()) color += ";" + bgColor;
            color += "m";
        }
        if (row >= 0 && col >= 0) out.print(ESC + row + ";" + col + "f");
        out.print(color + c);
        if (!color.isEmpty()) out.print(RESET);
        out.flush();
    }

    public static void setCursorTo(int row, int col) {
        System.out.print(ESC + row + ";" + col + "f");
    }

    public static String getColor(AnsiColor color, ColorType type) {
        if (color == AnsiColor.NONE) return "";
        return (type == ColorType.FOREGROUND ? "3" : "4") + color.ordinal();
    }

    public static void clearScreen() {
        System.out.print(ESC + "H" + ESC + "2J");
        System.out.flush();
    }

    public static void hideCursor() {
        System.out.print(ESC + "?25l");
        System.out.flush();
    }

    public static void showCursor() {
        System.out.print(ESC + "?25h");
        System.out.flush();
    }
}
