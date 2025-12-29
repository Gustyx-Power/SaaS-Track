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
 * SubscriptionView dengan Material Design 3 dan CRUD functionality
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
    private int[] deptIds = { 1, 2, 3 }; // ID untuk IT Department, Marketing, Human Resources

    public SubscriptionView() {
        subscriptionDAO = new SubscriptionDAO();
        setLayout(new BorderLayout(24, 24));
        setOpaque(false);
        add(createSearchPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
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
            tableRow[0] = row[0]; // id
            tableRow[1] = row[1]; // nama_layanan
            tableRow[2] = row[2]; // vendor
            tableRow[3] = "Rp " + String.format("%,.0f", ((BigDecimal) row[3]).doubleValue()); // harga
            tableRow[4] = sdf.format((java.sql.Date) row[4]); // tgl_expired
            tableRow[5] = row[5]; // status
            tableRow[6] = row[6]; // nama_dept
            tableModel.addRow(tableRow);
        }
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

        String dept = tableModel.getValueAt(r, 6).toString();
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
