package test;

import modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BuscardorCaminosTest {
	private Grilla grilla;
    private BuscadorCaminos buscador;

    @BeforeEach
    void setUp() throws IOException {
        grilla = new Grilla("src/test/resources/grilla.txt");
        buscador = new BuscadorCaminos(grilla);
    }

    @Test
    void testBuscarConPodaGeneraCaminos() {
        buscador.buscarConPoda();
        List<List<int[]>> caminos = buscador.getCaminosValidos();

        assertNotNull(caminos);
        assertFalse(caminos.isEmpty(), "Debe encontrar al menos un camino válido");
    }

    @Test
    void testLlamadasConPodaMayorACero() {
        buscador.buscarConPoda();
        assertTrue(buscador.getLlamadasConPoda() > 0);
    }

    @Test
    void testTiempoConPodaCalculado() {
        buscador.buscarConPoda();
        assertTrue(buscador.getTiempoConPoda() >= 0);
    }

    @Test
    void testCaminosLleganAlFinal() {
        buscador.buscarConPoda();
        List<List<int[]>> caminos = buscador.getCaminosValidos();

        for (List<int[]> camino : caminos) {
            int[] ultimaCelda = camino.get(camino.size() - 1);
            assertEquals(grilla.getFilas() - 1, ultimaCelda[0]);
            assertEquals(grilla.getColumnas() - 1, ultimaCelda[1]);
        }
    }

    @Test
    void testTodosLosCaminosSumanCero() {
        buscador.buscarConPoda();
        List<List<int[]>> caminos = buscador.getCaminosValidos();

        for (List<int[]> camino : caminos) {
            int suma = 0;
            for (int[] paso : camino) {
                suma += grilla.getValor(paso[0], paso[1]);
            }
            assertEquals(0, suma, "El camino no suma 0");
        }
    }
}