package view;

import dao.DepartmentDAO;
import model.Department;
import util.MaterialTheme;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * DepartmentView dengan Material Design 3 dan CRUD functionality
 */
public class DepartmentView extends JPanel {

    private JTextField txtId, txtNamaDept, txtBudgetLimit, txtSearch;
    private JButton btnSimpan, btnEdit, btnHapus, btnBersihkan;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private DepartmentDAO departmentDAO;

    public DepartmentView() {
        departmentDAO = new DepartmentDAO();
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
            Department dept = new Department();
            dept.setNamaDept(txtNamaDept.getText().trim());
            dept.setBudgetLimit(new BigDecimal(txtBudgetLimit.getText().trim()));

            if (departmentDAO.insert(dept)) {
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
            Department dept = new Department();
            dept.setId(Integer.parseInt(txtId.getText()));
            dept.setNamaDept(txtNamaDept.getText().trim());
            dept.setBudgetLimit(new BigDecimal(txtBudgetLimit.getText().trim()));

            if (departmentDAO.update(dept)) {
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
            if (departmentDAO.delete(id)) {
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
        if (txtNamaDept.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama departemen harus diisi!", "Validasi",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtBudgetLimit.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Budget limit harus diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            new BigDecimal(txtBudgetLimit.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Budget limit harus berupa angka!", "Validasi",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void loadDataFromDB() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        List<Object[]> data = departmentDAO.getAllDepartments();

        for (Object[] row : data) {
            Object[] tableRow = new Object[4];
            tableRow[0] = row[0];
            tableRow[1] = row[1];
            tableRow[2] = "Rp " + String.format("%,.0f", ((BigDecimal) row[2]).doubleValue());
            tableRow[3] = row[3] != null ? sdf.format((java.sql.Timestamp) row[3]) : "-";
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

        JLabel title = new JLabel("Form Departemen");
        title.setFont(MaterialTheme.TITLE_MEDIUM);
        title.setForeground(MaterialTheme.ON_SURFACE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtId = new JTextField();
        txtId.setVisible(false);
        txtNamaDept = createMaterialField();
        txtBudgetLimit = createMaterialField();

        p.add(title);
        p.add(Box.createVerticalStrut(24));
        addFormRow(p, "Nama Departemen", txtNamaDept);
        addFormRow(p, "Budget Limit (Rp)", txtBudgetLimit);
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

        JLabel title = new JLabel("🏢 Daftar Departemen");
        title.setFont(MaterialTheme.TITLE_MEDIUM);
        title.setForeground(MaterialTheme.ON_SURFACE);

        String[] cols = { "ID", "Nama Departemen", "Budget Limit", "Created At" };
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
        txtNamaDept.setText(tableModel.getValueAt(r, 1).toString());
        txtBudgetLimit.setText(tableModel.getValueAt(r, 2).toString().replaceAll("[^0-9]", ""));
    }

    public void clearForm() {
        txtId.setText("");
        txtNamaDept.setText("");
        txtBudgetLimit.setText("");
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
