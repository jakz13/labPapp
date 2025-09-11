package Presentacion.gui.guiSesion;

import DataTypes.DtCliente;
import DataTypes.DtPaquete;
import Logica.Fabrica;
import Logica.ISistema;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CompraPaquete {
    private JPanel panel1;
    private JComboBox PaqueteSeleccionado;
    private JComboBox ClienteElegido;
    private JTextField CostoCalculado;
    private JButton cancelarCompraButton;
    private JButton aceptarCompraButton;
    private JComboBox Dia;
    private JComboBox Ano;
    private JComboBox Mes;

    public CompraPaquete() {
        ISistema sistema = Fabrica.getInstance().getISistema();

        cargarPaquetes();
        cargarClientes();

        PaqueteSeleccionado.addActionListener(e ->actualizarCosto());

        aceptarCompraButton.addActionListener(e -> {

            DtPaquete seleccionado = (DtPaquete) PaqueteSeleccionado.getSelectedItem();
            String nomPaquete = (seleccionado != null) ? seleccionado.getNombre() : null;

            DtCliente clienteSeleccionado = (DtCliente) ClienteElegido.getSelectedItem();
            String ClienteElegidoStr = (clienteSeleccionado != null) ? clienteSeleccionado.getNickname() : null;

            String costoStr = (String) CostoCalculado.getText();
            String diaStr = (String) Dia.getSelectedItem();
            String mesStr = (String) Mes.getSelectedItem();
            String anioStr = (String) Ano.getSelectedItem();

            if (diaStr.equals("Día") || mesStr.equals("Mes") || anioStr.equals("Año")) {
                JOptionPane.showMessageDialog(null,
                        "Debe seleccionar una fecha válida.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int dia = Integer.parseInt(diaStr);
            int mes = Integer.parseInt(mesStr);
            int anio = Integer.parseInt(anioStr);

            LocalDate fecha = LocalDate.of(anio, mes, dia);
            LocalDate hoy = LocalDate.now();

            long diasValidez = ChronoUnit.DAYS.between(hoy, fecha);

            double costo = Double.parseDouble(costoStr);

            try {
                sistema.compraPaquete(nomPaquete, ClienteElegidoStr, (int) diasValidez, hoy, costo);
                JOptionPane.showMessageDialog(null,
                        "Paquete Comprado Correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null,
                        ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            //compraPaquete(String nomPaquete, String nomCliente, int validezDias, LocalDate fechaC, double costo)
        });

        // Acción del botón Cancelar
        cancelarCompraButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(PaqueteSeleccionado);
            if (topFrame != null) {
                topFrame.dispose();
            }
        });
    }

    // Carga paquetes disponibles
    private void cargarPaquetes() {
        ISistema sistema = Fabrica.getInstance().getISistema();
        DefaultComboBoxModel<DtPaquete> modeloPaquetes = new DefaultComboBoxModel<>();
        for (DtPaquete p : sistema.getPaquetesDisp()) {
            modeloPaquetes.addElement(p);
        }
        PaqueteSeleccionado.setModel(modeloPaquetes);
        PaqueteSeleccionado.setSelectedIndex(-1);
    }

    private void cargarClientes() {
        ISistema sistema = Fabrica.getInstance().getISistema();
        DefaultComboBoxModel<DtCliente> modeloClientes = new DefaultComboBoxModel<>();
        for (DtCliente c : sistema.getClientes()) {
            modeloClientes.addElement(c);
        }
        ClienteElegido.setModel(modeloClientes);
        ClienteElegido.setSelectedIndex(-1);
    }

    private void actualizarCosto() {
        DtPaquete seleccionado = (DtPaquete) PaqueteSeleccionado.getSelectedItem();
        if (seleccionado != null) {
            // Calculamos el costo total usando el método que ya definimos
            double costoTotal = seleccionado.getCosto();

            // Mostramos el costo en el campo de texto
            CostoCalculado.setText(String.valueOf(costoTotal));
        } else {
            CostoCalculado.setText("");
        }
    }


    public Container getPanelDeSesion() {
        return panel1;
    }
}

