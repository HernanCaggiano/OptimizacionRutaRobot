package vista;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import controlador.ControladorRobot;
import java.awt.*;
//import java.awt.event.*;
import java.io.File;
import java.util.List;

public class VentanaPrincipal extends JFrame {
	private static final long serialVersionUID = 1L;
	private ControladorRobot controlador;
    private JTable tablaResultados;
    private JComboBox<String> comboCaminos;
    private DefaultTableModel modeloResultados;
    private JPanel panelGrilla;
    private JLabel[][] celdas;

    public VentanaPrincipal() {
        setTitle("Ruta del Robot - TP3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        controlador = new ControladorRobot();

        JPanel panelBotones = new JPanel();
        JButton btnCargar = new JButton("Cargar Archivo");
        JButton btnSinPoda = new JButton("Ejecutar Sin Poda");
        JButton btnConPoda = new JButton("Ejecutar Con Poda");
        comboCaminos = new JComboBox<>();
        comboCaminos.addItem("Seleccionar camino...");
        panelBotones.add(btnCargar);
        panelBotones.add(btnSinPoda);
        panelBotones.add(btnConPoda);
        panelBotones.add(comboCaminos);

        add(panelBotones, BorderLayout.NORTH);

        modeloResultados = new DefaultTableModel(new Object[]{"Tamaño", "Tiempo Sin Poda (ms)", "Tiempo Con Poda (ms)", "Caminos Sin Poda", "Caminos Con Poda"}, 0);
        tablaResultados = new JTable(modeloResultados);
        add(new JScrollPane(tablaResultados), BorderLayout.SOUTH);

        panelGrilla = new JPanel();
        add(panelGrilla, BorderLayout.CENTER);

        btnCargar.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File archivo = chooser.getSelectedFile();
                try {
                    controlador.cargarGrilla(archivo.getAbsolutePath());
                    mostrarGrilla(controlador.getGrilla());
                    modeloResultados.setRowCount(0);
                    comboCaminos.removeAllItems();
                    comboCaminos.addItem("Seleccionar camino...");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al cargar archivo: " + ex.getMessage());
                }
            }
        });

        btnSinPoda.addActionListener(e -> {
            controlador.ejecutarSinPoda();
            actualizarResultados();
            cargarCaminos();
        });

        btnConPoda.addActionListener(e -> {
            controlador.ejecutarConPoda();
            actualizarResultados();
            cargarCaminos();
        });

        comboCaminos.addActionListener(e -> {
            if (comboCaminos.getSelectedIndex() > 0) {
                int index = comboCaminos.getSelectedIndex() - 1;
                List<int[]> camino = controlador.getCaminosValidos().get(index);
                resaltarCamino(camino);
            }
        });
    }

    private void mostrarGrilla(int[][] matriz) {
        panelGrilla.removeAll();
        int filas = matriz.length;
        int columnas = matriz[0].length;
        celdas = new JLabel[filas][columnas];
        panelGrilla.setLayout(new GridLayout(filas, columnas));

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JLabel label = new JLabel(String.valueOf(matriz[i][j]), SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(Color.WHITE);
                label.setBorder(new LineBorder(Color.BLACK));
                label.setFont(new Font("SansSerif", Font.BOLD, 18));
                celdas[i][j] = label;
                panelGrilla.add(label);
            }
        }
        panelGrilla.revalidate();
        panelGrilla.repaint();
    }

    private void resaltarCamino(List<int[]> camino) {
        int[][] matriz = controlador.getGrilla();
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                celdas[i][j].setText(String.valueOf(matriz[i][j]));
                celdas[i][j].setBackground(Color.WHITE);
            }
        }
        for (int[] paso : camino) {
            int i = paso[0];
            int j = paso[1];
            celdas[i][j].setBackground(Color.YELLOW);
        }
    }

    private void actualizarResultados() {
        modeloResultados.setRowCount(0);
        modeloResultados.addRow(controlador.getResultados());
    }

    private void cargarCaminos() {
        comboCaminos.removeAllItems();
        comboCaminos.addItem("Seleccionar camino...");
        List<List<int[]>> caminos = controlador.getCaminosValidos();
        for (int i = 0; i < caminos.size(); i++) {
            comboCaminos.addItem("Camino " + (i + 1));
        }
    }

}
