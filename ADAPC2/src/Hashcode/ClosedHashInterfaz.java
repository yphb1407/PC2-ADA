package Hashcode;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ClosedHashInterfaz extends JFrame {
    private ClosedHashTable tabla;
    private JTextField txtTarjeta;
    private JTextArea txtResultado;

    private final String[] nombres = {
            "Carlos", "Lucía", "Mateo", "Ana", "Sofía", "Diego",
            "María", "Luis", "Elena", "José", "Valeria", "Andrés",
            "Camila", "Martín", "Gabriela", "Juan", "Fernanda", "Daniel",
            "Isabella", "Pedro"
    };

    public ClosedHashInterfaz() {
        tabla = new ClosedHashTable(15); // capacidad de la tabla

        // Look and Feel moderno
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        setTitle("📇 Closed Hashing - Tarjetas y Usuarios");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---------- Panel Superior ----------
        JPanel panelSuperior = new JPanel(new GridLayout(2, 2, 10, 10));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("🔍 Búsqueda de Usuario"));

        JLabel lblTarjeta = new JLabel("Número de Tarjeta:");
        lblTarjeta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtTarjeta = new JTextField();
        txtTarjeta.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panelSuperior.add(lblTarjeta);
        panelSuperior.add(txtTarjeta);

        // ---------- Panel de Botones ----------
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBorder(BorderFactory.createTitledBorder("⚙️ Opciones"));

        JButton btnGenerar = crearBoton("🎲 Generar Aleatorios", new Color(70, 130, 180));
        JButton btnBuscar = crearBoton("🔎 Buscar Usuario", new Color(46, 139, 87));
        JButton btnMostrar = crearBoton("📋 Mostrar Tabla", new Color(255, 140, 0));

        panelBotones.add(btnGenerar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnMostrar);

        // ---------- Área de Resultados ----------
        txtResultado = new JTextArea();
        txtResultado.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtResultado.setBackground(new Color(245, 245, 245));
        txtResultado.setMargin(new Insets(10, 10, 10, 10));
        txtResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtResultado);
        scroll.setBorder(BorderFactory.createTitledBorder("📜 Resultados"));

        // Layout principal
        setLayout(new BorderLayout(10, 10));
        add(panelSuperior, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        // ---------- Eventos ----------
        btnGenerar.addActionListener(e -> {
            tabla = new ClosedHashTable(15); // reiniciamos
            Random rand = new Random();
            for (int i = 1; i <= 10; i++) {
                String tarjeta = generarTarjeta(rand);
                String usuario = nombres[rand.nextInt(nombres.length)];
                tabla.insertar(tarjeta, usuario);
            }
            txtResultado.setText("✅ Se generaron 10 usuarios aleatorios.\nUsa 'Mostrar Tabla' para verlos.");
        });

        btnBuscar.addActionListener(e -> {
            String tarjeta = txtTarjeta.getText().trim();
            if (!tarjeta.isEmpty()) {
                String usuario = tabla.buscar(tarjeta);
                txtResultado.setText("🔎 Resultado búsqueda:\n" + usuario);
            } else {
                txtResultado.setText("⚠️ Ingresa un número de tarjeta para buscar.");
            }
        });

        btnMostrar.addActionListener(e -> {
            txtResultado.setText(tabla.mostrarTodo());
        });
    }

    // Método para crear botones estilizados
    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    // Genera un número de tarjeta de 16 dígitos
    private String generarTarjeta(Random rand) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClosedHashInterfaz().setVisible(true));
    }
}
