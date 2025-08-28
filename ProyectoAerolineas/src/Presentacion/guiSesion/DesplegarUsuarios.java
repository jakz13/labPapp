// Supón que tienes una clase Cliente con los métodos getId(), getNombre(), getApellido(), getDocumento()
package guiSesion;


import Logica.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DesplegarUsuarios {
    private JPanel PanelUsuarios;
    public JTable TablaUsuarios;
    private JPanel PanelTablas;

    public DesplegarUsuarios(List<Cliente> clientes) {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Documento");

        // Cargar los datos desde la lista
        for (Cliente c : clientes) {
            modelo.addRow(new Object[]{c.getNombre(), c.getApellido(), c.getNumeroDocumento()});
        }

        // Se Asigna el modelo a la tabla del form
        TablaUsuarios.setModel(modelo);
        TablaUsuarios.setAutoCreateRowSorter(true); // permite ordenar columnas
    }

    public Container getPanelUsuarios() {
        return PanelUsuarios;
    }
}