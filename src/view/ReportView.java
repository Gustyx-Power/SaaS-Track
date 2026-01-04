package view;

import dao.SubscriptionDAO;
import util.BirtReportGenerator;
import util.MaterialTheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

/**
 * ReportView dengan Material Design 3
 */
public class ReportView extends JPanel {

    private JTable tableExpiring;
    private DefaultTableModel tableModel;
    private JLabel lblTotalSubs, lblActiveSubs, lblExpiredSubs, lblTotalCost;
    private JButton btnRefresh, btnExport, btnExportPDF, btnExportHTML;
    private SubscriptionDAO subscriptionDAO;

    public ReportView() {
        subscriptionDAO = new SubscriptionDAO();
        setLayout(new BorderLayout(24, 24));
        setOpaque(false);

        add(createStatsPanel(), BorderLayout.NORTH);
        add(createExpiringPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadData();
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 16, 0));
        panel.setOpaque(false);

        lblTotalSubs = new JLabel("0");
        lblActiveSubs = new JLabel("0");
        lblExpiredSubs = new JLabel("0");
        lblTotalCost = new JLabel("Rp 0");

        panel.add(createStatCard("📋", "Total Langganan", lblTotalSubs, MaterialTheme.PRIMARY,
                MaterialTheme.PRIMARY_CONTAINER));
        panel.add(createStatCard("✅", "Aktif", lblActiveSubs, MaterialTheme.SUCCESS, MaterialTheme.SUCCESS_CONTAINER));
        panel.add(createStatCard("❌", "Expired", lblExpiredSubs, MaterialTheme.ERROR, MaterialTheme.ERROR_CONTAINER));
        panel.add(createStatCard("💰", "Total Biaya", lblTotalCost, MaterialTheme.TERTIARY,
                MaterialTheme.TERTIARY_CONTAINER));

        return panel;
    }

    private JPanel createStatCard(String icon, String title, JLabel valueLabel, Color accent, Color container) {
        JPanel card = new JPanel(new BorderLayout(12, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(container);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(MaterialTheme.LABEL_MEDIUM);
        lblTitle.setForeground(MaterialTheme.ON_SURFACE_VARIANT);

        valueLabel.setFont(MaterialTheme.HEADLINE_MEDIUM);
        valueLabel.setForeground(accent);

        textPanel.add(lblTitle);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(valueLabel);

        card.add(lblIcon, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createExpiringPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MaterialTheme.SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel lblTitle = new JLabel("⚠️ Semua Data Expired");
        lblTitle.setFont(MaterialTheme.TITLE_MEDIUM);
        lblTitle.setForeground(MaterialTheme.ON_SURFACE);

        String[] cols = { "ID", "Nama Layanan", "Vendor", "Harga", "Tgl Expired", "Departemen", "Hari Lalu" };
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tableExpiring = new JTable(tableModel);
        MaterialTheme.styleTable(tableExpiring);

        tableExpiring.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                setFont(MaterialTheme.LABEL_LARGE);
                if (!isSelected && value != null) {
                    String v = value.toString();
                    int days = Integer.parseInt(v.replace(" hari", ""));
                    if (days >= 14) {
                        // Expired lebih dari 2 minggu - merah
                        setBackground(MaterialTheme.ERROR_CONTAINER);
                        setForeground(MaterialTheme.ERROR);
                    } else if (days >= 7) {
                        // Expired 1-2 minggu - kuning/warning
                        setBackground(new Color(255, 243, 224));
                        setForeground(MaterialTheme.WARNING);
                    } else {
                        // Baru expired kurang dari seminggu - hijau
                        setBackground(MaterialTheme.SUCCESS_CONTAINER);
                        setForeground(MaterialTheme.SUCCESS);
                    }
                }
                return this;
            }
        });

        JScrollPane sp = new JScrollPane(tableExpiring);
        sp.setBorder(null);
        sp.getViewport().setBackground(MaterialTheme.SURFACE);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panel.setOpaque(false);

        btnRefresh = createMaterialBtn("🔄 Refresh", MaterialTheme.SECONDARY_CONTAINER,
                MaterialTheme.ON_SECONDARY_CONTAINER);
        btnExportPDF = createMaterialBtn("📄 Export PDF", MaterialTheme.ERROR, MaterialTheme.ON_PRIMARY);
        btnExportHTML = createMaterialBtn("🌐 Export HTML", MaterialTheme.TERTIARY, MaterialTheme.ON_PRIMARY);
        btnExport = createMaterialBtn("📥 Export Excel", MaterialTheme.PRIMARY, MaterialTheme.ON_PRIMARY);

        btnRefresh.addActionListener(e -> loadData());
        btnExportPDF.addActionListener(e -> BirtReportGenerator.generatePDFWithDialog());
        btnExportHTML.addActionListener(e -> BirtReportGenerator.generateHTMLWithDialog());
        btnExport.addActionListener(e -> exportReport());

        panel.add(btnRefresh);
        panel.add(btnExportPDF);
        panel.add(btnExportHTML);
        panel.add(btnExport);

        return panel;
    }

    private JButton createMaterialBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
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
        btn.setFont(MaterialTheme.LABEL_LARGE);
        btn.setForeground(fg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 24, 12, 24));
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

        for (Object[] row : allData) {
            String status = (String) row[5];
            BigDecimal harga = (BigDecimal) row[3];
            java.sql.Date tglExpired = (java.sql.Date) row[4];

            if ("active".equalsIgnoreCase(status)) {
                active++;
                totalCost = totalCost.add(harga);
            } else if ("expired".equalsIgnoreCase(status)) {
                expired++;

                // Tampilkan semua subscription yang expired
                if (tglExpired != null) {
                    long diffDays = (today.getTime() - tglExpired.getTime()) / (1000 * 60 * 60 * 24);
                    tableModel.addRow(new Object[] { row[0], row[1], row[2], formatCurrency(harga),
                            formatDate(tglExpired), row[6], diffDays + " hari" });
                }
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
        return new java.text.SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    private void exportReport() {
        subscriptionDAO.exportToExcelWithDialog(tableExpiring);
    }
}
