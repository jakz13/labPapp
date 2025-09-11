package Presentacion.gui.guiSesion;



import Logica.*;
import javax.swing.*;
import java.awt.*;


public class ConsultaPaquete {
    private JPanel PanelConsultaPaquete;
    private JComboBox<Paquete> comboboxPaquetes;
    private JTextPane InfoPaquetes;
    private JComboBox<RutaVuelo> comboBoxRutaVuelo;
    private JTextArea textAreaInfoRuta;
    private JButton cerrarButton;


    public ConsultaPaquete() {
        ISistema sistema = Fabrica.getInstance().getISistema();


        // Cargar paquetes al iniciar
        cargarPaquetes();


        // Al seleccionar un paquete mostrar su información
        comboboxPaquetes.addActionListener(e -> actualizarInfoPaquete());


        // Al seleccionar una ruta dentro del paquete mostrar su info detallada
        //comboBoxRutaVuelo.addActionListener(e -> actualizarInfoRuta());


        comboBoxRutaVuelo.addActionListener(e -> {
            RutaVuelo seleccionada = (RutaVuelo) comboBoxRutaVuelo.getSelectedItem();
            DefaultComboBoxModel<Vuelo> modeloVuelos = new DefaultComboBoxModel<>();


            if (seleccionada != null) {
                StringBuilder infoRuta = new StringBuilder();
                infoRuta.append("Nombre: ").append(seleccionada.getNombre()).append("\n")
                        .append("Origen: ").append(seleccionada.getCiudadOrigen()).append("\n")
                        .append("Destino: ").append(seleccionada.getCiudadDestino()).append("\n")
                        .append("Duración Estimada: ").append(seleccionada.getHora()).append(" horas\n")
                        .append("Vuelos Asociados: ").append(seleccionada.getVuelos().size()).append("\n");


                textAreaInfoRuta.setText(infoRuta.toString());

                for (Vuelo v : seleccionada.getVuelos()) {
                    modeloVuelos.addElement(v);
                }
            } else {
                textAreaInfoRuta.setText("");
            }


        });




        // Botón cerrar
        cerrarButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(PanelConsultaPaquete);
            if (topFrame != null) {
                topFrame.dispose();
            }
        });
    }


    private void cargarPaquetes() {
        DefaultComboBoxModel<Paquete> modeloPaquetes = new DefaultComboBoxModel<>();
        for (Paquete p : ManejadorPaquete.getInstance().getPaquetes()) {
            modeloPaquetes.addElement(p);
        }
        comboboxPaquetes.setModel(modeloPaquetes);
        comboboxPaquetes.setSelectedIndex(-1);
    }


    private void actualizarInfoPaquete() {
        Paquete seleccionado = (Paquete) comboboxPaquetes.getSelectedItem();
        if (seleccionado != null) {
            StringBuilder info = new StringBuilder();
            info.append("Nombre: ").append(seleccionado.getNombre()).append("\n")
                    .append("Descripción: ").append(seleccionado.getDescripcion()).append("\n")
                    //.append("Costo base: ").append(seleccionado.getCosto()).append("\n")
                    .append("Descuento: ").append(seleccionado.getDescuento()).append("\n")
                    .append("Validez: ").append(seleccionado.getPeriodoValidez()).append("\n");


            // 🔹 Mostrar el costo calculado final
            double costoTotal = seleccionado.calcularCostoReservaPaquete();
            info.append("Costo final (con rutas y descuento): ").append(costoTotal).append("\n");


            info.append("Rutas incluidas:\n");


            DefaultComboBoxModel<RutaVuelo> modeloRutas = new DefaultComboBoxModel<>();


            for (ItemPaquete item : seleccionado.getItemPaquetes()) {
                RutaVuelo r = item.getRutaVuelo();
                if (r != null) {
                    modeloRutas.addElement(r);
                    info.append("  - ").append(r.getNombre())
                            .append(" (").append(item.getTipoAsiento())
                            .append(", Asientos: ").append(item.getCantAsientos())
                            .append(")\n");
                }
            }


            comboBoxRutaVuelo.setModel(modeloRutas);
            InfoPaquetes.setText(info.toString());
        } else {
            InfoPaquetes.setText("");
            comboBoxRutaVuelo.setModel(new DefaultComboBoxModel<>());
        }
        textAreaInfoRuta.setText(""); // limpiar info de ruta
    }








    private void actualizarInfoRuta() {
        RutaVuelo seleccionada = (RutaVuelo) comboBoxRutaVuelo.getSelectedItem();
        if (seleccionada != null) {
            StringBuilder info = new StringBuilder();
            info.append("Nombre Ruta: ").append(seleccionada.getNombre()).append("\n")
                    .append("Origen: ").append(seleccionada.getCiudadOrigen()).append("\n")
                    .append("Destino: ").append(seleccionada.getCiudadDestino()).append("\n")
                    .append("Duración: ").append(seleccionada.getDescripcion()).append(" horas").append("\n")
                    .append("Fecha de Alta: ").append(seleccionada.getFechaAlta()).append("\n");


            textAreaInfoRuta.setText(info.toString());
        } else {
            textAreaInfoRuta.setText("");
        }
    }


    public Container getPanelConsultaPaquete() {
        return PanelConsultaPaquete;
    }
}
