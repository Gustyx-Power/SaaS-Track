package view;

import util.MaterialTheme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Form Login dengan Modern UI (Glassmorphism & Split Layout)
 */
public class LoginView extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblMessage;

    // Custom colors for this view (Monet Blue Gradient)
    private final Color GRADIENT_START = new Color(0, 97, 164); // Primary Blue
    private final Color GRADIENT_END = new Color(24, 119, 242); // Brighter Blue

    public LoginView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("SaaS-Track - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setUndecorated(true);
        // Rounded corners for the whole window
        setShape(new RoundRectangle2D.Double(0, 0, 900, 550, MaterialTheme.CORNER_RADIUS_EXTRA_LARGE,
                MaterialTheme.CORNER_RADIUS_EXTRA_LARGE));

        // Main Container
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(Color.WHITE);

        // Left Panel (Branding / Gradient)
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                MaterialTheme.enableQualityRendering(g2);

                // Diagonal Gradient
                GradientPaint gp = new GradientPaint(0, 0, GRADIENT_START, getWidth(), getHeight(), GRADIENT_END);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles/shapes
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillOval(-50, -50, 200, 200);
                g2.fillOval(getWidth() - 100, getHeight() - 100, 300, 300);

                g2.dispose();
            }
        };
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Branding illustration
        JLabel lblEmoji = new JLabel("📊");
        lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 96));
        lblEmoji.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblBrand = new JLabel("SaaS-Track");
        lblBrand.setFont(MaterialTheme.DISPLAY_SMALL);
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTagline1 = new JLabel("Platform Manajemen Langganan");
        lblTagline1.setFont(MaterialTheme.BODY_LARGE);
        lblTagline1.setForeground(new Color(255, 255, 255, 200));
        lblTagline1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTagline2 = new JLabel("Terintegrasi & Modern");
        lblTagline2.setFont(MaterialTheme.BODY_LARGE);
        lblTagline2.setForeground(new Color(255, 255, 255, 200));
        lblTagline2.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(lblEmoji);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(lblBrand);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(lblTagline1);
        leftPanel.add(lblTagline2);
        leftPanel.add(Box.createVerticalGlue());

        // Right Panel (Form)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        // Custom Close Button
        JButton btnClose = new JButton("×");
        btnClose.setFont(new Font("Arial", Font.PLAIN, 28));
        btnClose.setForeground(MaterialTheme.ON_SURFACE_VARIANT);
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> System.exit(0));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setOpaque(false);
        topBar.add(btnClose);

        // Form Container
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(40, 60, 40, 60));

        JLabel lblLoginTitle = new JLabel("Selamat Datang");
        lblLoginTitle.setFont(MaterialTheme.HEADLINE_MEDIUM);
        lblLoginTitle.setForeground(MaterialTheme.PRIMARY);
        lblLoginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblLoginSub = new JLabel("Silakan login untuk melanjutkan");
        lblLoginSub.setFont(MaterialTheme.BODY_MEDIUM);
        lblLoginSub.setForeground(MaterialTheme.ON_SURFACE_VARIANT);
        lblLoginSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Inputs
        txtUsername = MaterialTheme.createTextField();
        JPanel userWrap = createInputWrapper("Username", txtUsername);

        txtPassword = new JPasswordField();
        stylePasswordField(txtPassword);
        JPanel passWrap = createInputWrapper("Password", txtPassword);

        // Button
        btnLogin = MaterialTheme.createGradientButton("LOGIN SEKARANG", GRADIENT_START, GRADIENT_END);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblMessage = new JLabel(" ");
        lblMessage.setFont(MaterialTheme.BODY_SMALL);
        lblMessage.setForeground(MaterialTheme.ERROR);
        lblMessage.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(Box.createVerticalGlue());
        formPanel.add(lblLoginTitle);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(lblLoginSub);
        formPanel.add(Box.createVerticalStrut(40));
        formPanel.add(userWrap);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passWrap);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(lblMessage);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(btnLogin);
        formPanel.add(Box.createVerticalGlue());

        // Footer
        JLabel lblFooter = new JLabel("© 2025 Kelompok 8");
        lblFooter.setFont(MaterialTheme.LABEL_SMALL);
        lblFooter.setForeground(MaterialTheme.OUTLINE);
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);

        rightPanel.add(topBar, BorderLayout.NORTH);
        rightPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.add(lblFooter, BorderLayout.SOUTH);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        setContentPane(mainPanel);

        // Drag Support
        MouseAdapter ma = new MouseAdapter() {
            Point lastPoint;

            public void mousePressed(MouseEvent e) {
                lastPoint = e.getPoint();
            }

            public void mouseDragged(MouseEvent e) {
                if (lastPoint != null) {
                    Point current = getLocation();
                    setLocation(current.x + e.getX() - lastPoint.x, current.y + e.getY() - lastPoint.y);
                }
            }
        };
        leftPanel.addMouseListener(ma);
        leftPanel.addMouseMotionListener(ma);
        rightPanel.addMouseListener(ma);
        rightPanel.addMouseMotionListener(ma);

        // Enter key support
        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    btnLogin.doClick();
            }
        });
    }

    // Helper to wrap inputs with label
    private JPanel createInputWrapper(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = new JLabel(label);
        l.setFont(MaterialTheme.LABEL_MEDIUM);
        l.setForeground(MaterialTheme.ON_SURFACE_VARIANT);

        p.add(l, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void stylePasswordField(JPasswordField pf) {
        pf.setFont(MaterialTheme.BODY_LARGE);
        pf.setForeground(MaterialTheme.ON_SURFACE);
        pf.setOpaque(false);
        pf.setBorder(new EmptyBorder(12, 16, 12, 16));
        pf.setCaretColor(MaterialTheme.PRIMARY);

        // We can't use the custom paintComponent of JTextField easily here without
        // extending,
        // so we'll wrap it in a border or just style basics.
        // For consistency, let's just make it look clean with a border.
        pf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(MaterialTheme.OUTLINE_VARIANT, 1, true),
                new EmptyBorder(10, 15, 10, 15)));
    }

    // Getters
    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public void showError(String msg) {
        lblMessage.setForeground(MaterialTheme.ERROR);
        lblMessage.setText(msg);
    }

    public void showSuccess(String msg) {
        lblMessage.setForeground(MaterialTheme.SUCCESS);
        lblMessage.setText(msg);
    }

    public void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        lblMessage.setText(" ");
    }
}
