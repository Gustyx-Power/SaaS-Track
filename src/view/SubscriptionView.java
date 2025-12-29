package view;

import util.MaterialTheme;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Date;

/**
 * SubscriptionView dengan Material Design 3
 */
public class SubscriptionView extends JPanel {

    private JTextField txtId, txtNamaLayanan, txtVendor, txtHarga, txtSearch;
    private JSpinner dateSpinner;
    private JComboBox<String> cmbStatus, cmbDepartment;
    private JButton btnSimpan, btnEdit, btnHapus, btnBersihkan;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    public SubscriptionView() {
        setLayout(new BorderLayout(24, 24));
        setOpaque(false);
        add(createSearchPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        loadSampleData();
    }

    private JPanel createSearchPanel() {
        JPanel p = new JPanel(new BorderLayout(16, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MaterialTheme.SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel lbl = new JLabel("🔍");
        lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        txtSearch = new JTextField();
        txtSearch.setFont(MaterialTheme.BODY_LARGE);
        txtSearch.setForeground(MaterialTheme.ON_SURFACE);
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                filterTable();
            }
        });

        p.add(lbl, BorderLayout.WEST);
        p.add(txtSearch, BorderLayout.CENTER);
        return p;
    }

    private JPanel createFormPanel() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MaterialTheme.SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.dispose();
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(320, 0));
        p.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Form Langganan");
        title.setFont(MaterialTheme.TITLE_MEDIUM);
        title.setForeground(MaterialTheme.ON_SURFACE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtId = new JTextField();
        txtId.setVisible(false);
        txtNamaLayanan = createMaterialField();
        txtVendor = createMaterialField();
        txtHarga = createMaterialField();

        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy"));
        dateSpinner.setFont(MaterialTheme.BODY_MEDIUM);
        dateSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        cmbStatus = createMaterialCombo(new String[] { "active", "expired", "cancelled" });
        cmbDepartment = createMaterialCombo(new String[] { "IT Department", "Marketing", "Human Resources" });

        p.add(title);
        p.add(Box.createVerticalStrut(24));
        addFormRow(p, "Nama Layanan", txtNamaLayanan);
        addFormRow(p, "Vendor", txtVendor);
        addFormRow(p, "Harga (Rp)", txtHarga);
        addFormRow(p, "Tgl Expired", dateSpinner);
        addFormRow(p, "Status", cmbStatus);
        addFormRow(p, "Departemen", cmbDepartment);
        p.add(Box.createVerticalStrut(24));
        p.add(createButtonPanel());
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JTextField createMaterialField() {
        JTextField tf = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MaterialTheme.SURFACE_VARIANT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setFont(MaterialTheme.BODY_MEDIUM);
        tf.setForeground(MaterialTheme.ON_SURFACE);
        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(12, 16, 12, 16));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        tf.setCaretColor(MaterialTheme.PRIMARY);
        return tf;
    }

    private JComboBox<String> createMaterialCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(MaterialTheme.BODY_MEDIUM);
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        cb.setBackground(MaterialTheme.SURFACE_VARIANT);
        return cb;
    }

    private void addFormRow(JPanel p, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(MaterialTheme.LABEL_MEDIUM);
        lbl.setForeground(MaterialTheme.ON_SURFACE_VARIANT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(8));
        p.add(field);
        p.add(Box.createVerticalStrut(16));
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new GridLayout(2, 2, 12, 12));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 112));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnSimpan = createMaterialBtn("Simpan", MaterialTheme.PRIMARY, MaterialTheme.ON_PRIMARY);
        btnEdit = createMaterialBtn("Update", MaterialTheme.SECONDARY, Color.WHITE);
        btnHapus = createMaterialBtn("Hapus", MaterialTheme.ERROR, Color.WHITE);
        btnBersihkan = createMaterialBtn("Bersihkan", MaterialTheme.SURFACE_VARIANT, MaterialTheme.ON_SURFACE);
        btnBersihkan.addActionListener(e -> clearForm());

        p.add(btnSimpan);
        p.add(btnEdit);
        p.add(btnHapus);
        p.add(btnBersihkan);
        return p;
    }

    private JButton createMaterialBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(MaterialTheme.LABEL_LARGE);
        b.setForeground(fg);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel createTablePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MaterialTheme.SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("📋 Daftar Langganan");
        title.setFont(MaterialTheme.TITLE_MEDIUM);
        title.setForeground(MaterialTheme.ON_SURFACE);

        String[] cols = { "ID", "Nama Layanan", "Vendor", "Harga", "Tgl Expired", "Status", "Departemen" };
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        MaterialTheme.styleTable(table);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1)
                populateForm();
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);
        sp.getViewport().setBackground(MaterialTheme.SURFACE);

        p.add(title, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
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
