package testUnit;


import org.junit.jupiter.api.Test;

import modelo.BuscadorCaminos;
import modelo.Grilla;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class BuscadorCaminosTest {

    /**
     * Crea una grilla temporal con el contenido dado y la devuelve.
     */
    private Grilla crearGrillaDesdeContenido(String contenido) throws IOException {
        Path temp = Files.createTempFile("grilla", ".txt");
        Files.writeString(temp, contenido);
        return new Grilla(temp.toAbsolutePath().toString());
    }

    @Test
    void testGrid2x3Valido_TresCaminos() throws IOException {
        // ejemplo 2x3 con exactamente 3 caminos de suma cero
        String contenido =
            "1 -1 1\n" +
            "-1 1 -1\n";
        Grilla grilla = crearGrillaDesdeContenido(contenido);
        BuscadorCaminos buscador = new BuscadorCaminos(grilla);

        buscador.ejecutarSinPoda();
        buscador.ejecutarConPoda();

        List<List<Point>> sinPoda = buscador.getCaminosSinPoda();
        List<List<Point>> conPoda = buscador.getCaminosConPoda();

        assertEquals(3, sinPoda.size(), "Debe encontrar 3 caminos sin poda");
        assertEquals(3, conPoda.size(), "Debe encontrar 3 caminos con poda");

        // tiempos no negativos
        assertTrue(buscador.getTiempoSinPoda() >= 0, "Tiempo sin poda debe ser ≥ 0");
        assertTrue(buscador.getTiempoConPoda() >= 0, "Tiempo con poda debe ser ≥ 0");

        // hubo llamadas recursivas
        assertTrue(buscador.getLlamadasSinPoda() > 0, "Debe haber explorado al menos una vez sin poda");
        assertTrue(buscador.getLlamadasConPoda() > 0, "Debe haber explorado al menos una vez con poda");

        // la poda solo reduce o iguala el número de exploraciones
        assertTrue(
            buscador.getLlamadasConPoda() <= buscador.getLlamadasSinPoda(),
            "Con poda debe explorar igual o menos nodos que sin poda"
        );
    }

    @Test
    void testGrid2x3Invalido_SinCaminos() throws IOException {
        // ejemplo 2x3 sin ningún camino de suma cero
        String contenido =
            "1 1 1\n" +
            "1 1 1\n";
        Grilla grilla = crearGrillaDesdeContenido(contenido);
        BuscadorCaminos buscador = new BuscadorCaminos(grilla);

        buscador.ejecutarSinPoda();
        buscador.ejecutarConPoda();

        assertTrue(buscador.getCaminosSinPoda().isEmpty(), "No debe encontrar caminos sin poda");
        assertTrue(buscador.getCaminosConPoda().isEmpty(), "No debe encontrar caminos con poda");
    }

    @Test
    void testGrid3x4Valido_ComparaSinYConPoda() throws IOException {
        // ejemplo 3x4 con al menos un camino válido
        String contenido =
            "1 -1  1 -1\n" +
            "-1  1 -1  1\n" +
            "1 -1  1 -1\n";
        Grilla grilla = crearGrillaDesdeContenido(contenido);
        BuscadorCaminos buscador = new BuscadorCaminos(grilla);

        buscador.ejecutarSinPoda();
        buscador.ejecutarConPoda();

        // Al menos un camino encontrado
        assertFalse(buscador.getCaminosSinPoda().isEmpty(), "Debe encontrar al menos un camino sin poda");
        assertFalse(buscador.getCaminosConPoda().isEmpty(), "Debe encontrar al menos un camino con poda");

        // Poda siempre explora menos o igual
        assertTrue(
            buscador.getLlamadasConPoda() <= buscador.getLlamadasSinPoda(),
            "Con poda debe explorar igual o menos nodos que sin poda"
        );
    }
}
