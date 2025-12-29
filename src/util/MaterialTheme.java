package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Material Design 3 (Material You) Theme untuk Java Swing
 * Berdasarkan Android 12+ Design Language
 */
public class MaterialTheme {

    // Material You Color Palette (Dynamic Colors)
    // Primary (Purple/Blue tones like Android 12+)
    public static final Color PRIMARY = new Color(103, 80, 164); // Purple 500
    public static final Color PRIMARY_CONTAINER = new Color(234, 221, 255); // Purple 100
    public static final Color ON_PRIMARY = Color.WHITE;
    public static final Color ON_PRIMARY_CONTAINER = new Color(33, 0, 93);

    // Secondary
    public static final Color SECONDARY = new Color(98, 91, 113);
    public static final Color SECONDARY_CONTAINER = new Color(232, 222, 248);

    // Tertiary
    public static final Color TERTIARY = new Color(125, 82, 96);
    public static final Color TERTIARY_CONTAINER = new Color(255, 216, 228);

    // Surface & Background
    public static final Color SURFACE = new Color(255, 251, 254);
    public static final Color SURFACE_VARIANT = new Color(231, 224, 236);
    public static final Color BACKGROUND = new Color(255, 251, 254);
    public static final Color ON_SURFACE = new Color(28, 27, 31);
    public static final Color ON_SURFACE_VARIANT = new Color(73, 69, 79);

    // Outline
    public static final Color OUTLINE = new Color(121, 116, 126);
    public static final Color OUTLINE_VARIANT = new Color(202, 196, 208);

    // Status Colors
    public static final Color SUCCESS = new Color(56, 142, 60);
    public static final Color SUCCESS_CONTAINER = new Color(200, 230, 201);
    public static final Color ERROR = new Color(179, 38, 30);
    public static final Color ERROR_CONTAINER = new Color(249, 222, 220);
    public static final Color WARNING = new Color(255, 152, 0);
    public static final Color WARNING_CONTAINER = new Color(255, 243, 224);

    // Typography
    public static final Font DISPLAY_LARGE = new Font("Segoe UI", Font.BOLD, 57);
    public static final Font DISPLAY_MEDIUM = new Font("Segoe UI", Font.BOLD, 45);
    public static final Font DISPLAY_SMALL = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font HEADLINE_LARGE = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font HEADLINE_MEDIUM = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font HEADLINE_SMALL = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font TITLE_LARGE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font TITLE_MEDIUM = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font TITLE_SMALL = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BODY_LARGE = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font BODY_MEDIUM = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BODY_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font LABEL_LARGE = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font LABEL_MEDIUM = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font LABEL_SMALL = new Font("Segoe UI", Font.BOLD, 11);

    // Spacing & Radius
    public static final int CORNER_RADIUS_SMALL = 8;
    public static final int CORNER_RADIUS_MEDIUM = 12;
    public static final int CORNER_RADIUS_LARGE = 16;
    public static final int CORNER_RADIUS_EXTRA_LARGE = 28;

    /**
     * Create a Material Design 3 styled button
     */
    public static JButton createFilledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS_EXTRA_LARGE, CORNER_RADIUS_EXTRA_LARGE);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(LABEL_LARGE);
        button.setForeground(ON_PRIMARY);
        button.setBackground(PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 24, 12, 24));
        return button;
    }

    /**
     * Create a Filled Tonal Button (Secondary style)
     */
    public static JButton createTonalButton(String text, Color containerColor, Color onContainerColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS_EXTRA_LARGE, CORNER_RADIUS_EXTRA_LARGE);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(LABEL_LARGE);
        button.setForeground(onContainerColor);
        button.setBackground(containerColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 24, 12, 24));
        return button;
    }

    /**
     * Create Material Card with elevation shadow
     */
    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 4, CORNER_RADIUS_LARGE, CORNER_RADIUS_LARGE);

                // Card background
                g2.setColor(SURFACE);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 6, CORNER_RADIUS_LARGE, CORNER_RADIUS_LARGE);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        return card;
    }

    /**
     * Create Material TextField
     */
    public static JTextField createTextField() {
        JTextField tf = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SURFACE_VARIANT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS_SMALL, CORNER_RADIUS_SMALL);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setFont(BODY_LARGE);
        tf.setForeground(ON_SURFACE);
        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(16, 16, 16, 16));
        tf.setCaretColor(PRIMARY);
        return tf;
    }

    /**
     * Create styled table header
     */
    public static void styleTable(JTable table) {
        table.setFont(BODY_MEDIUM);
        table.setRowHeight(56);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(PRIMARY_CONTAINER);
        table.setSelectionForeground(ON_PRIMARY_CONTAINER);
        table.setBackground(SURFACE);
        table.setForeground(ON_SURFACE);

        table.getTableHeader().setFont(LABEL_LARGE);
        table.getTableHeader().setBackground(SURFACE);
        table.getTableHeader().setForeground(ON_SURFACE_VARIANT);
        table.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, OUTLINE_VARIANT));
        table.getTableHeader().setPreferredSize(new Dimension(0, 56));
    }
}
