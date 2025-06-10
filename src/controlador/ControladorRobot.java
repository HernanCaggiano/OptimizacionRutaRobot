package controlador;

import modelo.Grilla;
import modelo.BuscadorCaminos;
import modelo.GeneradorGrilla;

import java.io.IOException;

/**
 * Controlador principal: enlaza Modelo y Vista.
 */
public class ControladorRobot {
    private Grilla grilla;
    private BuscadorCaminos buscador;

    /** Carga la grilla desde un archivo */
    public void cargarGrilla(String rutaArchivo) throws IOException {
        grilla  = new Grilla(rutaArchivo);
        buscador = new BuscadorCaminos(grilla);
    }

    /** Ejecuta sin poda y con poda */
    public void iniciarBusqueda() {
        buscador.ejecutarSinPoda();
        buscador.ejecutarConPoda();
    }

    // Exponer el buscador para suscribir listeners
    public BuscadorCaminos getBuscador() {
        return buscador;
    }
    public void generarYCargarGrillaAleatoria(int filas, int columnas, String rutaArchivo) throws IOException {
        GeneradorGrilla.guardarAleatoriaEnArchivo(filas, columnas, rutaArchivo);
        cargarGrilla(rutaArchivo);
    }

    // --- Métodos de acceso para la Vista ---
    public int[][] getMatriz() { return grilla.getMatriz(); }
    public int getFilas()      { return grilla.getFilas(); }
    public int getColumnas()   { return grilla.getColumnas(); }
    public long getTiempoSinPoda()   { return buscador.getTiempoSinPoda(); }
    public long getTiempoConPoda()   { return buscador.getTiempoConPoda(); }
    public long getLlamadasSinPoda() { return buscador.getLlamadasSinPoda(); }
    public long getLlamadasConPoda() { return buscador.getLlamadasConPoda(); }
    public java.util.List<java.util.List<java.awt.Point>> getCaminosSinPoda() {
        return buscador.getCaminosSinPoda();
    }
    public java.util.List<java.util.List<java.awt.Point>> getCaminosConPoda() {
        return buscador.getCaminosConPoda();
    }
}
