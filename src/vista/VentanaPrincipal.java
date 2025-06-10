package vista;

import controlador.ControladorRobot;
import modelo.GeneradorGrilla;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.Point;
import java.io.IOException;
import java.util.List;

/**
 * Ventana principal: carga archivos, genera grillas aleatorias,
 * ejecuta búsquedas y muestra resultados,
 * animando en tiempo real el camino que explora el robot y mostrando métricas.
 */
public class VentanaPrincipal extends JFrame {
    private final ControladorRobot controlador = new ControladorRobot();
    private final PanelGrilla panelGrilla = new PanelGrilla();

    // Labels de métricas
    private final JLabel lblTamanio         = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblTiempoSin       = new JLabel();
    private final JLabel lblExploradosSin   = new JLabel();
    private final JLabel lblCaminosSin      = new JLabel();
    private final JLabel lblTiempoCon       = new JLabel();
    private final JLabel lblExploradosCon   = new JLabel();
    private final JLabel lblCaminosCon      = new JLabel();

    // Botones
    private final JButton btnGenerar = new JButton("Generador de grilla aleatoria");
    private final JButton btnCargar  = new JButton("Cargar grilla");
    private final JButton btnBuscar  = new JButton("Iniciar búsqueda");

    // Combos para seleccionar camino
    private final JComboBox<String> comboSinPoda = new JComboBox<>();
    private final JComboBox<String> comboConPoda = new JComboBox<>();

    public VentanaPrincipal() {
        super("Robot en Planta de Energía");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        inicializarLayout();
        configurarEventos();
        pack();
        setLocationRelativeTo(null);
    }

    private void inicializarLayout() {
        setLayout(new BorderLayout(5,5));

        // Panel superior: generador, cargar y buscar
        JPanel pnlTop = new JPanel();
        pnlTop.add(btnGenerar);
        pnlTop.add(btnCargar);
        pnlTop.add(btnBuscar);
        add(pnlTop, BorderLayout.NORTH);

        // Panel central: grilla
        panelGrilla.setPreferredSize(new Dimension(400,400));
        add(panelGrilla, BorderLayout.CENTER);

        // Panel derecho: métricas y combos
        JPanel pnlDerecha = new JPanel(new BorderLayout(10,10));

        // Tamaño grande en el norte
        lblTamanio.setFont(lblTamanio.getFont().deriveFont(Font.BOLD, 20f));
        pnlDerecha.add(lblTamanio, BorderLayout.NORTH);

        // Métricas divididas en Sin poda / Con poda
        JPanel pnlMediciones = new JPanel(new GridLayout(2,1,10,10));

        JPanel pnlSin = new JPanel(new GridLayout(3,1,2,2));
        pnlSin.setBorder(new TitledBorder("Sin poda"));
        pnlSin.add(lblTiempoSin);
        pnlSin.add(lblExploradosSin);
        pnlSin.add(lblCaminosSin);

        JPanel pnlCon = new JPanel(new GridLayout(3,1,2,2));
        pnlCon.setBorder(new TitledBorder("Con poda"));
        pnlCon.add(lblTiempoCon);
        pnlCon.add(lblExploradosCon);
        pnlCon.add(lblCaminosCon);

        pnlMediciones.add(pnlSin);
        pnlMediciones.add(pnlCon);
        pnlDerecha.add(pnlMediciones, BorderLayout.CENTER);

        // Combos en el sur
        JPanel pnlCombos = new JPanel(new GridLayout(2,1,5,5));
        pnlCombos.add(comboSinPoda);
        pnlCombos.add(comboConPoda);
        pnlDerecha.add(pnlCombos, BorderLayout.SOUTH);

        add(pnlDerecha, BorderLayout.EAST);
    }

    private void configurarEventos() {
        btnGenerar.addActionListener(e -> generarGrillaAleatoria());
        btnCargar.addActionListener(e -> cargarGrilla());
        btnBuscar.addActionListener(e -> iniciarBusqueda());

        comboSinPoda.addActionListener(e -> mostrarCamino(comboSinPoda.getSelectedIndex(), false));
        comboConPoda.addActionListener(e -> mostrarCamino(comboConPoda.getSelectedIndex(), true));
    }

    /** Pide dimensiones, genera la grilla aleatoria, la guarda y la carga en la vista */
    private void generarGrillaAleatoria() {
        // Pedimos filas
        String sFilas = JOptionPane.showInputDialog(
            this,
            "Ingrese número de filas:",
            "Generar grilla aleatoria",
            JOptionPane.QUESTION_MESSAGE
        );
        if (sFilas == null) return;
        // Pedimos columnas
        String sCols = JOptionPane.showInputDialog(
            this,
            "Ingrese número de columnas:",
            "Generar grilla aleatoria",
            JOptionPane.QUESTION_MESSAGE
        );
        if (sCols == null) return;

        int filas, columnas;
        try {
            filas = Integer.parseInt(sFilas.trim());
            columnas = Integer.parseInt(sCols.trim());
            if (filas < 1 || columnas < 1) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Filas y columnas deben ser enteros positivos.",
                "Entrada inválida",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Pedimos ruta donde guardar
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar grilla aleatoria");
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        String ruta = chooser.getSelectedFile().getAbsolutePath();

        // Generar y cargar
        try {
            controlador.generarYCargarGrillaAleatoria(filas, columnas, ruta);
            panelGrilla.setGrilla(controlador.getMatriz());
            panelGrilla.repaint();
            // Limpiar métricas y combos
            lblTamanio.setText("");
            lblTiempoSin.setText("");
            lblExploradosSin.setText("");
            lblCaminosSin.setText("");
            lblTiempoCon.setText("");
            lblExploradosCon.setText("");
            lblCaminosCon.setText("");
            comboSinPoda.removeAllItems();
            comboConPoda.removeAllItems();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo generar o cargar la grilla:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarGrilla() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            controlador.cargarGrilla(chooser.getSelectedFile().getAbsolutePath());
            panelGrilla.setGrilla(controlador.getMatriz());
            panelGrilla.repaint();
            // Limpiar métricas y combos
            lblTamanio.setText("");
            lblTiempoSin.setText("");
            lblExploradosSin.setText("");
            lblCaminosSin.setText("");
            lblTiempoCon.setText("");
            lblExploradosCon.setText("");
            lblCaminosCon.setText("");
            comboSinPoda.removeAllItems();
            comboConPoda.removeAllItems();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error al leer archivo:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void iniciarBusqueda() {
        // Suscribirse a la animación
        modelo.BuscadorCaminos busc = controlador.getBuscador();
        busc.addPathListener(camino -> {
            SwingUtilities.invokeLater(() -> {
                panelGrilla.setCamino(camino);
                panelGrilla.repaint();
            });
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        });

        // Ejecutar en hilo aparte
        new Thread(() -> {
            controlador.iniciarBusqueda();
            SwingUtilities.invokeLater(() -> {
                actualizarMetricas();
                poblarCombos();
                // Avisar si no hay caminos
                boolean sinNinguno = controlador.getCaminosSinPoda().isEmpty()
                                   && controlador.getCaminosConPoda().isEmpty();
                if (sinNinguno) {
                    JOptionPane.showMessageDialog(
                        this,
                        "No se encontró ningún camino cuyos cargos sumen cero.",
                        "Sin resultados",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            });
        }).start();
    }

    private void actualizarMetricas() {
        lblTamanio.setText("Tamaño: " + controlador.getFilas() + " x " + controlador.getColumnas());
        lblTiempoSin.setText    ("Tiempo (ms):    " + controlador.getTiempoSinPoda());
        lblExploradosSin.setText("Explorados:     " + controlador.getLlamadasSinPoda());
        lblCaminosSin.setText   ("Caminos:        " + controlador.getCaminosSinPoda().size());

        lblTiempoCon.setText    ("Tiempo (ms):    " + controlador.getTiempoConPoda());
        lblExploradosCon.setText("Explorados:     " + controlador.getLlamadasConPoda());
        lblCaminosCon.setText   ("Caminos:        " + controlador.getCaminosConPoda().size());
    }

    private void poblarCombos() {
        comboSinPoda.removeAllItems();
        comboConPoda.removeAllItems();
        comboSinPoda.addItem("Seleccione camino sin poda");
        comboConPoda.addItem("Seleccione camino con poda");
        for (int i = 0; i < controlador.getCaminosSinPoda().size(); i++) {
            comboSinPoda.addItem("Camino " + (i+1));
        }
        for (int i = 0; i < controlador.getCaminosConPoda().size(); i++) {
            comboConPoda.addItem("Camino " + (i+1));
        }
    }

    private void mostrarCamino(int indice, boolean conPoda) {
        if (indice <= 0) return;
        List<Point> camino = conPoda
            ? controlador.getCaminosConPoda().get(indice-1)
            : controlador.getCaminosSinPoda().get(indice-1);
        panelGrilla.setCamino(camino);
        panelGrilla.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
