package controlador;

import java.io.IOException;
import java.util.List;

import modelo.*;

public class ControladorRobot {
	private Grilla grilla;
    private BuscadorCaminos buscador;

    public void cargarGrilla(String path) throws IOException {
        grilla = new Grilla(path);
        buscador = new BuscadorCaminos(grilla);
    }

    public void ejecutar() {
        if (buscador != null) buscador.buscarConPoda();
    }

    public Object[] getResultados() {
        return new Object[]{
            grilla.getFilas() + "x" + grilla.getColumnas(),
            buscador.getTiempoConPoda(),
            buscador.getLlamadasConPoda()
        };
    }

    public List<List<int[]>> getCaminosValidos() {
        return buscador.getCaminosValidos();
    }

    public int[][] getGrilla() {
        return grilla.getMatriz();
    }

    public int getFilas() {
        return grilla.getFilas();
    }

    public int getColumnas() {
        return grilla.getColumnas();
    }
}
