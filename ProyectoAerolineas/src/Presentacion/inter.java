import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class inter extends JFrame {

    private JButton btnComprar;
    private JPanel panel;
    private JLabel titulo;
    private JButton boton_vuelo;
    private JButton boton_paquete;
    private JLabel texto_vuelo;
    private JLabel texto_paquete;
    private JPanel panel1;
    public inter() {
        setTitle("Menú Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centrar ventana
        setLayout(new FlowLayout());

        // Botón principal
        btnComprar = new JButton("Comprar boleto");
        add(btnComprar);

        // Acción al presionar el botón
        btnComprar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrir nueva ventana de compra
                CompraFrame compra = new CompraFrame();
                compra.setVisible(true);
            }
        });
    }

    // Clase interna para la ventana de compra
    private class CompraFrame extends JFrame {
        private JLabel lblTitulo;
        private JButton btnPaquete;
        private JButton btnBoleto;


        public CompraFrame() {
            setTitle("Compra");
            setSize(300, 200);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(3, 1, 10, 10));

            lblTitulo = new JLabel("Seleccione opción de compra:", SwingConstants.CENTER);
            btnPaquete = new JButton("Comprar Paquete");
            btnBoleto = new JButton("Comprar Boleto");

            add(lblTitulo);
            add(btnPaquete);
            add(btnBoleto);

            // Acción para cada botón
            btnPaquete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Función comprar paquete en construcción...");
                }
            });

            btnBoleto.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Función comprar boleto en construcción...");
                }
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new inter().setVisible(true);
        });
    }
}
