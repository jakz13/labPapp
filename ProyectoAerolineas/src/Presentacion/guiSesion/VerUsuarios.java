package guiSesion;

import Logica.*;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class VerUsuarios {
    private JPanel panelConsultaRuta;
    private JButton cerrarButton;
    private JComboBox<String> comboBoxUsuarios;
    private JTextArea textAreaDatosUsuario;
    private JTextArea textAreaDatosAdicionales;
    private JList<Object> listAdicionales;

    public VerUsuarios() {
        ISistema sistema = Fabrica.getInstance().getISistema();

        // --- Cargar todos los usuarios ---
        DefaultComboBoxModel<String> modeloUsuarios = new DefaultComboBoxModel<>();
        for (Cliente c : sistema.listarClientes()) {
            modeloUsuarios.addElement("Cliente:" + c.getNickname());
        }
        for (Aerolinea a : sistema.listarAerolineas()) {
            modeloUsuarios.addElement("Aerolinea:" + a.getNickname());
        }
        comboBoxUsuarios.setModel(modeloUsuarios);
        comboBoxUsuarios.setSelectedIndex(-1);

        // --- Acción al seleccionar usuario ---
        comboBoxUsuarios.addActionListener(e -> {
            String seleccionado = (String) comboBoxUsuarios.getSelectedItem();
            if (seleccionado == null) return;

            DefaultListModel<Object> modelo = new DefaultListModel<>();
            String nick;
            if (seleccionado.startsWith("Cliente:")) {
                nick = seleccionado.substring("Cliente:".length()).trim();
                Cliente c = sistema.obtenerCliente(nick);
                if (c != null) {
                    textAreaDatosUsuario.setText(
                            "Cliente: " + c.getNombre() + " " + c.getApellido() + "\n" +
                                    "Email: " + c.getEmail() + "\n" +
                                    "Fecha Nacimiento: " + c.getFechaNacimiento() + "\n" +
                                    "Nacionalidad: " + c.getNacionalidad() + "\n"
                    );
                    for (Object obj : sistema.obtenerDatosAdicionalesUsuario(nick)) {
                        modelo.addElement(obj);
                    }
                    textAreaDatosAdicionales.setText("Reservas y Paquetes del cliente");
                }
            } else if (seleccionado.startsWith("Aerolinea:")) {
                nick = seleccionado.substring("Aerolinea:".length()).trim();
                Aerolinea a = sistema.obtenerAerolinea(nick);
                if (a != null) {
                    textAreaDatosUsuario.setText(
                            "Aerolinea: " + a.getNombre() + "\n" +
                                    "Email: " + a.getEmail() + "\n" +
                                    "Sitio Web: " + a.getSitioWeb() + "\n" +
                                    "Descripcion: " + a.getDescripcion() + "\n"
                    );
                    for (Object obj : sistema.obtenerDatosAdicionalesUsuario(nick)) {
                        modelo.addElement(obj);
                    }
                    textAreaDatosAdicionales.setText("Rutas de la aerolínea");
                }
            }
            listAdicionales.setModel(modelo);
        });

        // --- Acción al seleccionar adicional ---
        listAdicionales.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Object seleccionado = listAdicionales.getSelectedValue();
                if (seleccionado == null) return;

                if (seleccionado instanceof RutaVuelo) {
                    RutaVuelo ruta = (RutaVuelo) seleccionado;
                    StringBuilder info = new StringBuilder();
                    info.append("Ruta: ").append(ruta.getNombre()).append("\n")
                            .append("Origen: ").append(ruta.getCiudadOrigen()).append("\n")
                            .append("Destino: ").append(ruta.getCiudadDestino()).append("\n")
                            .append("Hora: ").append(ruta.getHora()).append("\n")
                            .append("Fecha Alta: ").append(ruta.getFechaAlta()).append("\n")
                            .append("Costo Turista: ").append(ruta.getCostoTurista()).append("\n")
                            .append("Costo Ejecutivo: ").append(ruta.getCostoEjecutivo()).append("\n")
                            .append("Costo Equipaje Extra: ").append(ruta.getCostoEquipajeExtra()).append("\n")
                            .append("Vuelos asociados:\n");
                    for (Vuelo v : ruta.getVuelos().values()) {
                        info.append("  - ").append(v.getNombre())
                                .append(" (Fecha: ").append(v.getFecha()).append(")\n");
                    }
                    textAreaDatosAdicionales.setText(info.toString());
                } else if (seleccionado instanceof Vuelo) {
                    Vuelo vuelo = (Vuelo) seleccionado;
                    StringBuilder info = new StringBuilder();
                    info.append("Vuelo: ").append(vuelo.getNombre()).append("\n")
                            .append("Ruta: ").append(vuelo.getNombreRuta()).append("\n")
                            .append("Duración: ").append(vuelo.getDuracion()).append(" días\n")
                            .append("Fecha: ").append(vuelo.getFecha()).append("\n")
                            .append("Asientos Turista: ").append(vuelo.getAsientosTurista()).append("\n")
                            .append("Asientos Ejecutivo: ").append(vuelo.getAsientosEjecutivo()).append("\n")
                            .append("Fecha Alta: ").append(vuelo.getFechaAlta()).append("\n")
                            .append("Reservas:\n");
                    Map<String, Reserva> reservas = vuelo.getReservas();
                    if (reservas.isEmpty()) {
                        info.append("  Ninguna reserva\n");
                    } else {
                        for (Reserva r : reservas.values()) {
                            info.append("  ID: ").append(r.getId())
                                    .append(", Costo: ").append(r.getCosto())
                                    .append(", Tipo: ").append(r.getTipoAsiento())
                                    .append(", Pasajes: ").append(r.getCantidadPasajes())
                                    .append(", Fecha: ").append(r.getFecha())
                                    .append("\n");
                        }
                    }
                    textAreaDatosAdicionales.setText(info.toString());
                } else if (seleccionado instanceof Paquete) {
                    Paquete paquete = (Paquete) seleccionado;
                    StringBuilder info = new StringBuilder();
                    info.append("Paquete: ").append(paquete.getNombre()).append("\n")
                            .append("Descripción: ").append(paquete.getDescripcion()).append("\n")
                            .append("Costo: ").append(paquete.getCosto()).append("\n")
                            .append("Descuento: ").append(paquete.getDescuento()).append("%\n")
                            .append("Validez: ").append(paquete.getPeriodoValidez()).append("\n")
                            .append("Fecha Alta: ").append(paquete.getFechaAlta()).append("\n")
                            .append("Rutas incluidas:\n");
                    for (ItemPaquete item : paquete.getItemPaquetes()) {
                        info.append("  - Ruta: ").append(item.getRutaVuelo().getNombre())
                                .append(", Asientos: ").append(item.getCantAsientos())
                                .append(", Tipo: ").append(item.getTipoAsiento()).append("\n");
                    }
                    info.append("Compras realizadas:\n");
                    for (CompraPaqLogica compra : paquete.getCompras()) {
                        info.append("  Cliente: ").append(compra.getCliente().getNombre())
                                .append(", Fecha compra: ").append(compra.getFechaCompra())
                                .append(", Vence: ").append(compra.getFechaVenc())
                                .append(", Costo: ").append(compra.getCosto()).append("\n");
                    }
                    textAreaDatosAdicionales.setText(info.toString());
                }
            }
        });

        // --- Botón cerrar ---
        cerrarButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panelConsultaRuta);
            if (topFrame != null) topFrame.dispose();
        });
    }

    public Container getPanelConsultaRuta() {
        return panelConsultaRuta;
    }
}
