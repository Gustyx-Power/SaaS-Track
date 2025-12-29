package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * View untuk mengelola data departemen
 * Layout: BorderLayout - form di West, JTable di Center, search di North
 */
public class DepartmentView extends JPanel {

    private JTextField txtId, txtNamaDept, txtBudgetLimit, txtSearch;
    private JButton btnSimpan, btnEdit, btnHapus, btnBersihkan;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private static final Color PRIMARY = new Color(41, 128, 185);
    private static final Color SUCCESS = new Color(39, 174, 96);
    private static final Color DANGER = new Color(231, 76, 60);

    public DepartmentView() {
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

        p.add(new JLabel("🔍 Cari Departemen:"), BorderLayout.WEST);
        p.add(txtSearch, BorderLayout.CENTER);
        return p;
    }

    private JPanel createFormPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setPreferredSize(new Dimension(280, 0));
        p.setBorder(new CompoundBorder(new LineBorder(new Color(220, 220, 220)), new EmptyBorder(20, 20, 20, 20)));

        JLabel title = new JLabel("Form Departemen");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(20));

        txtId = new JTextField();
        txtId.setVisible(false);
        txtNamaDept = createTextField();
        txtBudgetLimit = createTextField();

        addFormRow(p, "Nama Departemen *", txtNamaDept);
        addFormRow(p, "Budget Limit (Rp) *", txtBudgetLimit);

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

        String[] cols = { "ID", "Nama Departemen", "Budget Limit", "Created At" };
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

        JLabel lbl = new JLabel("🏢 Daftar Departemen");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setBorder(new EmptyBorder(0, 0, 15, 0));

        p.add(lbl, BorderLayout.NORTH);
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
        txtNamaDept.setText(tableModel.getValueAt(r, 1).toString());
        txtBudgetLimit.setText(tableModel.getValueAt(r, 2).toString().replaceAll("[^0-9]", ""));
    }

    public void clearForm() {
        txtId.setText("");
        txtNamaDept.setText("");
        txtBudgetLimit.setText("");
        table.clearSelection();
    }

    private void loadSampleData() {
        tableModel.addRow(new Object[] { 1, "IT Department", "Rp 50.000.000", "2025-12-30" });
        tableModel.addRow(new Object[] { 2, "Marketing", "Rp 30.000.000", "2025-12-30" });
        tableModel.addRow(new Object[] { 3, "Human Resources", "Rp 20.000.000", "2025-12-30" });
    }

    // Getters
    public JTextField getTxtNamaDept() {
        return txtNamaDept;
    }

    public JTextField getTxtBudgetLimit() {
        return txtBudgetLimit;
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
