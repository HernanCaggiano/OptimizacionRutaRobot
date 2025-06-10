package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Modelo de la grilla: almacena una matriz de cargas (+1 / -1).
 */
public class Grilla {
    private final int[][] matriz;
    private final int filas;
    private final int columnas;

    public Grilla(String rutaArchivo) throws IOException {
        String[] lineas = leerLineas(rutaArchivo);
        this.filas     = lineas.length;
        this.columnas  = contarColumnas(lineas[0]);
        this.matriz    = inicializarMatriz(lineas);
    }

    private String[] leerLineas(String ruta) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            return br.lines().toArray(String[]::new);
        }
    }

    private int contarColumnas(String primeraLinea) {
        return primeraLinea.trim().split("\\s+").length;
    }

    private int[][] inicializarMatriz(String[] lineas) {
        int[][] m = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            String[] tokens = lineas[i].trim().split("\\s+");
            for (int j = 0; j < columnas; j++) {
                m[i][j] = Integer.parseInt(tokens[j]);
            }
        }
        return m;
    }

    /** Devuelve el valor (+1 ó -1) de la casilla (i,j). */
    public int getValor(int i, int j) {
        return matriz[i][j];
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    /** Devuelve la matriz completa (para la vista). */
    public int[][] getMatriz() {
        return matriz;
    }
}
