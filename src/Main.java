import view.LoginView;
import view.MainFrame;
import model.User;

import javax.swing.*;

/**
 * Main class - Entry point untuk aplikasi SaaS-Track
 */
public class Main {

    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run application
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();

            // Login button action
            loginView.getBtnLogin().addActionListener(e -> {
                String username = loginView.getUsername();
                String password = loginView.getPassword();

                if (username.isEmpty() || password.isEmpty()) {
                    loginView.showError("Username dan password harus diisi!");
                    return;
                }

                // Simple authentication (nanti bisa diganti dengan database)
                User user = authenticate(username, password);

                if (user != null) {
                    loginView.showSuccess("Login berhasil!");

                    // Delay lalu buka MainFrame
                    Timer timer = new Timer(500, event -> {
                        loginView.dispose();
                        MainFrame mainFrame = new MainFrame(user);

                        // Logout action
                        mainFrame.getBtnLogout().addActionListener(ev -> {
                            int confirm = JOptionPane.showConfirmDialog(
                                    mainFrame,
                                    "Apakah Anda yakin ingin logout?",
                                    "Konfirmasi Logout",
                                    JOptionPane.YES_NO_OPTION);

                            if (confirm == JOptionPane.YES_OPTION) {
                                mainFrame.dispose();
                                main(null); // Restart app
                            }
                        });

                        mainFrame.setVisible(true);
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    loginView.showError("Username atau password salah!");
                }
            });

            loginView.setVisible(true);
        });
    }

    /**
     * Simple authentication method
     * TODO: Replace with database authentication
     */
    private static User authenticate(String username, String password) {
        // Hardcoded users for testing (sesuai sample data di database)
        if ("admin".equals(username) && "admin123".equals(password)) {
            return new User(1, username, password, "admin", null, null);
        } else if ("operator1".equals(username) && "operator123".equals(password)) {
            return new User(2, username, password, "operator", null, null);
        }
        return null;
    }
}
