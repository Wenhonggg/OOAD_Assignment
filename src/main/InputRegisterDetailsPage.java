package main;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

public class InputRegisterDetailsPage extends JFrame {

    private final Color LIGHT_GRAY = Color.decode("#F5F5F5");
    private final Color PINK_BG = Color.decode("#F8E7F6");
    private final Color ACCENT = Color.decode("#DD88CF");
    private final Color DEEP_PURPLE = Color.decode("#4B164C");

    private JTextField nameField, idField, emailField;
    private JSpinner quantitySpinner;
    private JCheckBox cateringBox, transportBox;

    public InputRegisterDetailsPage() {
        setTitle("Event Registration");
        setSize(650, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Font headerFont = new Font("Serif", Font.BOLD, 25);
        Font labelFont = new Font("Monospaced", Font.PLAIN, 14);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DEEP_PURPLE);
        JLabel titleLabel = new JLabel("Register for Event");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(headerFont);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(LIGHT_GRAY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Personal Info
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        infoPanel.setBackground(PINK_BG);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Personal Information"));

        nameField = new JTextField();
        idField = new JTextField();
        emailField = new JTextField();

        for (JTextField tf : new JTextField[]{nameField, idField, emailField}) {
            tf.setFont(labelFont);
            tf.setBackground(Color.WHITE);
            tf.setBorder(new RoundedBorder(15, 2, ACCENT));
        }

        infoPanel.add(new JLabel("Full Name:"));
        infoPanel.add(nameField);
        infoPanel.add(new JLabel("Student/Staff ID:"));
        infoPanel.add(idField);
        infoPanel.add(new JLabel("Email:"));
        infoPanel.add(emailField);

        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Quantity Panel (default box)
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quantityPanel.setBackground(PINK_BG);
        quantityPanel.setBorder(BorderFactory.createTitledBorder("Number of People"));

        JLabel qtyLabel = new JLabel("Quantity:");
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantitySpinner.setPreferredSize(new Dimension(60, 25));
        quantitySpinner.setFont(labelFont);

        quantityPanel.add(qtyLabel);
        quantityPanel.add(quantitySpinner);
        contentPanel.add(quantityPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Services Panel
        JPanel servicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        servicePanel.setBackground(PINK_BG);
        servicePanel.setBorder(BorderFactory.createTitledBorder("Additional Services"));

        cateringBox = new JCheckBox("Catering");
        transportBox = new JCheckBox("Transport");

        styleCheckbox(cateringBox);
        styleCheckbox(transportBox);

        servicePanel.add(cateringBox);
        servicePanel.add(transportBox);
        contentPanel.add(servicePanel);

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        // Pay Now Button
        JButton payButton = new JButton("Pay Now");
        payButton.setBackground(ACCENT);
        payButton.setForeground(Color.WHITE);
        payButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        payButton.setFocusPainted(false);
        payButton.setPreferredSize(new Dimension(120, 35));
        payButton.setBorder(new RoundedBorder(15, 2, ACCENT));
        payButton.addActionListener(e -> openPaymentPage());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(LIGHT_GRAY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bottomPanel.add(payButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void openPaymentPage() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        String email = emailField.getText().trim();
        int qty = (Integer) quantitySpinner.getValue();
        boolean catering = cateringBox.isSelected();
        boolean transport = transportBox.isSelected();

        if (name.isEmpty() || id.isEmpty() || email.isEmpty()) {
            showStyledDialog("Please fill in all personal information.");
            return;
        }

        //new PaymentPage(name, id, email, qty, catering, transport);
    }

    private void showStyledDialog(String message) {
        JDialog dialog = new JDialog(this, "Alert", true);
        dialog.setSize(350, 150);
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
        msg.setFont(new Font("Monospaced", Font.PLAIN, 14));
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

    private void styleCheckbox(JCheckBox cb) {
        cb.setBackground(PINK_BG);
        cb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cb.setBorder(new RoundedBorder(15, 2, ACCENT));
        cb.setFocusPainted(false);
    }

    // extra design
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
    
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(InputRegisterDetailsPage::new);
    // }
}
