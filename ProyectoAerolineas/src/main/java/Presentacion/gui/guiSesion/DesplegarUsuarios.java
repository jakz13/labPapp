package Presentacion.gui.guiSesion;

import Logica.Aerolinea;
import Logica.Cliente;
import Logica.Fabrica;
import Logica.ISistema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DesplegarUsuarios {
    private JPanel PanelUsuarios;
    public JTable TablaUsuarios;
    private JPanel PanelTablas;

    private ISistema sistema;

    public DesplegarUsuarios() {
        sistema = Fabrica.getInstance().getISistema();
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        // Definimos las columnas
        String[] columnas = {"Tipo", "Nickname", "Nombre"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        // 1. Obtener aerolíneas
        List<Aerolinea> aerolineas = sistema.listarAerolineas(); // asumo que tenés este método
        for (Aerolinea a : aerolineas) {
            Object[] fila = {"Aerolínea", a.getNickname(), a.getNombre()};
            modelo.addRow(fila);
        }

        // 2. Obtener clientes
        List<Cliente> clientes = sistema.listarClientes(); // asumo que también existe
        for (Cliente c : clientes) {
            Object[] fila = {"Cliente", c.getNickname(), c.getNombre()};
            modelo.addRow(fila);
        }

        // Asignamos el modelo a la tabla
        TablaUsuarios.setModel(modelo);
    }

    public Container getPanelUsuarios() {
        return PanelUsuarios;
    }
}
