package modelo;

import java.util.ArrayList;
import java.util.List;

public class BuscadorCaminos {
	private Grilla grilla;
    private List<List<int[]>> caminosValidos = new ArrayList<>();
    private int llamadasConPoda = 0;
    private long tiempoConPoda = 0;

    public BuscadorCaminos(Grilla grilla) {
        this.grilla = grilla;
    }

    public void buscarConPoda() {
        caminosValidos.clear();
        llamadasConPoda = 0;
        long inicio = System.currentTimeMillis();
        buscar(0, 0, 0, new ArrayList<>());
        tiempoConPoda = System.currentTimeMillis() - inicio;
    }

    private void buscar(int i, int j, int suma, List<int[]> camino) {
        if (i >= grilla.getFilas() || j >= grilla.getColumnas()) return;

        llamadasConPoda++;
        suma += grilla.getValor(i, j);
        camino.add(new int[]{i, j});

        if (i == grilla.getFilas() - 1 && j == grilla.getColumnas() - 1) {
            caminosValidos.add(new ArrayList<>(camino));
            camino.remove(camino.size() - 1);
            return;
        }

        int restantes = (grilla.getFilas() - 1 - i) + (grilla.getColumnas() - 1 - j);
        if (Math.abs(suma) > restantes) {
            camino.remove(camino.size() - 1);
            return;
        }

        buscar(i + 1, j, suma, camino);
        buscar(i, j + 1, suma, camino);
        camino.remove(camino.size() - 1);
    }

    public List<List<int[]>> getCaminosValidos() { return caminosValidos; }
    public int getLlamadasConPoda() { return llamadasConPoda; }
    public long getTiempoConPoda() { return tiempoConPoda; }
}