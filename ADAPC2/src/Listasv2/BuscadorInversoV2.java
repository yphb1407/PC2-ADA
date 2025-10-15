/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Listasv2;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * BuscadorInversoV2
 *
 * - Interfaz moderna (Swing)
 * - Botón "Agregar documento" con formulario (JDialog)
 * - Índice invertido dinámico (🫀 CORAZÓN)
 * - Visor con resaltado de términos buscados
 *
 * Archivo solicitado: BuscadorInversoV2.java
 */
public class BuscadorInversoV2 extends JFrame {

    // 🫀 CORAZÓN DEL SISTEMA: LISTA INVERTIDA (palabra -> conjunto de IDs de documento)
    private final Map<String, Set<Integer>> indiceInvertido = new HashMap<>();

    // 📚 Documentos por ID (LinkedHashMap para mantener orden de inserción)
    private final Map<Integer, String> documentos = new LinkedHashMap<>();

    // ID incremental para documentos nuevos
    private int siguienteId = 1;

    // Componentes UI
    private final JTextField campoBusqueda = new JTextField(30);
    private final JButton botonBuscar = new JButton("🔍 Buscar");
    private final JButton botonAgregar = new JButton("➕ Agregar documento");
    private final DefaultListModel<String> modeloResultados = new DefaultListModel<>();
    private final JList<String> listaResultados = new JList<>(modeloResultados);
    // Mapa auxiliar: índice en JList -> ID documento
    private final java.util.List<Integer> listaIds = new ArrayList<>();
    private final JTextArea areaInfo = new JTextArea();

    public BuscadorInversoV2() {
        super("Buscador Inverso V2");
        inicializarDatosEjemplo();

        construirInterfaz();
    }

    private void inicializarDatosEjemplo() {
        agregarDocumentoInterno("Java es un lenguaje de programación poderoso");
        agregarDocumentoInterno("Aprender Java y estructuras de datos");
        agregarDocumentoInterno("Estructuras de datos eficientes son clave en Java");
        agregarDocumentoInterno("Python es popular en ciencia de datos");
    }

    // Agrega documento durante inicialización sin re-ejecutar GUI eventos
    private void agregarDocumentoInterno(String contenido) {
        int id = siguienteId++;
        documentos.put(id, contenido);
        indexarDocumento(id, contenido);
    }

    // Indexa un documento (añadir sus tokens al índice invertido)
    private void indexarDocumento(int id, String contenido) {
        String[] tokens = contenido.toLowerCase().split("\\W+");
        for (String t : tokens) {
            if (t.isBlank()) continue;
            indiceInvertido.computeIfAbsent(t, k -> new LinkedHashSet<>()).add(id);
        }
    }

    // Reconstruye índice desde documentos (por si alguna vez se necesita)
    @SuppressWarnings("unused")
    private void reconstruirIndiceCompleto() {
        indiceInvertido.clear();
        for (Map.Entry<Integer, String> e : documentos.entrySet()) {
            indexarDocumento(e.getKey(), e.getValue());
        }
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(248, 249, 250));
        panelPrincipal.setBorder(new EmptyBorder(16, 16, 16, 16));
        panelPrincipal.setLayout(new BorderLayout(12, 12));
        add(panelPrincipal, BorderLayout.CENTER);

        // Header
        JPanel header = new JPanel(new BorderLayout(8, 8));
        header.setBackground(panelPrincipal.getBackground());
        JLabel titulo = new JLabel("Buscador Inverso V2");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(titulo, BorderLayout.WEST);

        // Búsqueda - center
        JPanel busquedaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        busquedaPanel.setBackground(panelPrincipal.getBackground());
        campoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoBusqueda.setPreferredSize(new Dimension(480, 36));
        campoBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        botonBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonBuscar.setBackground(new Color(10, 102, 194));
        botonBuscar.setForeground(Color.WHITE);
        botonBuscar.setFocusPainted(false);
        botonBuscar.setPreferredSize(new Dimension(110, 36));

        botonAgregar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonAgregar.setBackground(new Color(34, 138, 76));
        botonAgregar.setForeground(Color.WHITE);
        botonAgregar.setFocusPainted(false);
        botonAgregar.setPreferredSize(new Dimension(170, 36));

        busquedaPanel.add(campoBusqueda);
        busquedaPanel.add(botonBuscar);
        busquedaPanel.add(botonAgregar);

        header.add(busquedaPanel, BorderLayout.CENTER);
        panelPrincipal.add(header, BorderLayout.NORTH);

        // Center splitpane: resultados (izq) - info/instrucciones (der)
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.66);
        split.setDividerSize(6);
        split.setBorder(null);
        split.setBackground(panelPrincipal.getBackground());

        // Resultados - izquierda
        JPanel resultadosPanel = new JPanel(new BorderLayout(8, 8));
        resultadosPanel.setBackground(panelPrincipal.getBackground());
        listaResultados.setFont(new Font("Consolas", Font.PLAIN, 13));
        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollResultados = new JScrollPane(listaResultados);
        scrollResultados.setBorder(BorderFactory.createTitledBorder("Resultados (doble click para abrir visor)"));
        resultadosPanel.add(scrollResultados, BorderLayout.CENTER);
        split.setLeftComponent(resultadosPanel);

        // Info / instrucciones - derecha
        JPanel infoPanel = new JPanel(new BorderLayout(8, 8));
        infoPanel.setBackground(panelPrincipal.getBackground());
        areaInfo.setEditable(false);
        areaInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        areaInfo.setText(
                "Instrucciones:\n\n" +
                " - Escribe una consulta y presiona Buscar (o Enter).\n" +
                " - Soporta operadores: AND, OR, NOT (ej: java AND datos)\n" +
                " - Doble click en un resultado para abrir el visor con resaltado.\n" +
                " - Usa '➕ Agregar documento' para indexar más textos en vivo.\n\n" +
                "Consejo: puedes agregar un título al documento en el formulario para identificarlo mejor."
        );
        areaInfo.setLineWrap(true);
        areaInfo.setWrapStyleWord(true);
        JScrollPane scrollInfo = new JScrollPane(areaInfo);
        scrollInfo.setBorder(BorderFactory.createTitledBorder("Información"));
        infoPanel.add(scrollInfo, BorderLayout.CENTER);
        split.setRightComponent(infoPanel);

        panelPrincipal.add(split, BorderLayout.CENTER);

        // Footer: status simple
        JLabel footer = new JLabel("Documentos indexados: " + documentos.size());
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelPrincipal.add(footer, BorderLayout.SOUTH);

        // Listeners
        botonBuscar.addActionListener(e -> ejecutarBusquedaYMostrar());
        campoBusqueda.addActionListener(e -> ejecutarBusquedaYMostrar());
        botonAgregar.addActionListener(e -> abrirDialogoAgregarDocumento(footer));
        listaResultados.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int idx = listaResultados.getSelectedIndex();
                    if (idx >= 0 && idx < listaIds.size()) {
                        int id = listaIds.get(idx);
                        abrirVisorDocumento(id, campoBusqueda.getText());
                    }
                }
            }
        });

        // Resultado inicial vacío
        modeloResultados.clear();
    }

    // Ejecuta búsqueda y rellena la JList
    private void ejecutarBusquedaYMostrar() {
        String consulta = campoBusqueda.getText().trim();
        if (consulta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe una consulta válida.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Set<Integer> ids = procesarConsulta(consulta);
        mostrarEnListaResultados(ids, consulta);
    }

    private void mostrarEnListaResultados(Set<Integer> ids, String consulta) {
        modeloResultados.clear();
        listaIds.clear();
        if (ids.isEmpty()) {
            modeloResultados.addElement("❌ No se encontraron documentos para: \"" + consulta + "\"");
            return;
        }
        for (Integer id : ids) {
            String texto = documentos.getOrDefault(id, "");
            String linea = id + " - " + generarSnippet(texto, 80);
            modeloResultados.addElement(linea);
            listaIds.add(id);
        }
    }

    private String generarSnippet(String texto, int maxLen) {
        String t = texto.replaceAll("\\s+", " ").trim();
        if (t.length() <= maxLen) return t;
        return t.substring(0, maxLen - 3) + "...";
    }

    // Procesa consulta simple con AND/OR/NOT o palabra única
    private Set<Integer> procesarConsulta(String consultaOriginal) {
        String consulta = consultaOriginal.trim().toLowerCase();
        if (consulta.contains(" and ")) {
            String[] parts = consulta.split("\\s+and\\s+", 2);
            return interseccion(buscarPalabra(parts[0]), buscarPalabra(parts[1]));
        } else if (consulta.contains(" or ")) {
            String[] parts = consulta.split("\\s+or\\s+", 2);
            return union(buscarPalabra(parts[0]), buscarPalabra(parts[1]));
        } else if (consulta.contains(" not ")) {
            String[] parts = consulta.split("\\s+not\\s+", 2);
            return diferencia(buscarPalabra(parts[0]), buscarPalabra(parts[1]));
        } else {
            return new LinkedHashSet<>(buscarPalabra(consulta));
        }
    }

    private Set<Integer> buscarPalabra(String termino) {
        return indiceInvertido.getOrDefault(termino.trim(), Collections.emptySet());
    }

    private Set<Integer> interseccion(Set<Integer> a, Set<Integer> b) {
        Set<Integer> r = new LinkedHashSet<>(a);
        r.retainAll(b);
        return r;
    }

    private Set<Integer> union(Set<Integer> a, Set<Integer> b) {
        Set<Integer> r = new LinkedHashSet<>(a);
        r.addAll(b);
        return r;
    }

    private Set<Integer> diferencia(Set<Integer> a, Set<Integer> b) {
        Set<Integer> r = new LinkedHashSet<>(a);
        r.removeAll(b);
        return r;
    }

    // Abrir diálogo (JDialog) para agregar documento en forma moderna
    private void abrirDialogoAgregarDocumento(JLabel footerLabel) {
        JDialog dialog = new JDialog(this, "Agregar Documento", true);
        dialog.setSize(640, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false);

        JPanel cont = new JPanel();
        cont.setBorder(new EmptyBorder(12, 12, 12, 12));
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        dialog.add(cont, BorderLayout.CENTER);

        JLabel lblTitulo = new JLabel("Título (opcional):");
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField campoTitulo = new JTextField();
        campoTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        campoTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblContenido = new JLabel("Contenido:");
        lblContenido.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextArea campoContenido = new JTextArea(8, 40);
        campoContenido.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoContenido.setLineWrap(true);
        campoContenido.setWrapStyleWord(true);
        JScrollPane scrollContenido = new JScrollPane(campoContenido);
        scrollContenido.setAlignmentX(Component.LEFT_ALIGNMENT);

        cont.add(lblTitulo);
        cont.add(Box.createRigidArea(new Dimension(0, 6)));
        cont.add(campoTitulo);
        cont.add(Box.createRigidArea(new Dimension(0, 12)));
        cont.add(lblContenido);
        cont.add(Box.createRigidArea(new Dimension(0, 6)));
        cont.add(scrollContenido);
        cont.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(34, 138, 76));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnCancelar.setBackground(new Color(200, 60, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);

        footer.add(btnCancelar);
        footer.add(btnGuardar);
        dialog.add(footer, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnGuardar.addActionListener(e -> {
            String titulo = campoTitulo.getText().trim();
            String contenido = campoContenido.getText().trim();
            if (contenido.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "El contenido no puede estar vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String textoFinal = titulo.isEmpty() ? contenido : (titulo + " — " + contenido);
            int nuevoId = siguienteId++;
            documentos.put(nuevoId, textoFinal);
            indexarDocumento(nuevoId, textoFinal);

            JOptionPane.showMessageDialog(dialog, "Documento agregado e indexado (ID " + nuevoId + ").", "Listo", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();

            // actualizar footer y, si hay consulta activa, re-ejecutarla
            footerLabel.setText("Documentos indexados: " + documentos.size());
            if (!campoBusqueda.getText().trim().isEmpty()) {
                ejecutarBusquedaYMostrar();
            } else {
                modeloResultados.clear();
                modeloResultados.addElement("Documento agregado (ID " + nuevoId + "). Escribe una consulta para buscar.");
                listaIds.clear();
            }
        });

        dialog.setVisible(true);
    }

    // Abre visor que muestra el documento y resalta términos buscados
    private void abrirVisorDocumento(int id, String consultaOriginal) {
        String contenido = documentos.getOrDefault(id, "(contenido no disponible)");
        JDialog visor = new JDialog(this, "Documento " + id, true);
        visor.setSize(700, 500);
        visor.setLocationRelativeTo(this);
        visor.setLayout(new BorderLayout());

        JTextArea cabecera = new JTextArea("Documento " + id);
        cabecera.setEditable(false);
        cabecera.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cabecera.setBackground(Color.WHITE);
        cabecera.setBorder(new EmptyBorder(8, 8, 8, 8));
        visor.add(cabecera, BorderLayout.NORTH);

        JTextPane textoPane = new JTextPane();
        textoPane.setText(contenido);
        textoPane.setEditable(false);
        textoPane.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(textoPane);
        visor.add(scroll, BorderLayout.CENTER);

        // Resaltar términos encontrados
        List<String> terminos = extraerTerminosParaResaltar(consultaOriginal);
        if (!terminos.isEmpty()) {
            Highlighter high = textoPane.getHighlighter();
            high.removeAllHighlights();
            DefaultHighlighter.DefaultHighlightPainter highlightPainter =
                    new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 250, 102)); // amarillo suave

            String textoLower = contenido.toLowerCase();
            for (String term : terminos) {
                String t = term.toLowerCase();
                int fromIndex = 0;
                while (true) {
                    int pos = textoLower.indexOf(t, fromIndex);
                    if (pos < 0) break;
                    try {
                        high.addHighlight(pos, pos + t.length(), highlightPainter);
                    } catch (BadLocationException ex) {
                        // ignore
                    }
                    fromIndex = pos + t.length();
                }
            }
        }

        // Footer con botones
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> visor.dispose());
        footer.add(btnCerrar);
        visor.add(footer, BorderLayout.SOUTH);

        visor.setVisible(true);
    }

    /** Extrae términos para resaltar desde la consulta: descarta operadores AND/OR/NOT y paréntesis simples */
    private List<String> extraerTerminosParaResaltar(String consultaOriginal) {
        if (consultaOriginal == null) return Collections.emptyList();
        String low = consultaOriginal.toLowerCase();
        // Reemplazar operadores por espacios
        low = low.replaceAll("\\band\\b", " ");
        low = low.replaceAll("\\bor\\b", " ");
        low = low.replaceAll("\\bnot\\b", " ");
        // Quitar caracteres no alfanuméricos (pero mantener espacios)
        low = low.replaceAll("[^a-z0-9\\s]", " ");
        String[] parts = low.trim().split("\\s+");
        List<String> terms = new ArrayList<>();
        for (String p : parts) {
            if (p.isBlank()) continue;
            terms.add(p);
        }
        return terms;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Look and feel nativo opcional
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            BuscadorInversoV2 app = new BuscadorInversoV2();
            app.setVisible(true);
        });
    }
}

