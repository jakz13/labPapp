package gui;
import Logica.Fabrica;
import guiSesion.AltaAereolinea;
import guiSesion.DesplegarUsuarios;
import guiSesion.InicioSesion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntentoDiseño {
    public JPanel Ventana;
    public JPanel Recuadro1;
    public JLabel NombreEmpresa;
    public JPanel RecuadroCentral;
    public JPanel PanelMedIzq;
    public JLabel label1;
    public JPanel PanelInferiorizq;
    public JButton INICIASESIONAQUIButton;
    private JButton MOSTRARUSUARIOSbutton;
    private JPanel PanelUsuarios;
    private JButton AltaAereolinea;

    public IntentoDiseño(JFrame framePrincipal) {
        INICIASESIONAQUIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                framePrincipal.setVisible(false);

                InicioSesion IS = new InicioSesion();
                JFrame frameSesion = new JFrame("Inicio de Sesión");
                frameSesion.setContentPane(IS.getPanelDeSesion());
                frameSesion.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frameSesion.setBounds(framePrincipal.getBounds());

                // Escucha el cierre de la ventana de inicio de sesión
                frameSesion.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        framePrincipal.setVisible(true);
                    }
                });

                frameSesion.setVisible(true);
            }
        });
        MOSTRARUSUARIOSbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    framePrincipal.setVisible(false);

                    DesplegarUsuarios DS = new DesplegarUsuarios(Fabrica.getInstance().getISistema().listarClientes());
                    JFrame frameSesion = new JFrame("Inicio de Sesión");
                    frameSesion.setContentPane(DS.getPanelUsuarios());
                    frameSesion.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frameSesion.setBounds(framePrincipal.getBounds());

                    frameSesion.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            framePrincipal.setVisible(true);
                        }
                    });

                    frameSesion.setVisible(true);
            }
        });
        AltaAereolinea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                framePrincipal.setVisible(false);

                AltaAereolinea AA = new AltaAereolinea();
                JFrame frameSesion = new JFrame("Alta Aereolinea");
                frameSesion.setContentPane(AA.getPanelDeSesion());
                frameSesion.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frameSesion.setBounds(framePrincipal.getBounds());

                // Escucha el cierre de la ventana de inicio de sesión
                frameSesion.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        framePrincipal.setVisible(true);
                    }
                });

                frameSesion.setVisible(true);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
