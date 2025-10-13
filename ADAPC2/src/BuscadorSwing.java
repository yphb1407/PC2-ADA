import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class BuscadorSwing extends JFrame {
    private final Map<String, List<Integer>> indice = new HashMap<>();
    private final Map<Integer, String> documentos = new HashMap<>();
    private int siguienteId = 1;

    // Componentes GUI
    private final JTextField consultaField = new JTextField();
    private final JButton buscarBtn = new JButton("Buscar");
    private final JTextArea resultadosArea = new JTextArea();
    private final JTextArea nuevoDocArea = new JTextArea(4, 30);
    private final JButton agregarDocBtn = new JButton("Agregar documento");

    public BuscadorSwing() {
        super("Buscador con Índice Invertido (Swing)");
        initGUI();
        // Documentos de ejemplo
        agregarDocumento("Java es un lenguaje poderoso");
        agregarDocumento("Aprender Java y estructuras de datos");
        agregarDocumento("Estructuras de datos eficientes son clave en Java");
    }

    private void initGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // Panel superior: caja de consulta y botón
        JPanel topPanel = new JPanel(new BorderLayout(6, 6));
        consultaField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        topPanel.add(new JLabel("Consulta:"), BorderLayout.WEST);
        topPanel.add(consultaField, BorderLayout.CENTER);
        topPanel.add(buscarBtn, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Panel central: resultados (scroll)
        resultadosArea.setEditable(false);
        resultadosArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollResultados = new JScrollPane(resultadosArea);
        scrollResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        add(scrollResultados, BorderLayout.CENTER);

        // Panel derecho: añadir documento
        JPanel rightPanel = new JPanel(new BorderLayout(6, 6));
        JScrollPane scrollNuevoDoc = new JScrollPane(nuevoDocArea);
        scrollNuevoDoc.setBorder(BorderFactory.createTitledBorder("Nuevo documento (texto)"));
        rightPanel.add(scrollNuevoDoc, BorderLayout.CENTER);
        rightPanel.add(agregarDocBtn, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);

        // Instrucciones abajo
        JTextArea instrucciones = new JTextArea(
            "Soporta consultas:\n" +
            " - palabra\n" +
            " - palabra1 AND palabra2\n" +
            " - palabra1 OR palabra2\n" +
            " - palabra1 NOT palabra2\n\n" +
            "Ejemplos: java\n" +
            "         java AND datos\n" +
            "         estructuras OR poderoso\n" +
            "         java NOT estructuras\n"
        );
        instrucciones.setEditable(false);
        instrucciones.setBackground(getBackground());
        add(instrucciones, BorderLayout.SOUTH);

        // Listeners
        buscarBtn.addActionListener(e -> procesarConsultaDesdeGUI());
        consultaField.addActionListener(e -> procesarConsultaDesdeGUI()); // Enter en campo
        agregarDocBtn.addActionListener(e -> {
            String texto = nuevoDocArea.getText().trim();
            if (texto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Escribe el contenido del documento antes de agregar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            agregarDocumento(texto);
            nuevoDocArea.setText("");
            JOptionPane.showMessageDialog(this, "Documento agregado e indexado (ID " + (siguienteId - 1) + ").", "OK", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void procesarConsultaDesdeGUI() {
        String consulta = consultaField.getText().trim();
        if (consulta.isEmpty()) {
            resultadosArea.setText("Escribe una consulta válida.");
            return;
        }
        List<Integer> resultados = procesarConsulta(consulta);
        mostrarResultados(resultados);
    }

    // --- Lógica del índice invertido ---

    public void agregarDocumento(String contenido) {
        int id = siguienteId++;
        documentos.put(id, contenido);

        String[] palabras = contenido.toLowerCase().split("\\W+");
        for (String palabra : palabras) {
            if (palabra.isBlank()) continue;
            indice.putIfAbsent(palabra, new ArrayList<>());
            List<Integer> lista = indice.get(palabra);
            // mantener sin duplicados y ordenada
            if (lista.isEmpty() || lista.get(lista.size() - 1) != id) {
                lista.add(id);
            }
        }
    }

    private List<Integer> buscar(String termino) {
        return new ArrayList<>(indice.getOrDefault(termino.toLowerCase(), Collections.emptyList()));
    }

    private List<Integer> interseccion(List<Integer> a, List<Integer> b) {
        List<Integer> res = new ArrayList<>();
        int i = 0, j = 0;
        while (i < a.size() && j < b.size()) {
            int ai = a.get(i), bj = b.get(j);
            if (ai == bj) {
                res.add(ai); i++; j++;
            } else if (ai < bj) i++; else j++;
        }
        return res;
    }

    private List<Integer> union(List<Integer> a, List<Integer> b) {
        Set<Integer> s = new TreeSet<>();
        s.addAll(a); s.addAll(b);
        return new ArrayList<>(s);
    }

    private List<Integer> diferencia(List<Integer> a, List<Integer> b) {
        Set<Integer> s = new LinkedHashSet<>(a);
        s.removeAll(b);
        return new ArrayList<>(s);
    }

    // Procesa consultas simples: AND, OR, NOT o única
    private List<Integer> procesarConsulta(String consulta) {
        consulta = consulta.trim();
        String low = consulta.toLowerCase();

        if (low.contains(" and ")) {
            String[] parts = low.split("\\s+and\\s+", 2);
            String p1 = parts[0].trim();
            String p2 = parts[1].trim();
            return interseccion(buscar(p1), buscar(p2));
        } else if (low.contains(" or ")) {
            String[] parts = low.split("\\s+or\\s+", 2);
            String p1 = parts[0].trim();
            String p2 = parts[1].trim();
            return union(buscar(p1), buscar(p2));
        } else if (low.contains(" not ")) {
            String[] parts = low.split("\\s+not\\s+", 2);
            String p1 = parts[0].trim();
            String p2 = parts[1].trim();
            return diferencia(buscar(p1), buscar(p2));
        } else {
            return buscar(consulta);
        }
    }

    private void mostrarResultados(List<Integer> resultados) {
        StringBuilder sb = new StringBuilder();
        if (resultados.isEmpty()) {
            sb.append("❌ No se encontraron documentos.\n");
        } else {
            sb.append("📌 Documentos encontrados: ").append(resultados.size()).append("\n\n");
            for (int id : resultados) {
                String texto = documentos.getOrDefault(id, "(contenido no disponible)");
                sb.append("📄 Documento ").append(id).append(":\n");
                sb.append(texto).append("\n");
                sb.append("--------------------------------------------------\n");
            }
        }
        resultadosArea.setText(sb.toString());
        resultadosArea.setCaretPosition(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BuscadorSwing app = new BuscadorSwing();
            app.setVisible(true);
        });
    }
}
