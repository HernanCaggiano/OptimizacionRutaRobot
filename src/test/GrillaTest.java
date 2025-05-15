package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.Test;

import modelo.Grilla;

public class GrillaTest {
	@Test
    public void testCargaGrilla() throws IOException {
        Grilla grilla = new Grilla("recursos/grilla1.txt");

        // Verificar dimensiones correctas (ejemplo: 4x4)
        assertEquals(4, grilla.getFilas(), "Debe tener 4 filas");
        assertEquals(4, grilla.getColumnas(), "Debe tener 4 columnas");

        // Verificar valores específicos (ejemplo posición [0][0] = 1, [0][1] = -1)
        assertEquals(1, grilla.getValor(0, 0), "Valor en (0,0) debe ser 1");
        assertEquals(-1, grilla.getValor(0, 1), "Valor en (0,1) debe ser -1");
        assertEquals(-1, grilla.getValor(3, 2), "Valor en (3,2) debe ser -1");
        assertEquals(1, grilla.getValor(1, 1), "Valor en (1,1) debe ser 1");
    }

}
