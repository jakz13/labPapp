package Presentacion.gui.guiSesion;

import DataTypes.DtRutaVuelo;
import DataTypes.DtAerolinea;
import logica.Fabrica;
import logica.ISistema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MasVistas {
    private JPanel panelPrincipal;
    private JPanel panelConDatos;
    private JTable tablaRanking;
    private JScrollPane scrollPane;
    private JButton btnActualizar;
    private JLabel lblTitulo;

    private final ISistema sistema;

    public MasVistas() {
        sistema = Fabrica.getInstance().getISistema();

        inicializarComponentes();
        configurarEventos();
        cargarDatos();

        // Forzar una recarga inicial desde BD
        sistema.cargarDesdeBd();
    }

    private void inicializarComponentes() {
        // Configurar el panel principal
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel de título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lblTitulo = new JLabel("Rutas de Vuelo Más Visitadas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelTitulo.add(lblTitulo);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnActualizar = new JButton("Actualizar");
        panelBotones.add(btnActualizar);

        // Configurar la tabla
        String[] columnNames = {"#", "Ruta de Vuelo", "Aerolínea", "Ciudad Origen", "Ciudad Destino", "Cantidad de visitas"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 5) return Integer.class;
                return String.class;
            }
        };

        tablaRanking = new JTable(model);
        tablaRanking.setRowHeight(25);
        tablaRanking.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaRanking.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        // Personalizar la tabla
        tablaRanking.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaRanking.getColumnModel().getColumn(1).setPreferredWidth(120);
        tablaRanking.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablaRanking.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaRanking.getColumnModel().getColumn(4).setPreferredWidth(120);
        tablaRanking.getColumnModel().getColumn(5).setPreferredWidth(100);

        scrollPane = new JScrollPane(tablaRanking);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        // Panel de datos
        panelConDatos = new JPanel(new BorderLayout());
        panelConDatos.add(scrollPane, BorderLayout.CENTER);

        // Ensamblar componentes
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelConDatos, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        btnActualizar.addActionListener(e -> {
            // Forzar recarga desde BD antes de actualizar
            sistema.cargarDesdeBd();
            cargarDatos();
        });
    }

    private void cargarDatos() {
        try {
            System.out.println("[DEBUG MasVistas] Cargando datos de rutas más visitadas...");

            // Obtener las 5 rutas más visitadas del sistema
            List<DtRutaVuelo> rutasMasVisitadas = sistema.obtenerTopRutasMasVisitadas(5);

            // Limpiar la tabla
            DefaultTableModel model = (DefaultTableModel) tablaRanking.getModel();
            model.setRowCount(0);

            if (rutasMasVisitadas == null || rutasMasVisitadas.isEmpty()) {
                model.addRow(new Object[]{1, "No hay datos disponibles", "", "", "", 0});
                System.out.println("[DEBUG MasVistas] No se encontraron rutas visitadas");
            } else {
                System.out.println("[DEBUG MasVistas] Encontradas " + rutasMasVisitadas.size() + " rutas");

                int posicion = 1;
                for (DtRutaVuelo ruta : rutasMasVisitadas) {
                    System.out.println("[DEBUG MasVistas] Procesando ruta: " + ruta.getNombre() +
                            " - Visitas: " + ruta.getContadorVisitas());

                    String nombreAerolinea = "No disponible";
                    try {
                        // Obtener información completa de la aerolínea
                        DtAerolinea aerolinea = sistema.obtenerAerolinea(ruta.getAerolinea());
                        if (aerolinea != null) {
                            nombreAerolinea = aerolinea.getNombre();
                        }
                    } catch (Exception ex) {
                        System.err.println("[DEBUG MasVistas] Error obteniendo aerolínea: " + ex.getMessage());
                        nombreAerolinea = ruta.getAerolinea();
                    }

                    model.addRow(new Object[]{
                            posicion++,
                            ruta.getNombre(),
                            nombreAerolinea,
                            ruta.getCiudadOrigen(),
                            ruta.getCiudadDestino(),
                            ruta.getContadorVisitas()
                    });
                }
            }

            tablaRanking.repaint();
            System.out.println("[DEBUG MasVistas] Tabla actualizada correctamente");

        } catch (Exception ex) {
            System.err.println("[ERROR MasVistas] Error al cargar datos: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(panelPrincipal,
                    "Error al cargar los datos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }
}