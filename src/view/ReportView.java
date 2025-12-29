package view;

import dao.SubscriptionDAO;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

/**
 * View untuk menampilkan laporan dan statistik langganan
 */
public class ReportView extends JPanel {

    private JTable tableExpiring;
    private DefaultTableModel tableModel;
    private JLabel lblTotalSubs, lblActiveSubs, lblExpiredSubs, lblTotalCost;
    private JButton btnRefresh, btnExport;
    private SubscriptionDAO subscriptionDAO;

    private static final Color PRIMARY = new Color(41, 128, 185);
    private static final Color SUCCESS = new Color(39, 174, 96);
    private static final Color WARNING = new Color(241, 196, 15);
    private static final Color DANGER = new Color(231, 76, 60);

    public ReportView() {
        subscriptionDAO = new SubscriptionDAO();
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);

        add(createStatsPanel(), BorderLayout.NORTH);
        add(createExpiringPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadData();
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setOpaque(false);

        lblTotalSubs = new JLabel("0");
        lblActiveSubs = new JLabel("0");
        lblExpiredSubs = new JLabel("0");
        lblTotalCost = new JLabel("Rp 0");

        panel.add(createStatCard("📋 Total Langganan", lblTotalSubs, PRIMARY));
        panel.add(createStatCard("✅ Aktif", lblActiveSubs, SUCCESS));
        panel.add(createStatCard("❌ Expired", lblExpiredSubs, DANGER));
        panel.add(createStatCard("💰 Total Biaya/Bulan", lblTotalCost, new Color(155, 89, 182)));

        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(Color.GRAY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createExpiringPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel lblTitle = new JLabel("⚠️ Langganan Akan Expired (30 Hari Kedepan)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        String[] cols = { "ID", "Nama Layanan", "Vendor", "Harga", "Tgl Expired", "Departemen", "Sisa Hari" };
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tableExpiring = new JTable(tableModel);
        tableExpiring.setRowHeight(32);
        tableExpiring.setSelectionBackground(new Color(41, 128, 185, 50));
        tableExpiring.getTableHeader().setBackground(new Color(241, 196, 15));
        tableExpiring.getTableHeader().setForeground(Color.BLACK);
        tableExpiring.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Color code for remaining days
        tableExpiring.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                if (!isSelected && value != null) {
                    int days = Integer.parseInt(value.toString().replace(" hari", ""));
                    if (days <= 7) {
                        setBackground(new Color(231, 76, 60, 60));
                        setForeground(new Color(192, 57, 43));
                    } else if (days <= 14) {
                        setBackground(new Color(241, 196, 15, 60));
                        setForeground(new Color(156, 136, 0));
                    } else {
                        setBackground(new Color(39, 174, 96, 40));
                        setForeground(new Color(39, 174, 96));
                    }
                }
                return this;
            }
        });

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableExpiring), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);

        btnRefresh = createButton("🔄 Refresh", PRIMARY);
        btnExport = createButton("📥 Export Excel", SUCCESS);

        btnRefresh.addActionListener(e -> loadData());
        btnExport.addActionListener(e -> exportReport());

        panel.add(btnRefresh);
        panel.add(btnExport);

        return panel;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        return btn;
    }

    public void loadData() {
        List<Object[]> allData = subscriptionDAO.getAllSubscriptions();

        int total = allData.size();
        int active = 0;
        int expired = 0;
        BigDecimal totalCost = BigDecimal.ZERO;

        tableModel.setRowCount(0);

        java.util.Date today = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        java.util.Date thirtyDaysLater = cal.getTime();

        for (Object[] row : allData) {
            String status = (String) row[5];
            BigDecimal harga = (BigDecimal) row[3];
            java.sql.Date tglExpired = (java.sql.Date) row[4];

            if ("active".equalsIgnoreCase(status)) {
                active++;
                totalCost = totalCost.add(harga);

                // Check if expiring within 30 days
                if (tglExpired != null && tglExpired.after(today) && tglExpired.before(thirtyDaysLater)) {
                    long diffMillis = tglExpired.getTime() - today.getTime();
                    long diffDays = diffMillis / (1000 * 60 * 60 * 24);

                    tableModel.addRow(new Object[] {
                            row[0], row[1], row[2],
                            formatCurrency(harga),
                            formatDate(tglExpired),
                            row[6],
                            diffDays + " hari"
                    });
                }
            } else if ("expired".equalsIgnoreCase(status)) {
                expired++;
            }
        }

        lblTotalSubs.setText(String.valueOf(total));
        lblActiveSubs.setText(String.valueOf(active));
        lblExpiredSubs.setText(String.valueOf(expired));
        lblTotalCost.setText(formatCurrency(totalCost));
    }

    private String formatCurrency(BigDecimal value) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
        return "Rp " + nf.format(value);
    }

    private String formatDate(java.sql.Date date) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    private void exportReport() {
        subscriptionDAO.exportToExcelWithDialog(tableExpiring);
    }
}
