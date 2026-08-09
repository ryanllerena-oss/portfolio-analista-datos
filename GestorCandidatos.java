import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GestorCandidatos extends JFrame {
    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final JTextField buscar;
    private final List<Candidato> candidatos = new ArrayList<>();

    public GestorCandidatos() {
        setTitle("Gestor de Candidatos");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Vacante", "Estado"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        buscar = new JTextField(22);
        JButton nuevo = new JButton("Nuevo candidato");

        JPanel superior = new JPanel(new BorderLayout(10, 10));
        superior.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        superior.add(new JLabel("Buscar:"), BorderLayout.WEST);
        superior.add(buscar, BorderLayout.CENTER);
        superior.add(nuevo, BorderLayout.EAST);
        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarEjemplos();
        actualizarTabla(candidatos);
        nuevo.addActionListener(e -> mostrarFormulario());
        buscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });
    }

    private void mostrarFormulario() {
        JTextField nombre = new JTextField();
        JComboBox<String> vacante = new JComboBox<>(new String[]{"Analista de Datos Jr.", "Desarrollador Java", "Especialista BI"});
        JComboBox<String> estado = new JComboBox<>(new String[]{"Registrado", "Entrevista", "Finalista", "Contratado"});
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Nombre:")); panel.add(nombre);
        panel.add(new JLabel("Vacante:")); panel.add(vacante);
        panel.add(new JLabel("Estado:")); panel.add(estado);
        if (JOptionPane.showConfirmDialog(this, panel, "Nuevo candidato", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (nombre.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "El nombre es obligatorio."); return; }
            String id = String.format("CAN-%03d", candidatos.size() + 1);
            candidatos.add(new Candidato(id, nombre.getText().trim(), vacante.getSelectedItem().toString(), estado.getSelectedItem().toString()));
            actualizarTabla(candidatos);
        }
    }

    private void filtrar() {
        String texto = buscar.getText().toLowerCase();
        List<Candidato> resultado = new ArrayList<>();
        for (Candidato c : candidatos) if ((c.nombre + " " + c.vacante + " " + c.estado).toLowerCase().contains(texto)) resultado.add(c);
        actualizarTabla(resultado);
    }

    private void actualizarTabla(List<Candidato> lista) {
        modelo.setRowCount(0);
        for (Candidato c : lista) modelo.addRow(new Object[]{c.id, c.nombre, c.vacante, c.estado});
    }

    private void cargarEjemplos() {
        candidatos.add(new Candidato("CAN-001", "Lucía Mendoza", "Analista de Datos Jr.", "Entrevista"));
        candidatos.add(new Candidato("CAN-002", "Mateo Flores", "Desarrollador Java", "Registrado"));
        candidatos.add(new Candidato("CAN-003", "Valeria Ruiz", "Especialista BI", "Finalista"));
    }

    private static class Candidato {
        String id, nombre, vacante, estado;
        Candidato(String id, String nombre, String vacante, String estado) { this.id=id; this.nombre=nombre; this.vacante=vacante; this.estado=estado; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestorCandidatos().setVisible(true));
    }
}
