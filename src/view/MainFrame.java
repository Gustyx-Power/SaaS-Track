package view;

import model.User;
import util.MaterialTheme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dashboard Utama dengan Material Design 3
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
        setSize(1300, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1100, 700));

        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(MaterialTheme.BACKGROUND);

        createSidebar();
        createHeader();
        createContentArea();

        mainContainer.add(sidebarPanel, BorderLayout.WEST);
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainContainer);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MaterialTheme.SURFACE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Right border
                g2.setColor(MaterialTheme.OUTLINE_VARIANT);
                g2.fillRect(getWidth() - 1, 0, 1, getHeight());
                g2.dispose();
            }
        };
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(280, 0));
        sidebarPanel.setBorder(new EmptyBorder(24, 16, 24, 16));

        // Logo section
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));

        JLabel lblIcon = new JLabel("📊");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        JPanel logoText = new JPanel();
        logoText.setLayout(new BoxLayout(logoText, BoxLayout.Y_AXIS));
        logoText.setOpaque(false);

        JLabel lblLogo = new JLabel("SaaS-Track");
        lblLogo.setFont(MaterialTheme.TITLE_LARGE);
        lblLogo.setForeground(MaterialTheme.PRIMARY);

        JLabel lblVersion = new JLabel("v1.0.0");
        lblVersion.setFont(MaterialTheme.LABEL_SMALL);
        lblVersion.setForeground(MaterialTheme.ON_SURFACE_VARIANT);

        logoText.add(lblLogo);
        logoText.add(lblVersion);

        logoPanel.add(lblIcon);
        logoPanel.add(logoText);

        // Menu section
        JLabel lblMenu = new JLabel("MENU");
        lblMenu.setFont(MaterialTheme.LABEL_SMALL);
        lblMenu.setForeground(MaterialTheme.ON_SURFACE_VARIANT);
        lblMenu.setBorder(new EmptyBorder(0, 16, 8, 0));
        lblMenu.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnDashboard = createNavButton("📊", "Dashboard", true);
        btnSubscriptions = createNavButton("📋", "Data Langganan", false);
        btnDepartments = createNavButton("🏢", "Data Departemen", false);
        btnReports = createNavButton("📈", "Laporan", false);

        activeButton = btnDashboard;

        btnDashboard.addActionListener(e -> showDashboard());
        btnSubscriptions.addActionListener(e -> showSubscriptions());
        btnDepartments.addActionListener(e -> showDepartments());
        btnReports.addActionListener(e -> showReports());

        // User info at bottom
        JPanel userPanel = createUserInfoPanel();

        sidebarPanel.add(logoPanel);
        sidebarPanel.add(Box.createVerticalStrut(32));
        sidebarPanel.add(lblMenu);
        sidebarPanel.add(Box.createVerticalStrut(8));
        sidebarPanel.add(btnDashboard);
        sidebarPanel.add(Box.createVerticalStrut(4));
        sidebarPanel.add(btnSubscriptions);
        sidebarPanel.add(Box.createVerticalStrut(4));
        sidebarPanel.add(btnDepartments);
        sidebarPanel.add(Box.createVerticalStrut(4));
        sidebarPanel.add(btnReports);
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(userPanel);
    }

    private JButton createNavButton(String icon, String text, boolean isActive) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (this == activeButton) {
                    g2.setColor(MaterialTheme.SECONDARY_CONTAINER);
                } else if (getModel().isRollover()) {
                    g2.setColor(MaterialTheme.SURFACE_VARIANT);
                } else {
                    g2.setColor(MaterialTheme.SURFACE);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 0));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        btn.setPreferredSize(new Dimension(248, 56));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        JLabel lblText = new JLabel(text);
        lblText.setFont(MaterialTheme.LABEL_LARGE);
        lblText.setForeground(isActive ? MaterialTheme.ON_SECONDARY_CONTAINER : MaterialTheme.ON_SURFACE_VARIANT);

        btn.add(lblIcon);
        btn.add(lblText);

        return btn;
    }

    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MaterialTheme.SURFACE_VARIANT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        panel.setLayout(new BorderLayout(12, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        panel.setBorder(new EmptyBorder(12, 16, 12, 16));

        // Avatar
        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        // User info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel lblName = new JLabel(currentUser.getUsername());
        lblName.setFont(MaterialTheme.LABEL_LARGE);
        lblName.setForeground(MaterialTheme.ON_SURFACE);

        JLabel lblRole = new JLabel(currentUser.getRole().toUpperCase());
        lblRole.setFont(MaterialTheme.LABEL_SMALL);
        lblRole.setForeground(MaterialTheme.PRIMARY);

        infoPanel.add(lblName);
        infoPanel.add(lblRole);

        // Logout button
        btnLogout = new JButton("↪") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(MaterialTheme.ERROR_CONTAINER);
                }
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogout.setForeground(MaterialTheme.ERROR);
        btnLogout.setOpaque(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new Dimension(40, 40));
        btnLogout.setToolTipText("Logout");

        panel.add(avatar, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(btnLogout, BorderLayout.EAST);

        return panel;
    }

    private void createHeader() {
        headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(MaterialTheme.SURFACE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(MaterialTheme.OUTLINE_VARIANT);
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 72));
        headerPanel.setBorder(new EmptyBorder(0, 32, 0, 32));

        lblPageTitle = new JLabel("Dashboard");
        lblPageTitle.setFont(MaterialTheme.HEADLINE_SMALL);
        lblPageTitle.setForeground(MaterialTheme.ON_SURFACE);

        headerPanel.add(lblPageTitle, BorderLayout.WEST);
    }

    private void createContentArea() {
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(MaterialTheme.BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(24, 32, 24, 32));

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

        panel.add(
                createStatCard("📋", "Total Langganan", "24", MaterialTheme.PRIMARY, MaterialTheme.PRIMARY_CONTAINER));
        panel.add(createStatCard("✅", "Langganan Aktif", "18", MaterialTheme.SUCCESS, MaterialTheme.SUCCESS_CONTAINER));
        panel.add(createStatCard("⚠️", "Akan Expired", "3", MaterialTheme.WARNING, new Color(255, 243, 224)));
        panel.add(createStatCard("🏢", "Total Departemen", "5", MaterialTheme.TERTIARY,
                MaterialTheme.TERTIARY_CONTAINER));

        return panel;
    }

    private JPanel createStatCard(String icon, String title, String value, Color accentColor, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout(16, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(32, 32, 32, 32));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(MaterialTheme.DISPLAY_SMALL);
        lblValue.setForeground(accentColor);
        lblValue.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(MaterialTheme.BODY_LARGE);
        lblTitle.setForeground(MaterialTheme.ON_SURFACE_VARIANT);
        lblTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);

        textPanel.add(lblValue);
        textPanel.add(lblTitle);

        card.add(lblIcon, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.EAST);

        return card;
    }

    private void setActiveMenu(JButton btn) {
        activeButton = btn;
        sidebarPanel.repaint();
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
        lblPageTitle.setText("Dashboard");
    }

    private void showSubscriptions() {
        setActiveMenu(btnSubscriptions);
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "subscriptions");
        lblPageTitle.setText("Data Langganan");
    }

    private void showDepartments() {
        if (currentUser.isAdmin()) {
            setActiveMenu(btnDepartments);
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "departments");
            lblPageTitle.setText("Data Departemen");
        }
    }

    private void showReports() {
        setActiveMenu(btnReports);
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "reports");
        lblPageTitle.setText("Laporan");
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
