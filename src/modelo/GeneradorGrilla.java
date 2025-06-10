package modelo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Genera instancias aleatorias de grillas (+1 / -1) y las guarda en archivo.
 */
public class GeneradorGrilla {
	private static final Random ALEATORIO = new Random();

	/**
	 * Genera una matriz aleatoria de tamaño filas x columnas con valores +1 y -1
	 * equiprobables.
	 */
	public static int[][] generarMatrizAleatoria(int filas, int columnas) {
		int[][] matriz = new int[filas][columnas];
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				matriz[i][j] = valorAleatorio();
			}
		}
		return matriz;
	}

	/**
	 * Escribe la matriz en un archivo de texto, con valores separados por espacios
	 * y filas separadas por saltos de línea.
	 */
	public static void guardarMatrizEnArchivo(int[][] matriz, String rutaArchivo) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
			for (int[] fila : matriz) {
				bw.write(formatearFila(fila));
				bw.newLine();
			}
		}
	}

	/**
	 * Genera y guarda una grilla aleatoria de tamaño filas x columnas en el archivo
	 * indicado.
	 */
	public static void guardarAleatoriaEnArchivo(int filas, int columnas, String rutaArchivo) throws IOException {
		int[][] matriz = generarMatrizAleatoria(filas, columnas);
		guardarMatrizEnArchivo(matriz, rutaArchivo);
	}

	// ==================== Métodos auxiliares privados ====================

	private static int valorAleatorio() {
		return ALEATORIO.nextBoolean() ? 1 : -1;
	}

	private static String formatearFila(int[] fila) {
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < fila.length; k++) {
			sb.append(fila[k]);
			if (k < fila.length - 1)
				sb.append(' ');
		}
		return sb.toString();
	}
}
