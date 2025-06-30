package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.TitledBorder;

public class ViewInfoPage extends JPanel {

    private final Color LIGHT_GRAY = Color.decode("#F5F5F5");
    private final Color PINK_BG = Color.decode("#F8E7F6");
    private final Color ACCENT = Color.decode("#DD88CF");
    private final Color DEEP_PURPLE = Color.decode("#4B164C");
    
    // EO (Event Organizer) color scheme
    private final Color BLUE_BG = Color.decode("#E7F3FF");
    private final Color BLUE_ACCENT = Color.decode("#4A90E2");
    private final Color DEEP_BLUE = Color.decode("#1C3A5B");
    
    private boolean isEventOrganizer;
    private Color backgroundColor;
    private Color accentColor;
    private Color textColor;
    
    public ViewInfoPage(JFrame f, Event event, boolean isEventOrganizer) {
        this.isEventOrganizer = isEventOrganizer;
        
        // Set color scheme based on user type
        if (isEventOrganizer) {
            backgroundColor = BLUE_BG;
            accentColor = BLUE_ACCENT;
            textColor = DEEP_BLUE;
        } else {
            backgroundColor = PINK_BG;
            accentColor = ACCENT;
            textColor = DEEP_PURPLE;
        }
        setLayout(new BorderLayout());

        Font headerFont = new Font("Serif", Font.BOLD, 25);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(textColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel(isEventOrganizer ? "Event Management" : "Event Information");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Only show register button for participants
        if (!isEventOrganizer) {
            JButton registerButton = new JButton("Register Now");
            registerButton.setBackground(accentColor);
            registerButton.setForeground(Color.WHITE);
            registerButton.setFont(new Font("Serif", Font.BOLD, 14));
            registerButton.setFocusPainted(false);
            registerButton.setBorder(new RoundedBorder(15, 2, accentColor));
            registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    f.remove(((MainPage) f).contentPanel);
                    ((MainPage) f).contentPanel = new InputRegisterDetailsPage(f, event);
                    f.add(((MainPage) f).contentPanel);
                    f.revalidate();
                    f.repaint();
                }
            });

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.setBackground(backgroundColor);
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            bottomPanel.add(registerButton);
            add(bottomPanel, BorderLayout.SOUTH);
        }

        // Info panel with labels
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(backgroundColor);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Styled labels
        infoPanel.add(makeLabel(event.getEventName(), new Font("Serif", Font.BOLD, 40)));
        infoPanel.add(Box.createVerticalStrut(15));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateTime = event.getEventDate().format(formatter) + " | " + event.getEventTime();
        infoPanel.add(makeLabel(dateTime, new Font("Serif", Font.PLAIN, 25)));
        infoPanel.add(makeLabel(event.getEventVenue(), new Font("Serif", Font.PLAIN, 25)));

        infoPanel.add(Box.createVerticalStrut(15));

        String formattedDetails = "<html><body style='width: 300px;'>" +
                event.getEventDetails().replaceAll("\n", "<br>") + "</body></html>";

        JLabel detailsLabel = new JLabel(formattedDetails);
        detailsLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        detailsLabel.setForeground(textColor);
        detailsLabel.setBackground(backgroundColor);
        detailsLabel.setOpaque(false);

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(backgroundColor);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(BorderFactory.createEmptyBorder(), "Event Details",
                        TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Serif", Font.BOLD, 14), textColor),
                new RoundedBorder(20, 2, accentColor)));
        detailsPanel.add(detailsLabel, BorderLayout.CENTER);
        detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(detailsPanel);

        infoPanel.add(Box.createVerticalStrut(15));

        String combinedFees;
        if (isEventOrganizer) {
            combinedFees = String.format(
                "Registration fee: RM%.2f  |  Catering: RM%.2f  |  Transport: RM%.2f  |  Capacity: %d participants",
                event.getEventFee(),
                event.getEventCateringFee(),
                event.getEventTransportationFee(),
                event.getEventCapacity()
            );
        } else {
            combinedFees = String.format(
                "Registration fee: RM%.2f  |  Catering: RM%.2f  |  Transport: RM%.2f",
                event.getEventFee(),
                event.getEventCateringFee(),
                event.getEventTransportationFee()
            );
        }

        infoPanel.add(makeLabel(combinedFees, new Font("Serif", Font.PLAIN, 16)));

        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        setBackground(LIGHT_GRAY);
        setVisible(true);
    }

    private JLabel makeLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setForeground(textColor);
        label.setFont(font);
        return label;
    }

    // RoundedBorder class
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
                    radius);
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
