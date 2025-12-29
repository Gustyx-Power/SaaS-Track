package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

/**
 * View untuk mengelola data langganan (CRUD Screen)
 * Layout: BorderLayout - form di West, JTable di Center, search di North
 */
public class SubscriptionView extends JPanel {

    private JTextField txtId, txtNamaLayanan, txtVendor, txtHarga, txtSearch;
    private JSpinner dateSpinner;
    private JComboBox<String> cmbStatus, cmbDepartment;
    private JButton btnSimpan, btnEdit, btnHapus, btnBersihkan;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private static final Color PRIMARY = new Color(41, 128, 185);
    private static final Color SUCCESS = new Color(39, 174, 96);
    private static final Color DANGER = new Color(231, 76, 60);

    public SubscriptionView() {
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        add(createSearchPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        loadSampleData();
    }

    private JPanel createSearchPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(new Color(220, 220, 220)), new EmptyBorder(12, 15, 12, 15)));

        txtSearch = new JTextField();
        txtSearch
                .setBorder(new CompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(8, 12, 8, 12)));
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterTable();
            }
        });

        p.add(new JLabel("🔍 Cari:"), BorderLayout.WEST);
        p.add(txtSearch, BorderLayout.CENTER);
        return p;
    }

    private JPanel createFormPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setPreferredSize(new Dimension(280, 0));
        p.setBorder(new CompoundBorder(new LineBorder(new Color(220, 220, 220)), new EmptyBorder(20, 20, 20, 20)));

        txtId = new JTextField();
        txtId.setVisible(false);
        txtNamaLayanan = createTextField();
        txtVendor = createTextField();
        txtHarga = createTextField();

        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy"));
        dateSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        cmbStatus = new JComboBox<>(new String[] { "active", "expired", "cancelled" });
        cmbStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        cmbDepartment = new JComboBox<>(new String[] { "IT Department", "Marketing", "Human Resources" });
        cmbDepartment.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        addFormRow(p, "Nama Layanan *", txtNamaLayanan);
        addFormRow(p, "Vendor *", txtVendor);
        addFormRow(p, "Harga (Rp) *", txtHarga);
        addFormRow(p, "Tgl Expired *", dateSpinner);
        addFormRow(p, "Status *", cmbStatus);
        addFormRow(p, "Departemen *", cmbDepartment);

        p.add(Box.createVerticalStrut(20));
        p.add(createButtonPanel());
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        tf.setBorder(new CompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(6, 10, 6, 10)));
        return tf;
    }

    private void addFormRow(JPanel p, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(5));
        p.add(field);
        p.add(Box.createVerticalStrut(12));
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new GridLayout(2, 2, 8, 8));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnSimpan = createBtn("💾 Simpan", SUCCESS);
        btnEdit = createBtn("✏️ Update", PRIMARY);
        btnHapus = createBtn("🗑️ Hapus", DANGER);
        btnBersihkan = createBtn("🔄 Bersihkan", Color.GRAY);
        btnBersihkan.addActionListener(e -> clearForm());

        p.add(btnSimpan);
        p.add(btnEdit);
        p.add(btnHapus);
        p.add(btnBersihkan);
        return p;
    }

    private JButton createBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel createTablePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(new Color(220, 220, 220)), new EmptyBorder(15, 15, 15, 15)));

        String[] cols = { "ID", "Nama Layanan", "Vendor", "Harga", "Tgl Expired", "Status", "Departemen" };
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setSelectionBackground(new Color(41, 128, 185, 50));
        table.getTableHeader().setBackground(new Color(44, 62, 80));
        table.getTableHeader().setForeground(Color.WHITE);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1)
                populateForm();
        });

        p.add(new JLabel("📋 Daftar Langganan"), BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void filterTable() {
        String s = txtSearch.getText().trim();
        sorter.setRowFilter(s.isEmpty() ? null : RowFilter.regexFilter("(?i)" + s, 1));
    }

    private void populateForm() {
        int r = table.convertRowIndexToModel(table.getSelectedRow());
        txtId.setText(tableModel.getValueAt(r, 0).toString());
        txtNamaLayanan.setText(tableModel.getValueAt(r, 1).toString());
        txtVendor.setText(tableModel.getValueAt(r, 2).toString());
        txtHarga.setText(tableModel.getValueAt(r, 3).toString().replaceAll("[^0-9]", ""));
        cmbStatus.setSelectedItem(tableModel.getValueAt(r, 5));
        cmbDepartment.setSelectedItem(tableModel.getValueAt(r, 6));
    }

    public void clearForm() {
        txtId.setText("");
        txtNamaLayanan.setText("");
        txtVendor.setText("");
        txtHarga.setText("");
        dateSpinner.setValue(new Date());
        cmbStatus.setSelectedIndex(0);
        cmbDepartment.setSelectedIndex(0);
        table.clearSelection();
    }

    private void loadSampleData() {
        tableModel.addRow(new Object[] { 1, "Microsoft 365", "Microsoft", "Rp 1.500.000", "31-12-2025", "active",
                "IT Department" });
        tableModel.addRow(
                new Object[] { 2, "Adobe CC", "Adobe", "Rp 2.000.000", "30-06-2025", "active", "IT Department" });
        tableModel.addRow(
                new Object[] { 3, "Slack Business+", "Slack", "Rp 500.000", "15-03-2025", "active", "Marketing" });
        tableModel.addRow(
                new Object[] { 4, "Zoom Pro", "Zoom", "Rp 300.000", "01-12-2024", "expired", "Human Resources" });
    }

    // Getters
    public JTextField getTxtNamaLayanan() {
        return txtNamaLayanan;
    }

    public JTextField getTxtVendor() {
        return txtVendor;
    }

    public JTextField getTxtHarga() {
        return txtHarga;
    }

    public JSpinner getDateSpinner() {
        return dateSpinner;
    }

    public JComboBox<String> getCmbStatus() {
        return cmbStatus;
    }

    public JComboBox<String> getCmbDepartment() {
        return cmbDepartment;
    }

    public JButton getBtnSimpan() {
        return btnSimpan;
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JButton getBtnHapus() {
        return btnHapus;
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
