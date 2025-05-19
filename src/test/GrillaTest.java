package test;

import modelo.Grilla;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class GrillaTest {
	private Grilla grilla;

    @BeforeEach
    void setUp() throws IOException {
        grilla = new Grilla("src/test/resources/grilla.txt");
    }

    @Test
    void testDimensionesCorrectas() {
        assertEquals(3, grilla.getFilas());
        assertEquals(3, grilla.getColumnas());
    }

    @Test
    void testContenidoDeLaGrilla() {
        assertEquals(1, grilla.getValor(0, 0));
        assertEquals(-1, grilla.getValor(0, 2));
        assertEquals(1, grilla.getValor(1, 1));
        assertEquals(1, grilla.getValor(2, 2));
    }

    @Test
    void testMatrizNoEsNula() {
        assertNotNull(grilla.getMatriz());
    }
}