package view;

import util.MaterialTheme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Form Login dengan Material Design 3 (Material You) Style
 */
public class LoginView extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblMessage;

    public LoginView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("SaaS-Track - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 600, 28, 28));

        // Main panel with gradient
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(103, 80, 164),
                        getWidth(), getHeight(), new Color(79, 55, 139));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.dispose();
            }
        };
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Card Container
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MaterialTheme.SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.dispose();
            }
        };
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setOpaque(false);
        cardPanel.setBorder(new EmptyBorder(48, 40, 48, 40));

        // App icon/Logo
        JLabel lblIcon = new JLabel("📊");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel lblTitle = new JLabel("SaaS-Track");
        lblTitle.setFont(MaterialTheme.HEADLINE_LARGE);
        lblTitle.setForeground(MaterialTheme.PRIMARY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel lblSubtitle = new JLabel("Sistem Manajemen Langganan");
        lblSubtitle.setFont(MaterialTheme.BODY_MEDIUM);
        lblSubtitle.setForeground(MaterialTheme.ON_SURFACE_VARIANT);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username field with Material styling
        JPanel usernameWrapper = createMaterialTextField("Username");
        txtUsername = (JTextField) usernameWrapper.getClientProperty("textField");

        // Password field
        JPanel passwordWrapper = createMaterialPasswordField("Password");
        txtPassword = (JPasswordField) passwordWrapper.getClientProperty("passwordField");

        // Message label
        lblMessage = new JLabel(" ");
        lblMessage.setFont(MaterialTheme.BODY_SMALL);
        lblMessage.setForeground(MaterialTheme.ERROR);
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Login button - Material Filled Button
        btnLogin = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(MaterialTheme.PRIMARY.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(123, 100, 184));
                } else {
                    g2.setColor(MaterialTheme.PRIMARY);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLogin.setFont(MaterialTheme.LABEL_LARGE);
        btnLogin.setForeground(MaterialTheme.ON_PRIMARY);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setOpaque(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Footer
        JLabel lblFooter = new JLabel("© 2025 SaaS-Track - Kelompok 8");
        lblFooter.setFont(MaterialTheme.LABEL_SMALL);
        lblFooter.setForeground(MaterialTheme.ON_SURFACE_VARIANT);
        lblFooter.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Close button
        JButton btnClose = new JButton("✕") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 50));
                    g2.fillOval(0, 0, getWidth(), getHeight());
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnClose.setFont(MaterialTheme.TITLE_MEDIUM);
        btnClose.setForeground(Color.WHITE);
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.setPreferredSize(new Dimension(40, 40));
        btnClose.addActionListener(e -> System.exit(0));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topBar.setOpaque(false);
        topBar.add(btnClose);

        // Assemble card
        cardPanel.add(lblIcon);
        cardPanel.add(Box.createVerticalStrut(16));
        cardPanel.add(lblTitle);
        cardPanel.add(Box.createVerticalStrut(4));
        cardPanel.add(lblSubtitle);
        cardPanel.add(Box.createVerticalStrut(40));
        cardPanel.add(usernameWrapper);
        cardPanel.add(Box.createVerticalStrut(16));
        cardPanel.add(passwordWrapper);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(lblMessage);
        cardPanel.add(Box.createVerticalStrut(24));
        cardPanel.add(btnLogin);
        cardPanel.add(Box.createVerticalGlue());
        cardPanel.add(lblFooter);

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        // Window drag support
        final Point[] dragPoint = { null };
        mainPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dragPoint[0] = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                dragPoint[0] = null;
            }
        });
        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (dragPoint[0] != null) {
                    Point loc = getLocation();
                    setLocation(loc.x + e.getX() - dragPoint[0].x, loc.y + e.getY() - dragPoint[0].y);
                }
            }
        });

        // Enter key
        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    btnLogin.doClick();
            }
        });
    }

    private JPanel createMaterialTextField(String hint) {
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MaterialTheme.SURFACE_VARIANT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        wrapper.setBorder(new EmptyBorder(0, 16, 0, 16));

        JTextField tf = new JTextField();
        tf.setFont(MaterialTheme.BODY_LARGE);
        tf.setForeground(MaterialTheme.ON_SURFACE);
        tf.setOpaque(false);
        tf.setBorder(null);
        tf.setCaretColor(MaterialTheme.PRIMARY);

        JLabel lbl = new JLabel(hint);
        lbl.setFont(MaterialTheme.BODY_SMALL);
        lbl.setForeground(MaterialTheme.ON_SURFACE_VARIANT);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setOpaque(false);
        inner.setBorder(new EmptyBorder(8, 0, 8, 0));
        inner.add(lbl, BorderLayout.NORTH);
        inner.add(tf, BorderLayout.CENTER);

        wrapper.add(inner, BorderLayout.CENTER);
        wrapper.putClientProperty("textField", tf);
        return wrapper;
    }

    private JPanel createMaterialPasswordField(String hint) {
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MaterialTheme.SURFACE_VARIANT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        wrapper.setBorder(new EmptyBorder(0, 16, 0, 16));

        JPasswordField pf = new JPasswordField();
        pf.setFont(MaterialTheme.BODY_LARGE);
        pf.setForeground(MaterialTheme.ON_SURFACE);
        pf.setOpaque(false);
        pf.setBorder(null);
        pf.setCaretColor(MaterialTheme.PRIMARY);

        JLabel lbl = new JLabel(hint);
        lbl.setFont(MaterialTheme.BODY_SMALL);
        lbl.setForeground(MaterialTheme.ON_SURFACE_VARIANT);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setOpaque(false);
        inner.setBorder(new EmptyBorder(8, 0, 8, 0));
        inner.add(lbl, BorderLayout.NORTH);
        inner.add(pf, BorderLayout.CENTER);

        wrapper.add(inner, BorderLayout.CENTER);
        wrapper.putClientProperty("passwordField", pf);
        return wrapper;
    }

    // Getters
    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
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
