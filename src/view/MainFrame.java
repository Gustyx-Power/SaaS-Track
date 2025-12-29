package view;

import model.User;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dashboard Utama (Main Frame) untuk aplikasi SaaS-Track
 * Memiliki Sidebar, Header, dan Content Area
 */
public class MainFrame extends JFrame {

    // Components
    private JPanel sidebarPanel;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JLabel lblUsername;
    private JLabel lblRole;
    private JButton btnLogout;

    // Menu buttons
    private JButton btnDashboard;
    private JButton btnSubscriptions;
    private JButton btnDepartments;
    private JButton btnReports;

    // Current user
    private User currentUser;

    // Constants
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SIDEBAR_COLOR = new Color(44, 62, 80);
    private static final Color SIDEBAR_HOVER = new Color(52, 73, 94);
    private static final Color HEADER_COLOR = Color.WHITE;
    private static final Color CONTENT_COLOR = new Color(236, 240, 241);
    private static final Font MENU_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);

    // Content panels
    private JPanel dashboardPanel;
    private SubscriptionView subscriptionPanel;
    private DepartmentView departmentPanel;
    private JPanel reportPanel;

    public MainFrame(User user) {
        this.currentUser = user;
        initComponents();
        setupMenuAccess();
        showDashboard(); // Default view
    }

    private void initComponents() {
        // Frame settings
        setTitle("SaaS-Track - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));

        // Main layout
        setLayout(new BorderLayout());

        // Create components
        createSidebar();
        createHeader();
        createContentArea();

        // Add to frame
        add(sidebarPanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_COLOR);
        sidebarPanel.setPreferredSize(new Dimension(220, 0));
        sidebarPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(new Color(34, 49, 63));
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        logoPanel.setPreferredSize(new Dimension(220, 70));

        JLabel lblLogo = new JLabel("SaaS-Track");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(Color.WHITE);
        logoPanel.add(lblLogo);

        // Menu buttons
        btnDashboard = createMenuButton("📊  Dashboard", true);
        btnSubscriptions = createMenuButton("📋  Data Langganan", false);
        btnDepartments = createMenuButton("🏢  Data Departemen", false);
        btnReports = createMenuButton("📈  Laporan", false);

        // Add menu actions
        btnDashboard.addActionListener(e -> showDashboard());
        btnSubscriptions.addActionListener(e -> showSubscriptions());
        btnDepartments.addActionListener(e -> showDepartments());
        btnReports.addActionListener(e -> showReports());

        // Add components
        sidebarPanel.add(logoPanel);
        sidebarPanel.add(Box.createVerticalStrut(20));
        sidebarPanel.add(btnDashboard);
        sidebarPanel.add(btnSubscriptions);
        sidebarPanel.add(btnDepartments);
        sidebarPanel.add(btnReports);
        sidebarPanel.add(Box.createVerticalGlue());

        // Version label
        JLabel lblVersion = new JLabel("v1.0.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVersion.setForeground(new Color(127, 140, 141));
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(lblVersion);
        sidebarPanel.add(Box.createVerticalStrut(15));
    }

    private JButton createMenuButton(String text, boolean isActive) {
        JButton button = new JButton(text);
        button.setFont(MENU_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(isActive ? PRIMARY_COLOR : SIDEBAR_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setBorder(new EmptyBorder(12, 25, 12, 20));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.getBackground().equals(PRIMARY_COLOR)) {
                    button.setBackground(SIDEBAR_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.getBackground().equals(PRIMARY_COLOR)) {
                    button.setBackground(SIDEBAR_COLOR);
                }
            }
        });

        return button;
    }

    private void setActiveMenu(JButton activeButton) {
        JButton[] buttons = { btnDashboard, btnSubscriptions, btnDepartments, btnReports };
        for (JButton btn : buttons) {
            btn.setBackground(SIDEBAR_COLOR);
        }
        activeButton.setBackground(PRIMARY_COLOR);
    }

    private void createHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                new EmptyBorder(10, 20, 10, 20)));

        // Left side - Page title
        JLabel lblPageTitle = new JLabel("Dashboard");
        lblPageTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPageTitle.setForeground(new Color(44, 62, 80));

        // Right side - User info and logout
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false);

        // User info
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);

        lblUsername = new JLabel(currentUser.getUsername());
        lblUsername.setFont(HEADER_FONT);
        lblUsername.setForeground(new Color(44, 62, 80));
        lblUsername.setAlignmentX(Component.RIGHT_ALIGNMENT);

        lblRole = new JLabel(currentUser.getRole().toUpperCase());
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRole.setForeground(PRIMARY_COLOR);
        lblRole.setAlignmentX(Component.RIGHT_ALIGNMENT);

        userInfoPanel.add(lblUsername);
        userInfoPanel.add(lblRole);

        // Logout button
        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setBorder(new EmptyBorder(8, 15, 8, 15));

        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setBackground(new Color(192, 57, 43));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setBackground(new Color(231, 76, 60));
            }
        });

        userPanel.add(userInfoPanel);
        userPanel.add(btnLogout);

        headerPanel.add(lblPageTitle, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
    }

    private void createContentArea() {
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(CONTENT_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create panels
        dashboardPanel = createDashboardPanel();
        subscriptionPanel = new SubscriptionView();
        departmentPanel = new DepartmentView();
        reportPanel = createReportPanel();

        // Add to card layout
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(subscriptionPanel, "subscriptions");
        contentPanel.add(departmentPanel, "departments");
        contentPanel.add(reportPanel, "reports");
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setOpaque(false);

        // Stats cards
        panel.add(createStatCard("Total Langganan", "24", "📋", new Color(41, 128, 185)));
        panel.add(createStatCard("Langganan Aktif", "18", "✅", new Color(39, 174, 96)));
        panel.add(createStatCard("Akan Expired", "3", "⚠️", new Color(241, 196, 15)));
        panel.add(createStatCard("Total Departemen", "5", "🏢", new Color(155, 89, 182)));

        return panel;
    }

    private JPanel createStatCard(String title, String value, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(25, 25, 25, 25)));

        // Left side - Icon
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIcon.setVerticalAlignment(SwingConstants.CENTER);

        // Right side - Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValue.setForeground(color);
        lblValue.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(Color.GRAY);
        lblTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);

        infoPanel.add(lblValue);
        infoPanel.add(lblTitle);

        card.add(lblIcon, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.EAST);

        return card;
    }

    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel lblComingSoon = new JLabel("📊 Fitur Laporan - Coming Soon");
        lblComingSoon.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblComingSoon.setForeground(Color.GRAY);
        lblComingSoon.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(lblComingSoon, BorderLayout.CENTER);

        return panel;
    }

    private void setupMenuAccess() {
        // Operator tidak bisa akses menu tertentu
        if (currentUser.isOperator()) {
            // Sembunyikan atau disable menu yang tidak diizinkan
            btnDepartments.setEnabled(false);
            btnDepartments.setForeground(new Color(127, 140, 141));
        }
    }

    // Navigation methods
    private void showDashboard() {
        setActiveMenu(btnDashboard);
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "dashboard");
        updateHeaderTitle("Dashboard");
    }

    private void showSubscriptions() {
        setActiveMenu(btnSubscriptions);
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "subscriptions");
        updateHeaderTitle("Data Langganan");
    }

    private void showDepartments() {
        if (currentUser.isAdmin()) {
            setActiveMenu(btnDepartments);
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "departments");
            updateHeaderTitle("Data Departemen");
        }
    }

    private void showReports() {
        setActiveMenu(btnReports);
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "reports");
        updateHeaderTitle("Laporan");
    }

    private void updateHeaderTitle(String title) {
        // Find and update the title label in header
        for (Component c : headerPanel.getComponents()) {
            if (c instanceof JLabel) {
                ((JLabel) c).setText(title);
                break;
            }
        }
    }

    // Getters
    public JButton getBtnLogout() {
        return btnLogout;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public SubscriptionView getSubscriptionPanel() {
        return subscriptionPanel;
    }

    public DepartmentView getDepartmentPanel() {
        return departmentPanel;
    }

    // Main method for testing
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            User testUser = new User("admin", "admin123", "admin");
            new MainFrame(testUser).setVisible(true);
        });
    }
}
