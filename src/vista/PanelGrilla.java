package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.util.List;

/**
 * Panel que dibuja la grilla y destaca un camino dado.
 */
public class PanelGrilla extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[][] grilla;
    private List<Point> camino;

    public void setGrilla(int[][] grilla) {
        this.grilla = grilla;
        this.camino = null;
    }

    public void setCamino(List<Point> camino) {
        this.camino = camino;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (grilla == null) return;

        int filas = grilla.length;
        int cols  = grilla[0].length;
        int w = getWidth()  / cols;
        int h = getHeight() / filas;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < cols; j++) {
                int x = j * w, y = i * h;
                if (camino != null && camino.contains(new Point(i,j))) {
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(x, y, w, h);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, w, h);

                String texto = String.valueOf(grilla[i][j]);
                FontMetrics fm = g.getFontMetrics();
                int tx = x + (w - fm.stringWidth(texto)) / 2;
                int ty = y + (h + fm.getAscent()) / 2;
                g.drawString(texto, tx, ty);
            }
        }
    }
}
