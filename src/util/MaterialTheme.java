package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.geom.*;

/**
 * Material Design 3 (Material You) Theme untuk Java Swing
 * Updated with "Monet" inspired Blue Palette for a softer look.
 */
public class MaterialTheme {

    // Monet Engine Blue Palette (Based on M3 Standard Blue)
    // Primary Key: #0061A4
    public static final Color PRIMARY = new Color(0, 97, 164);
    public static final Color PRIMARY_CONTAINER = new Color(209, 228, 255);
    public static final Color ON_PRIMARY = Color.WHITE;
    public static final Color ON_PRIMARY_CONTAINER = new Color(0, 29, 54);

    // Secondary (Softer Blue-Grey)
    public static final Color SECONDARY = new Color(83, 95, 112);
    public static final Color SECONDARY_CONTAINER = new Color(215, 227, 248);
    public static final Color ON_SECONDARY_CONTAINER = new Color(16, 28, 43);

    // Tertiary (Visual Interest - distinct but harmonic, e.g. Violet/Pink-ish)
    public static final Color TERTIARY = new Color(107, 87, 120);
    public static final Color TERTIARY_CONTAINER = new Color(243, 218, 255);

    // Surface & Background (Soft Blue Tints - NOT Pure White)
    public static final Color BACKGROUND = new Color(248, 253, 255); // Very soft Ice Blue
    public static final Color SURFACE = new Color(248, 253, 255);
    public static final Color SURFACE_VARIANT = new Color(223, 226, 235); // For inputs/cards borders
    public static final Color SURFACE_CONTAINER = new Color(236, 240, 248); // Slightly darker surface for cards

    public static final Color ON_SURFACE = new Color(26, 28, 30);
    public static final Color ON_SURFACE_VARIANT = new Color(67, 71, 78);

    // Outline
    public static final Color OUTLINE = new Color(115, 119, 127);
    public static final Color OUTLINE_VARIANT = new Color(195, 199, 207);

    // Status Colors (Harmonized)
    public static final Color SUCCESS = new Color(34, 197, 94); // Green
    public static final Color SUCCESS_CONTAINER = new Color(220, 252, 231);
    public static final Color ERROR = new Color(186, 26, 26); // Red
    public static final Color ERROR_CONTAINER = new Color(255, 218, 214);
    public static final Color WARNING = new Color(245, 158, 11); // Amber
    public static final Color WARNING_CONTAINER = new Color(254, 243, 199);

    // Typography (Segoe UI optimized)
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
    public static final int CORNER_RADIUS_MEDIUM = 16;
    public static final int CORNER_RADIUS_LARGE = 24;
    public static final int CORNER_RADIUS_EXTRA_LARGE = 32;

    public static void enableQualityRendering(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    // --- Component Helpers ---

    public static JButton createGradientButton(String text, Color color1, Color color2) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                enableQualityRendering(g2);

                if (getModel().isPressed()) {
                    g2.setPaint(new GradientPaint(0, 0, color1.darker(), getWidth(), 0, color2.darker()));
                } else {
                    g2.setPaint(new GradientPaint(0, 0, color1, getWidth(), 0, color2));
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 100, 100); // Fully rounded (Capsule)

                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 40));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 100, 100);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(LABEL_LARGE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 28, 12, 28));
        return button;
    }

    public static JButton createFilledButton(String text, Color bg) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                enableQualityRendering(g2);

                if (getModel().isPressed()) {
                    g2.setColor(bg.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bg.brighter());
                } else {
                    g2.setColor(bg);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 100, 100); // Capsule
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(LABEL_LARGE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 24, 10, 24));
        return button;
    }

    /**
     * Standard M3 Card
     * Uses SURFACE_CONTAINER color (slightly distinct from background)
     */
    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                enableQualityRendering(g2);

                g2.setColor(SURFACE_CONTAINER);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS_LARGE, CORNER_RADIUS_LARGE);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        return card;
    }

    public static JTextField createTextField() {
        JTextField tf = new JTextField() {
            private boolean isFocused = false;
            {
                addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent evt) {
                        isFocused = true;
                        repaint();
                    }

                    public void focusLost(java.awt.event.FocusEvent evt) {
                        isFocused = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                enableQualityRendering(g2);

                // M3 Filled Text Field Style
                g2.setColor(isFocused ? new Color(230, 242, 255) : SURFACE_VARIANT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS_SMALL, CORNER_RADIUS_SMALL);

                // Indicator line at bottom (if desired) or full border
                if (isFocused) {
                    g2.setColor(PRIMARY);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, CORNER_RADIUS_SMALL, CORNER_RADIUS_SMALL);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setFont(BODY_LARGE);
        tf.setForeground(ON_SURFACE);
        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(12, 16, 12, 16));
        tf.setCaretColor(PRIMARY);
        return tf;
    }

    public static void styleTable(JTable table) {
        table.setFont(BODY_MEDIUM);
        table.setRowHeight(50);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(PRIMARY_CONTAINER);
        table.setSelectionForeground(ON_PRIMARY_CONTAINER);
        table.setBackground(SURFACE);
        table.setForeground(ON_SURFACE);

        table.getTableHeader().setFont(LABEL_LARGE);
        table.getTableHeader().setBackground(BACKGROUND);
        table.getTableHeader().setForeground(ON_SURFACE_VARIANT);
        table.getTableHeader().setBorder(null);
        table.getTableHeader().setPreferredSize(new Dimension(0, 50));
    }
}
