import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

public class ViewInfoPage extends JFrame {

    private final Color LIGHT_GRAY = Color.decode("#F5F5F5");
    private final Color PINK_BG = Color.decode("#F8E7F6");
    private final Color ACCENT = Color.decode("#DD88CF");
    private final Color DEEP_PURPLE = Color.decode("#4B164C");

    public ViewInfoPage() {
        setTitle("Event Info");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Font headerFont = new Font("SansSerif", Font.BOLD, 20);
        Font textFont = new Font("SansSerif", Font.PLAIN, 14);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DEEP_PURPLE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Event Information");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton registerButton = new JButton("Register Now");
        registerButton.setBackground(ACCENT);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(new RoundedBorder(15, 2, ACCENT));
        registerButton.addActionListener(e -> new InputRegisterDetailsPage());

        headerPanel.add(registerButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Scrollable Event Details
        JTextArea eventInfo = new JTextArea(generateEventText());
        eventInfo.setWrapStyleWord(true);
        eventInfo.setLineWrap(true);
        eventInfo.setEditable(false);
        eventInfo.setFont(textFont);
        eventInfo.setBackground(PINK_BG);
        eventInfo.setForeground(DEEP_PURPLE);
        eventInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(eventInfo);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Terms and Conditions Link
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(LIGHT_GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JLabel termsLabel = new JLabel("<HTML><U>Terms and Conditions</U></HTML>");
        termsLabel.setForeground(DEEP_PURPLE);
        termsLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        termsLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        termsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showStyledDialog("Terms and Conditions:\n\n1. No refunds after registration.\n2. Respect event timing.\n3. ID must be shown at entry.");
            }
        });

        footerPanel.add(termsLabel);
        add(footerPanel, BorderLayout.SOUTH);

        getContentPane().setBackground(LIGHT_GRAY);
        setVisible(true);
    }

    private void showStyledDialog(String message) {
        JDialog dialog = new JDialog(this, "Terms and Conditions", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(PINK_BG);
        dialog.setUndecorated(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PINK_BG);
        mainPanel.setBorder(new RoundedBorder(20, 2, ACCENT));

        JTextArea msg = new JTextArea(message);
        msg.setWrapStyleWord(true);
        msg.setLineWrap(true);
        msg.setEditable(false);
        msg.setFocusable(false);
        msg.setOpaque(false);
        msg.setFont(new Font("SansSerif", Font.PLAIN, 14));
        msg.setForeground(DEEP_PURPLE);
        msg.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JButton okButton = new JButton("OK");
        okButton.setBackground(ACCENT);
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        okButton.setBorder(new RoundedBorder(15, 2, ACCENT));
        okButton.setPreferredSize(new Dimension(80, 30));
        okButton.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(PINK_BG);
        btnPanel.add(okButton);

        mainPanel.add(msg, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private String generateEventText() {
        return """
                🎉 Welcome to the 2025 Campus Innovation Expo! 🎉

                Join us for a full-day celebration of creativity, technology, and ideas from across the university.

                📅 Date: July 15, 2025
                🕒 Time: 9:00 AM – 6:00 PM
                📍 Venue: Main Auditorium & Exhibition Hall

                🔍 Highlights:
                - Over 100 student projects across AI, HealthTech, Business, and Sustainability.
                - Panel discussions with industry leaders.
                - Interactive workshops (limited seats, registration required).
                - Food trucks, photo booths, and giveaways!

                📝 Registration:
                - Free for all students and staff.
                - Optional services: catering, transportation.
                - Group registration available.

                📧 Contact: events@university.edu.my

                We look forward to seeing you there!
                """;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewInfoPage::new);
    }

    // Reusable RoundedBorder class
    static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final int thickness;
        private final Color borderColor;

        public RoundedBorder(int radius, int thickness, Color borderColor) {
            this.radius = radius;
            this.thickness = thickness;
            this.borderColor = borderColor;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(thickness));
            Shape rounded = new RoundRectangle2D.Float(
                    x + thickness / 2f,
                    y + thickness / 2f,
                    width - thickness,
                    height - thickness,
                    radius,
                    radius
            );
            g2.draw(rounded);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + thickness, radius + thickness, radius + thickness, radius + thickness);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(radius + thickness, radius + thickness, radius + thickness, radius + thickness);
            return insets;
        }
    }
}
