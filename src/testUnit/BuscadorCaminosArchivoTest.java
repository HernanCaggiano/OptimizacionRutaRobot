package testUnit;

import org.junit.jupiter.api.Test;

import modelo.BuscadorCaminos;
import modelo.Grilla;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class BuscadorCaminosArchivoTest {

    @Test
    void testGrillaTxt_sinCaminos() throws IOException, URISyntaxException {
        // 1) Cargar el recurso grilla.txt desde src/test/resources
        Path ruta = Paths.get(
            getClass().getClassLoader().getResource("grilla.txt").toURI()
        );
        Grilla grilla = new Grilla(ruta.toString());
        BuscadorCaminos buscador = new BuscadorCaminos(grilla);

        // 2) Ejecutar búsquedas
        buscador.ejecutarSinPoda();
        buscador.ejecutarConPoda();

        // 3) Obtener resultados
        List<List<Point>> sinPoda = buscador.getCaminosSinPoda();
        List<List<Point>> conPoda = buscador.getCaminosConPoda();

        // 4) Comprobar que no hay caminos válidos
        assertTrue(sinPoda.isEmpty(), "No debe encontrar caminos sin poda");
        assertTrue(conPoda.isEmpty(), "No debe encontrar caminos con poda");

        // 5) Comprobar que sí exploró algo
        assertTrue(buscador.getLlamadasSinPoda() > 0, "Debe haber explorado sin poda");
        assertTrue(buscador.getLlamadasConPoda() > 0, "Debe haber explorado con poda");

        // 6) Verificar que la poda no explora más que la versión sin poda
        assertTrue(
            buscador.getLlamadasConPoda() <= buscador.getLlamadasSinPoda(),
            "Con poda debe explorar igual o menos nodos que sin poda"
        );
    }
}