package modelo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Explora todos los caminos mínimos de (0,0) a (n-1,m-1),
 * midiendo tiempo y llamadas, con y sin poda, y notificando
 * cada paso (ruta parcial) a listeners.
 */
public class BuscadorCaminos {
    /** Interfaz para recibir la ruta parcial en tiempo real */
    public interface PathListener {
        void rutaActualizada(List<Point> caminoParcial);
    }

    private final Grilla grilla;
    private final int totalPasos;    // movimientos: n + m - 2
    private final int pasosVisita;   // celdas:     n + m - 1

    private final List<PathListener> listeners = new ArrayList<>();

    // Resultados sin poda
    private long llamadasSinPoda;
    private long tiempoSinPoda;
    private final List<List<Point>> caminosSinPoda = new ArrayList<>();

    // Resultados con poda
    private long llamadasConPoda;
    private long tiempoConPoda;
    private final List<List<Point>> caminosConPoda = new ArrayList<>();

    public BuscadorCaminos(Grilla grilla) {
        this.grilla      = grilla;
        this.totalPasos  = grilla.getFilas() + grilla.getColumnas() - 2;
        this.pasosVisita = totalPasos + 1;
    }

    /** Registrar un listener para la ruta parcial */
    public void addPathListener(PathListener l) {
        listeners.add(l);
    }
    private void notifyPathListeners(List<Point> camino) {
        List<Point> copia = new ArrayList<>(camino);
        for (PathListener l : listeners) {
            l.rutaActualizada(copia);
        }
    }

    /** Ejecuta la búsqueda pura (sin poda) */
    public void ejecutarSinPoda() {
        llamadasSinPoda = 0;
        caminosSinPoda.clear();
        long inicio = System.nanoTime();
        explorarSinPoda(0, 0, grilla.getValor(0,0), new ArrayList<>(List.of(new Point(0,0))));
        tiempoSinPoda = (System.nanoTime() - inicio) / 1_000_000;
    }

    private void explorarSinPoda(int fila, int col, int suma, List<Point> camino) {
        llamadasSinPoda++;
        notifyPathListeners(camino);

        if (fila == grilla.getFilas()-1 && col == grilla.getColumnas()-1) {
            if (suma == 0) caminosSinPoda.add(new ArrayList<>(camino));
            return;
        }

        // Mover derecha
        if (col < grilla.getColumnas()-1) {
            camino.add(new Point(fila, col+1));
            explorarSinPoda(fila, col+1, suma + grilla.getValor(fila, col+1), camino);
            camino.remove(camino.size()-1);
        }
        // Mover abajo
        if (fila < grilla.getFilas()-1) {
            camino.add(new Point(fila+1, col));
            explorarSinPoda(fila+1, col, suma + grilla.getValor(fila+1, col), camino);
            camino.remove(camino.size()-1);
        }
    }

    /** Ejecuta la búsqueda con poda inteligente */
    public void ejecutarConPoda() {
        llamadasConPoda = 0;
        caminosConPoda.clear();
        long inicio = System.nanoTime();
        explorarConPoda(0, 0, grilla.getValor(0,0), new ArrayList<>(List.of(new Point(0,0))));
        tiempoConPoda = (System.nanoTime() - inicio) / 1_000_000;
    }

    private void explorarConPoda(int fila, int col, int suma, List<Point> camino) {
        llamadasConPoda++;
        notifyPathListeners(camino);

        int pasosRestantes = pasosVisita - camino.size();
        // Poda si no hay forma de compensar la suma
        if (Math.abs(suma) > pasosRestantes) return;

        if (fila == grilla.getFilas()-1 && col == grilla.getColumnas()-1) {
            if (suma == 0) caminosConPoda.add(new ArrayList<>(camino));
            return;
        }

        // Mover derecha
        if (col < grilla.getColumnas()-1) {
            camino.add(new Point(fila, col+1));
            explorarConPoda(fila, col+1, suma + grilla.getValor(fila, col+1), camino);
            camino.remove(camino.size()-1);
        }
        // Mover abajo
        if (fila < grilla.getFilas()-1) {
            camino.add(new Point(fila+1, col));
            explorarConPoda(fila+1, col, suma + grilla.getValor(fila+1, col), camino);
            camino.remove(camino.size()-1);
        }
    }

    // --- Getters para controlador/vista ---
    public long getLlamadasSinPoda() { return llamadasSinPoda; }
    public long getLlamadasConPoda() { return llamadasConPoda; }
    public long getTiempoSinPoda()   { return tiempoSinPoda; }
    public long getTiempoConPoda()   { return tiempoConPoda; }
    public List<List<Point>> getCaminosSinPoda() { return caminosSinPoda; }
    public List<List<Point>> getCaminosConPoda() { return caminosConPoda; }
}
