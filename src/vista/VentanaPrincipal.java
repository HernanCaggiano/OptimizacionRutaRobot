package vista;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import controlador.ControladorRobot;
import java.awt.*;
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

        // Panel superior con botones
        JPanel panelBotones = new JPanel();
        JButton btnCargar = new JButton("Cargar Archivo");
        JButton btnEjecutar = new JButton("Ejecutar");
        comboCaminos = new JComboBox<>();
        comboCaminos.addItem("Seleccionar camino...");
        panelBotones.add(btnCargar);
        panelBotones.add(btnEjecutar);
        panelBotones.add(comboCaminos);
        add(panelBotones, BorderLayout.NORTH);

        //Panel de grilla
        panelGrilla = new JPanel();
        JScrollPane scrollGrilla = new JScrollPane(panelGrilla);

        // Tabla de resultados
        modeloResultados = new DefaultTableModel(new Object[]{"Tamaño", "Tiempo (ms)", "Llamadas"}, 0);
        tablaResultados = new JTable(modeloResultados);
        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.setPreferredSize(new Dimension(900, 120));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollGrilla, scrollTabla);
        splitPane.setResizeWeight(0.85);
        splitPane.setDividerSize(5);
        splitPane.setOneTouchExpandable(true);

        add(splitPane, BorderLayout.SOUTH);

        // Acciones
        btnCargar.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    controlador.cargarGrilla(chooser.getSelectedFile().getAbsolutePath());
                    mostrarGrilla(controlador.getGrilla());
                    modeloResultados.setRowCount(0);
                    comboCaminos.removeAllItems();
                    comboCaminos.addItem("Seleccionar camino...");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al cargar archivo: " + ex.getMessage());
                }
            }
        });

        btnEjecutar.addActionListener(e -> {
            controlador.ejecutar();
            actualizarResultados();
            cargarCaminos();
        });

        comboCaminos.addActionListener(e -> {
            if (comboCaminos.getSelectedIndex() > 0) {
                List<int[]> camino = controlador.getCaminosValidos().get(comboCaminos.getSelectedIndex() - 1);
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
        camino.forEach(paso -> celdas[paso[0]][paso[1]].setBackground(Color.YELLOW));
    }

    private void actualizarResultados() {
        modeloResultados.setRowCount(0);
        modeloResultados.addRow(controlador.getResultados());
    }

    private void cargarCaminos() {
        comboCaminos.removeAllItems();
        comboCaminos.addItem("Seleccionar camino...");
        for (int i = 0; i < controlador.getCaminosValidos().size(); i++) {
            comboCaminos.addItem("Camino " + (i + 1));
        }
    }
}