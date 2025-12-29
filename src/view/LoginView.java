package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Form Login untuk aplikasi SaaS-Track
 * Membedakan menu berdasarkan role (Admin vs Operator)
 */
public class LoginView extends JFrame {

    // Components
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblMessage;

    // Constants
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public LoginView() {
        initComponents();
    }

    private void initComponents() {
        // Frame settings
        setTitle("SaaS-Track - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, 0, getHeight(), SECONDARY_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Login card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(40, 40, 40, 40)));
        cardPanel.setMaximumSize(new Dimension(350, 380));
        cardPanel.setPreferredSize(new Dimension(350, 380));

        // Logo/Title
        JLabel lblTitle = new JLabel("SaaS-Track");
        lblTitle.setFont(TITLE_FONT);
        lblTitle.setForeground(PRIMARY_COLOR);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Sistem Manajemen Langganan SaaS");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(Color.GRAY);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username field
        JPanel usernamePanel = createInputPanel("Username", false);
        txtUsername = (JTextField) usernamePanel.getComponent(2);

        // Password field
        JPanel passwordPanel = createInputPanel("Password", true);
        txtPassword = (JPasswordField) passwordPanel.getComponent(2);

        // Login button
        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(PRIMARY_COLOR);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(31, 97, 141));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(PRIMARY_COLOR);
            }
        });

        // Message label
        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMessage.setForeground(Color.RED);
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to card
        cardPanel.add(lblTitle);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(lblSubtitle);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(usernamePanel);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(passwordPanel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(lblMessage);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(btnLogin);

        // Center wrapper
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(cardPanel);

        mainPanel.add(wrapperPanel, BorderLayout.CENTER);

        // Footer
        JLabel lblFooter = new JLabel("© 2024 SaaS-Track Management System");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(200, 200, 200));
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
        lblFooter.setBorder(new EmptyBorder(10, 0, 15, 0));
        mainPanel.add(lblFooter, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Enter key listener
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLogin.doClick();
                }
            }
        });
    }

    private JPanel createInputPanel(String labelText, boolean isPassword) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(SECONDARY_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComponent inputField;
        if (isPassword) {
            inputField = new JPasswordField();
        } else {
            inputField = new JTextField();
        }
        inputField.setFont(INPUT_FONT);
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(8, 12, 8, 12)));

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(inputField);

        return panel;
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

    public JLabel getLblMessage() {
        return lblMessage;
    }

    // Helper methods
    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public void showError(String message) {
        lblMessage.setForeground(Color.RED);
        lblMessage.setText(message);
    }

    public void showSuccess(String message) {
        lblMessage.setForeground(new Color(39, 174, 96));
        lblMessage.setText(message);
    }

    public void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        lblMessage.setText(" ");
    }

    // Main method for testing
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
