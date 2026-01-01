package view;

import model.User;
import util.MaterialTheme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Dashboard Utama - Matching User Reference Design
 */
public class MainFrame extends JFrame {

    private JPanel sidebarPanel;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JLabel lblPageTitle;
    private JButton btnLogout;

    private JButton btnDashboard;
    private JButton btnSubscriptions;
    private JButton btnDepartments;
    private JButton btnReports;
    private JButton activeButton;

    private User currentUser;

    private JPanel dashboardPanel;
    private SubscriptionView subscriptionPanel;
    private DepartmentView departmentPanel;
    private ReportView reportPanel;

    public MainFrame(User user) {
        this.currentUser = user;
        initComponents();
        setupMenuAccess();
        showDashboard();
    }

    private void initComponents() {
        setTitle("SaaS-Track - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(245, 247, 251));

        createSidebar();
        createHeader();
        createContentArea();

        mainContainer.add(sidebarPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(headerPanel, BorderLayout.NORTH);
        rightPanel.add(contentPanel, BorderLayout.CENTER);

        mainContainer.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainContainer);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                MaterialTheme.enableQualityRendering(g2);
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(260, 0));
        sidebarPanel.setBorder(new EmptyBorder(24, 20, 24, 20));

        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lblIcon = new JLabel("📊");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        JPanel logoText = new JPanel();
        logoText.setLayout(new BoxLayout(logoText, BoxLayout.Y_AXIS));
        logoText.setOpaque(false);

        JLabel lblLogo = new JLabel("SaaS-Track");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(new Color(30, 41, 59));

        JLabel lblVersion = new JLabel("Management System");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblVersion.setForeground(Color.GRAY);

        logoText.add(lblLogo);
        logoText.add(lblVersion);
        logoPanel.add(lblIcon);
        logoPanel.add(logoText);

        // Navigation Buttons - Matching Reference
        btnDashboard = createNavButton("⊞", "Dashboard", true);
        btnSubscriptions = createNavButton("↗", "Subscription", false);
        btnReports = createNavButton("✎", "Reports", false);
        btnDepartments = createNavButton("⚙", "Departments", false); // Using Settings icon as in image

        activeButton = btnDashboard;

        btnDashboard.addActionListener(e -> showDashboard());
        btnSubscriptions.addActionListener(e -> showSubscriptions());
        btnReports.addActionListener(e -> showReports());
        btnDepartments.addActionListener(e -> showDepartments());

        // User Profile (Gray Pill)
        JPanel userPanel = createUserInfoPanel();

        sidebarPanel.add(logoPanel);
        sidebarPanel.add(Box.createVerticalStrut(40));
        sidebarPanel.add(btnDashboard);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(btnSubscriptions);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(btnReports);
        sidebarPanel.add(Box.createVerticalStrut(5));
        sidebarPanel.add(btnDepartments);
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(userPanel);
    }

    private JButton createNavButton(String icon, String text, boolean isActive) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                MaterialTheme.enableQualityRendering(g2);

                if (this == activeButton) {
                    // Dark Navy active state from reference image
                    g2.setColor(new Color(20, 30, 50));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(241, 245, 249));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setLayout(new BoxLayout(btn, BoxLayout.X_AXIS));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setPreferredSize(new Dimension(220, 48));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBorder(new EmptyBorder(0, 16, 0, 16));

        // Icon with fixed width for alignment
        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        lblIcon.setPreferredSize(new Dimension(28, 24));
        lblIcon.setMaximumSize(new Dimension(28, 24));

        JLabel lblText = new JLabel(text);
        lblText.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btn.putClientProperty("lblText", lblText);
        btn.putClientProperty("lblIcon", lblIcon);

        btn.add(lblIcon);
        btn.add(Box.createRigidArea(new Dimension(12, 0)));
        btn.add(lblText);

        updateButtonColor(btn, isActive);

        return btn;
    }

    private void updateButtonColor(JButton btn, boolean isActive) {
        JLabel lblText = (JLabel) btn.getClientProperty("lblText");
        JLabel lblIcon = (JLabel) btn.getClientProperty("lblIcon");
        if (lblText != null && lblIcon != null) {
            if (isActive) {
                lblText.setForeground(Color.WHITE);
                lblIcon.setForeground(Color.WHITE);
            } else {
                lblText.setForeground(new Color(100, 116, 139));
                lblIcon.setForeground(new Color(100, 116, 139));
            }
        }
    }

    private void setActiveMenu(JButton newBtn) {
        if (activeButton != null)
            updateButtonColor(activeButton, false);
        activeButton = newBtn;
        updateButtonColor(activeButton, true);
        sidebarPanel.repaint();
    }

    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                MaterialTheme.enableQualityRendering(g2);
                g2.setColor(new Color(241, 245, 249));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        panel.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel lblName = new JLabel(currentUser.getUsername());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel lblRole = new JLabel(currentUser.getRole().toUpperCase());
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblRole.setForeground(Color.GRAY);

        infoPanel.add(lblName);
        infoPanel.add(lblRole);

        btnLogout = new JButton("→");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.add(avatar, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(btnLogout, BorderLayout.EAST);

        return panel;
    }

    private void createHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(new EmptyBorder(0, 40, 0, 40));

        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleContainer.setOpaque(false);

        JLabel btnCollapse = new JLabel("<");
        btnCollapse.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCollapse.setForeground(Color.GRAY);
        btnCollapse.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        lblPageTitle = new JLabel("Dashboard Overview");
        lblPageTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblPageTitle.setForeground(new Color(30, 41, 59));

        titleContainer.add(btnCollapse);
        titleContainer.add(lblPageTitle);

        JButton btnNotif = new JButton("🔔");
        btnNotif.setBackground(Color.WHITE);
        btnNotif.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        btnNotif.setFocusPainted(false);
        btnNotif.setContentAreaFilled(false);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(btnNotif, BorderLayout.EAST);
    }

    private void createContentArea() {
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(10, 40, 40, 40));

        dashboardPanel = createDashboardPanel();
        subscriptionPanel = new SubscriptionView();
        departmentPanel = new DepartmentView();
        reportPanel = new ReportView();

        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(subscriptionPanel, "subscriptions");
        contentPanel.add(departmentPanel, "departments");
        contentPanel.add(reportPanel, "reports");
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 24, 24));
        panel.setOpaque(false);

        Color blue = new Color(30, 86, 160);
        Color green = new Color(34, 160, 107);
        Color orange = new Color(220, 120, 20);
        Color purple = new Color(125, 60, 152);

        panel.add(createWaveCard("Total Langganan", "24", "Langganan Aktif", blue, "📊"));
        panel.add(createWaveCard("Aktif Sekarang", "18", "Layanan Berjalan", green, "✅"));
        panel.add(createWaveCard("Perlu Perhatian", "3", "Akan Expired", orange, "⏳"));
        panel.add(createWaveCard("Total Departemen", "5", "Unit Kerja", purple, "🏢"));

        return panel;
    }

    private JPanel createWaveCard(String title, String value, String subtitle, Color themeColor, String iconEmoji) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                MaterialTheme.enableQualityRendering(g2);

                int w = getWidth();
                int h = getHeight();

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, w, h, 24, 24);

                g2.setColor(themeColor);
                g2.fillRoundRect(0, 0, 8, h, 24, 24);
                g2.fillRect(4, 0, 6, h);

                drawWaveGraph(g2, w, h, themeColor);

                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);

        JPanel topP = new JPanel(new BorderLayout());
        topP.setOpaque(false);
        topP.setBorder(new EmptyBorder(24, 24, 10, 24));

        JPanel titleP = new JPanel(new GridLayout(3, 1, 0, 4));
        titleP.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(new Color(100, 116, 139));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblValue.setForeground(new Color(30, 41, 59));

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSub.setForeground(themeColor);

        titleP.add(lblTitle);
        titleP.add(lblValue);
        titleP.add(lblSub);

        JPanel iconP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        iconP.setOpaque(false);

        JPanel iconBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                MaterialTheme.enableQualityRendering(g2);
                g2.setColor(themeColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        iconBox.setPreferredSize(new Dimension(50, 50));
        iconBox.setLayout(new GridBagLayout());
        iconBox.setOpaque(false);

        JLabel lblIcon = new JLabel(iconEmoji);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        lblIcon.setForeground(Color.WHITE);
        iconBox.add(lblIcon);

        iconP.add(iconBox);

        topP.add(titleP, BorderLayout.CENTER);
        topP.add(iconP, BorderLayout.EAST);

        card.add(topP, BorderLayout.NORTH);

        return card;
    }

    private void drawWaveGraph(Graphics2D g2, int w, int h, Color color) {
        int bottomY = h;
        int startX = 8;
        int curveStartY = bottomY - 30;
        int width = w - startX;

        GeneralPath wave = new GeneralPath();
        wave.moveTo(startX, bottomY);
        wave.lineTo(startX, curveStartY);
        wave.curveTo(
                startX + width * 0.25, curveStartY - 40,
                startX + width * 0.25, curveStartY + 10,
                startX + width * 0.5, curveStartY - 20);
        wave.curveTo(
                startX + width * 0.75, curveStartY - 50,
                startX + width * 0.75, curveStartY,
                startX + width, bottomY - 60);
        wave.lineTo(startX + width, bottomY);
        wave.closePath();

        GradientPaint gp = new GradientPaint(
                0, bottomY - 80, new Color(color.getRed(), color.getGreen(), color.getBlue(), 80),
                0, bottomY, new Color(255, 255, 255, 0));
        g2.setPaint(gp);
        g2.fill(wave);

        g2.setColor(color);
        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        GeneralPath line = new GeneralPath();
        line.moveTo(startX, curveStartY);
        line.curveTo(
                startX + width * 0.25, curveStartY - 40,
                startX + width * 0.25, curveStartY + 10,
                startX + width * 0.5, curveStartY - 20);
        line.curveTo(
                startX + width * 0.75, curveStartY - 50,
                startX + width * 0.75, curveStartY,
                startX + width, bottomY - 60);
        g2.draw(line);
    }

    private void setupMenuAccess() {
        if (currentUser.isOperator()) {
            btnDepartments.setEnabled(false);
            btnDepartments.setVisible(false);
        }
    }

    private void showDashboard() {
        setActiveMenu(btnDashboard);
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "dashboard");
        lblPageTitle.setText("Dashboard Overview");
    }

    private void showSubscriptions() {
        setActiveMenu(btnSubscriptions);
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "subscriptions");
        lblPageTitle.setText("Subscription Management");
    }

    private void showDepartments() {
        if (currentUser.isAdmin()) {
            setActiveMenu(btnDepartments);
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "departments");
            lblPageTitle.setText("Department Data");
        }
    }

    private void showReports() {
        setActiveMenu(btnReports);
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "reports");
        lblPageTitle.setText("Analytics & Reports");
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
