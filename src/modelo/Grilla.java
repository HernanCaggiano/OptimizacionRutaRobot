package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grilla {
	private int[][] matriz;
    private int filas;
    private int columnas;

    public Grilla(String path) throws IOException {
        cargarDesdeArchivo(path);
    }

    private void cargarDesdeArchivo(String path) throws IOException {
        List<int[]> temp = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] tokens = linea.split("\\s+");
                int[] fila = Arrays.stream(tokens).mapToInt(Integer::parseInt).toArray();
                temp.add(fila);
            }
        }
        filas = temp.size();
        columnas = temp.get(0).length;
        matriz = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            matriz[i] = temp.get(i);
        }
    }

    public int getValor(int i, int j) {
        return matriz[i][j];
    }

    public int[][] getMatriz() {
        return matriz;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }
}