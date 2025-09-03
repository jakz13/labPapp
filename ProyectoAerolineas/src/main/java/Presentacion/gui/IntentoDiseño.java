package Presentacion.gui;
import Logica.Fabrica;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import Presentacion.gui.guiSesion.InicioSesion;
import Presentacion.gui.guiSesion.DesplegarUsuarios;
import Presentacion.gui.guiSesion.AltaAereolinea;
import Presentacion.gui.guiSesion.AltaCiudad;
import Presentacion.gui.guiSesion.NuevaRutaVuelo;
import Presentacion.gui.guiSesion.AltaVuelo;
import Presentacion.gui.guiSesion.EligeUsuario;


public class IntentoDiseño {
    public JPanel Ventana;
    public JPanel Recuadro1;
    public JLabel NombreEmpresa;
    public JPanel RecuadroCentral;
    public JPanel PanelMedIzq;
    public JLabel label1;
    public JPanel PanelInferiorizq;
    public JButton AltaClienteButton;
    private JButton MOSTRARUSUARIOSbutton;
    private JPanel PanelUsuarios;
    private JButton AltaAereolinea;
    private JButton ALTACIUDADButton;
    private JButton AltaRutaVuelo;
    private JButton ALTAVUELO;
    private JButton MODIFICARUSUARIOS;

    public IntentoDiseño(JFrame framePrincipal) {
        AltaClienteButton.addActionListener(new ActionListener() {
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

                frameSesion.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        framePrincipal.setVisible(true);
                    }
                });

                frameSesion.setVisible(true);
            }
        });
        ALTACIUDADButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                framePrincipal.setVisible(false);

                AltaCiudad AC = new AltaCiudad();
                JFrame frameSesion = new JFrame("Alta Aereolinea");
                frameSesion.setContentPane(AC.getPanelDeCiudad());
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
        AltaRutaVuelo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                framePrincipal.setVisible(false);

                NuevaRutaVuelo NR = new NuevaRutaVuelo();
                JFrame frameSesion = new JFrame("Alta Ruta Vuelo");
                frameSesion.setContentPane(NR.getPanelAltaRuta());
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
        ALTAVUELO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                framePrincipal.setVisible(false);

                AltaVuelo AVU = new AltaVuelo();
                JFrame frameSesion = new JFrame("Alta Vuelo");
                frameSesion.setContentPane(AVU.getpanelDeVuelo());
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
        MODIFICARUSUARIOS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                framePrincipal.setVisible(false);

                EligeUsuario EG = new EligeUsuario();
                JFrame frameSesion = new JFrame("Modificar Usuario");
                frameSesion.setContentPane(EG.getpanelModificar());
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
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
