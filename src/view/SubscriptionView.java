package view;

import dao.SubscriptionDAO;
import model.Subscription;
import util.MaterialTheme;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * SubscriptionView - Redesigned to match reference image
 */
public class SubscriptionView extends JPanel {

    private JTextField txtId, txtNamaLayanan, txtVendor, txtHarga, txtSearch;
    private JSpinner dateSpinner;
    private JComboBox<String> cmbStatus, cmbDepartment;
    private JButton btnSimpan, btnEdit, btnHapus, btnBersihkan;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private SubscriptionDAO subscriptionDAO;
    private int[] deptIds = { 1, 2, 3 };

    public SubscriptionView() {
        subscriptionDAO = new SubscriptionDAO();
        setLayout(new BorderLayout(24, 24));
        setOpaque(false);

        // Top Panel: Search + Add Button
        add(createTopPanel(), BorderLayout.NORTH);

        // Main Content: Form + Table side by side
        JPanel mainContent = new JPanel(new BorderLayout(24, 0));
        mainContent.setOpaque(false);
        mainContent.add(createFormPanel(), BorderLayout.WEST);
        mainContent.add(createTablePanel(), BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        loadDataFromDB();
        setupButtonActions();
    }

    private void setupButtonActions() {
        btnSimpan.addActionListener(e -> simpanData());
        btnEdit.addActionListener(e -> updateData());
        btnHapus.addActionListener(e -> hapusData());
    }

    private void simpanData() {
        if (!validateForm())
            return;
        try {
            Subscription sub = new Subscription();
            sub.setNamaLayanan(txtNamaLayanan.getText().trim());
            sub.setVendor(txtVendor.getText().trim());
            sub.setHarga(new BigDecimal(txtHarga.getText().trim()));
            sub.setTglExpired(new java.sql.Date(((Date) dateSpinner.getValue()).getTime()));
            sub.setStatus((String) cmbStatus.getSelectedItem());
            sub.setIdDept(deptIds[cmbDepartment.getSelectedIndex()]);

            if (subscriptionDAO.insert(sub)) {
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!", "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDB();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateData() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diupdate!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateForm())
            return;
        try {
            Subscription sub = new Subscription();
            sub.setId(Integer.parseInt(txtId.getText()));
            sub.setNamaLayanan(txtNamaLayanan.getText().trim());
            sub.setVendor(txtVendor.getText().trim());
            sub.setHarga(new BigDecimal(txtHarga.getText().trim()));
            sub.setTglExpired(new java.sql.Date(((Date) dateSpinner.getValue()).getTime()));
            sub.setStatus((String) cmbStatus.getSelectedItem());
            sub.setIdDept(deptIds[cmbDepartment.getSelectedIndex()]);

            if (subscriptionDAO.update(sub)) {
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate!", "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDB();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            if (subscriptionDAO.delete(id)) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!", "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDB();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateForm() {
        if (txtNamaLayanan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama layanan harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtVendor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vendor harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtHarga.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harga harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            new BigDecimal(txtHarga.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void loadDataFromDB() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        List<Object[]> data = subscriptionDAO.getAllSubscriptions();

        for (Object[] row : data) {
            Object[] tableRow = new Object[7];
            tableRow[0] = row[0];
            tableRow[1] = row[1];
            tableRow[2] = "Rp " + String.format("%,.0f", ((BigDecimal) row[3]).doubleValue());
            tableRow[3] = sdf.format((java.sql.Date) row[4]);
            tableRow[4] = row[5]; // Status
            tableRow[5] = row[6]; // Departemen
            tableRow[6] = ""; // Actions placeholder
            tableModel.addRow(tableRow);
        }
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 0));
        panel.setOpaque(false);

        // Search Bar
        JPanel searchCard = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        searchCard.setOpaque(false);
        searchCard.setBorder(new EmptyBorder(12, 16, 12, 16));
        searchCard.setPreferredSize(new Dimension(300, 48));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                filterTable();
            }
        });

        searchCard.add(searchIcon, BorderLayout.WEST);
        searchCard.add(txtSearch, BorderLayout.CENTER);

        // "Tambah Langganan Baru" button
        JButton btnTambah = new JButton("Tambah Langganan Baru") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20, 30, 50)); // Dark Navy
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnTambah.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setOpaque(false);
        btnTambah.setContentAreaFilled(false);
        btnTambah.setBorderPainted(false);
        btnTambah.setFocusPainted(false);
        btnTambah.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTambah.setBorder(new EmptyBorder(12, 20, 12, 20));
        btnTambah.addActionListener(e -> {
            clearForm();
            txtNamaLayanan.requestFocusInWindow();
        });

        panel.add(searchCard, BorderLayout.WEST);
        panel.add(btnTambah, BorderLayout.EAST);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(340, 0));

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Form Langganan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(30, 41, 59));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtId = new JTextField();
        txtId.setVisible(false);
        txtNamaLayanan = createMaterialField();
        txtVendor = createMaterialField();
        txtHarga = createMaterialField();

        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy"));
        dateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        cmbStatus = createMaterialCombo(new String[] { "active", "expired", "cancelled" });
        cmbDepartment = createMaterialCombo(new String[] { "IT Department", "Marketing", "Human Resources" });

        p.add(title);
        p.add(Box.createVerticalStrut(20));
        addFormRow(p, "Nama Layanan", txtNamaLayanan);
        addFormRow(p, "Vendor", txtVendor);
        addFormRow(p, "Harga (Rp)", txtHarga);

        // Row for Date and Status side by side
        JPanel dateStatusRow = new JPanel(new GridLayout(1, 2, 16, 0));
        dateStatusRow.setOpaque(false);
        dateStatusRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        dateStatusRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        datePanel.setOpaque(false);
        JLabel lblDate = new JLabel("Tgl Expired");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDate.setForeground(new Color(100, 116, 139));
        datePanel.add(lblDate);
        datePanel.add(Box.createVerticalStrut(6));
        datePanel.add(dateSpinner);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setOpaque(false);
        JLabel lblStatus = new JLabel("Status");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(100, 116, 139));
        statusPanel.add(lblStatus);
        statusPanel.add(Box.createVerticalStrut(6));
        statusPanel.add(cmbStatus);

        dateStatusRow.add(datePanel);
        dateStatusRow.add(statusPanel);
        p.add(dateStatusRow);
        p.add(Box.createVerticalStrut(16));

        addFormRow(p, "Departemen", cmbDepartment);
        p.add(Box.createVerticalStrut(16));

        // Single "Simpan" button as in reference
        btnSimpan = new JButton("Simpan") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(34, 160, 107)); // Green
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setOpaque(false);
        btnSimpan.setContentAreaFilled(false);
        btnSimpan.setBorderPainted(false);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimpan.setMaximumSize(new Dimension(120, 44));
        btnSimpan.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Hidden buttons for internal use (Edit, Delete, Clear)
        btnEdit = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnBersihkan = new JButton("Clear");
        btnBersihkan.addActionListener(e -> clearForm());

        p.add(btnSimpan);
        p.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(p);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        cardPanel.add(scrollPane, BorderLayout.CENTER);
        return cardPanel;
    }

    private JTextField createMaterialField() {
        JTextField tf = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(241, 245, 249));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                // Border
                g2.setColor(new Color(203, 213, 225));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setForeground(new Color(30, 41, 59));
        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(10, 14, 10, 14));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        tf.setCaretColor(new Color(0, 97, 164));
        return tf;
    }

    private JComboBox<String> createMaterialCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        cb.setBackground(new Color(241, 245, 249));
        return cb;
    }

    private void addFormRow(JPanel p, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(100, 116, 139));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(6));
        p.add(field);
        p.add(Box.createVerticalStrut(14));
    }

    private JPanel createTablePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("◇ Daftar Langganan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(30, 41, 59));

        String[] cols = { "ID", "Nama Layanan", "Harga", "Tgl Expired", "Status", "Departemen", "" };
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return c == 6; // Only actions column is "editable" for button clicks
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(48);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(209, 228, 255));
        table.setSelectionForeground(new Color(30, 41, 59));
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(30, 41, 59));

        // Header
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setForeground(new Color(100, 116, 139));
        table.getTableHeader().setBorder(null);
        table.getTableHeader().setPreferredSize(new Dimension(0, 44));

        // Custom renderer for "Status" column (index 4)
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusPillRenderer());

        // Custom renderer for "Actions" column (index 6)
        table.getColumnModel().getColumn(6).setCellRenderer(new ActionButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ActionButtonEditor(new JCheckBox()));
        table.getColumnModel().getColumn(6).setPreferredWidth(80);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1)
                populateForm();
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);
        sp.getViewport().setBackground(Color.WHITE);

        p.add(title, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    // Custom Cell Renderer for Status Pill
    class StatusPillRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            JLabel lbl = new JLabel(value != null ? value.toString() : "", SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));

            String status = value != null ? value.toString().toLowerCase() : "";
            if ("active".equals(status)) {
                lbl.setBackground(new Color(220, 252, 231));
                lbl.setForeground(new Color(22, 101, 52));
            } else if ("expired".equals(status)) {
                lbl.setBackground(new Color(254, 226, 226));
                lbl.setForeground(new Color(153, 27, 27));
            } else {
                lbl.setBackground(new Color(254, 243, 199));
                lbl.setForeground(new Color(133, 77, 14));
            }
            lbl.setBorder(new EmptyBorder(4, 10, 4, 10));

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
            panel.setOpaque(isSelected);
            if (isSelected)
                panel.setBackground(table.getSelectionBackground());
            else
                panel.setBackground(Color.WHITE);
            panel.add(lbl);
            return panel;
        }
    }

    // Custom Renderer for Action Buttons (Edit, Delete)
    class ActionButtonRenderer extends JPanel implements TableCellRenderer {
        JButton btnE = new JButton("✎");
        JButton btnD = new JButton("🗑");

        public ActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));
            setOpaque(true);
            btnE.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
            btnD.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
            btnE.setMargin(new Insets(2, 6, 2, 6));
            btnD.setMargin(new Insets(2, 6, 2, 6));
            btnE.setForeground(new Color(0, 97, 164));
            btnD.setForeground(new Color(185, 28, 28));
            add(btnE);
            add(btnD);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return this;
        }
    }

    // Custom Editor for Action Buttons
    class ActionButtonEditor extends DefaultCellEditor {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
        JButton btnE = new JButton("✎");
        JButton btnD = new JButton("🗑");

        public ActionButtonEditor(JCheckBox cb) {
            super(cb);
            panel.setOpaque(true);
            btnE.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
            btnD.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
            btnE.setMargin(new Insets(2, 6, 2, 6));
            btnD.setMargin(new Insets(2, 6, 2, 6));
            btnE.setForeground(new Color(0, 97, 164));
            btnD.setForeground(new Color(185, 28, 28));

            btnE.addActionListener(e -> {
                fireEditingStopped();
                updateData();
            });
            btnD.addActionListener(e -> {
                fireEditingStopped();
                hapusData();
            });

            panel.add(btnE);
            panel.add(btnD);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }
    }

    private void filterTable() {
        String s = txtSearch.getText().trim();
        sorter.setRowFilter(s.isEmpty() ? null : RowFilter.regexFilter("(?i)" + s, 1));
    }

    private void populateForm() {
        int r = table.convertRowIndexToModel(table.getSelectedRow());
        txtId.setText(tableModel.getValueAt(r, 0).toString());
        txtNamaLayanan.setText(tableModel.getValueAt(r, 1).toString());
        txtHarga.setText(tableModel.getValueAt(r, 2).toString().replaceAll("[^0-9]", ""));
        cmbStatus.setSelectedItem(tableModel.getValueAt(r, 4));

        String dept = tableModel.getValueAt(r, 5).toString();
        for (int i = 0; i < cmbDepartment.getItemCount(); i++) {
            if (cmbDepartment.getItemAt(i).equals(dept)) {
                cmbDepartment.setSelectedIndex(i);
                break;
            }
        }
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
