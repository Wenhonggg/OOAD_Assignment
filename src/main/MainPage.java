package main;
import javax.swing.*;
import java.awt.*;

public abstract class MainPage extends JFrame {
    // Common properties for all pages
    protected String pageTitle;
    protected Color headerBackground = Color.WHITE;
    protected Color headerTextColor = Color.BLACK;
    protected Color cardBackground = Color.WHITE;
    protected Color cardHeaderColor = new Color(144, 238, 144); // Light green
    protected Color pageBackground = new Color(240, 240, 240); // Light grey

    public MainPage(String title) {
        this.pageTitle = title;
        setupPage();
    }

    // Template method: Defines the skeleton of the page
    private void setupPage() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Header (common to all pages)
        add(createHeader(), BorderLayout.NORTH);

        // Content (specific to each subclass)
        add(createContent(), BorderLayout.CENTER);

        setVisible(true);
    }

    // Common header for all pages
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(headerBackground);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel headerLabel = new JLabel(pageTitle, SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(headerTextColor);
        headerPanel.add(headerLabel);
        
        return headerPanel;
    }

    // Abstract method for content (to be implemented by subclasses)
    protected abstract JComponent createContent();

    // Shared helper method for creating event cards
    protected JPanel createEventCard(String name, String venue, String date) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(cardBackground);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(250, 150));

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setOpaque(true);
        nameLabel.setBackground(cardHeaderColor);
        nameLabel.setForeground(headerTextColor);
        card.add(nameLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridLayout(2, 1));
        detailsPanel.setBackground(cardBackground);

        JLabel venueLabel = new JLabel("Venue: " + venue);
        JLabel dateLabel = new JLabel("Date: " + date);
        venueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        detailsPanel.add(venueLabel);
        detailsPanel.add(dateLabel);
        card.add(detailsPanel, BorderLayout.CENTER);

        return card;
    }
}
