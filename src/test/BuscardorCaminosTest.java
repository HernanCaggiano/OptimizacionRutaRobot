package test;

import modelo.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class BuscardorCaminosTest {
	 @Test
	    public void testEncuentraCaminoValidoSinPoda() throws IOException {
	        Grilla grilla = new Grilla("recursos/grilla1.txt");
	        BuscadorCaminos buscador = new BuscadorCaminos(grilla);
	        buscador.buscarSinPoda();
	        assertTrue(buscador.getCaminosValidos().size() > 0);
	    }

	    @Test
	    public void testEncuentraCaminoValidoConPoda() throws IOException {
	        Grilla grilla = new Grilla("recursos/grilla1.txt");
	        BuscadorCaminos buscador = new BuscadorCaminos(grilla);
	        buscador.buscarConPoda();
	        assertTrue(buscador.getCaminosValidos().size() > 0);
	    }

	    @Test
	    public void testCantidadDeLlamadasConPodaEsMenor() throws IOException {
	        Grilla grilla = new Grilla("recursos/grilla1.txt");
	        BuscadorCaminos buscador = new BuscadorCaminos(grilla);
	        buscador.buscarSinPoda();
	        int sinPoda = buscador.getLlamadasSinPoda();
	        buscador.buscarConPoda();
	        int conPoda = buscador.getLlamadasConPoda();
	        assertTrue(conPoda <= sinPoda);
	    }



}
