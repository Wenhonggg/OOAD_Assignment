package main;
import javax.swing.*;
import java.awt.*;
import swing.ButtonOutLine;
import javax.swing.border.EmptyBorder;

public abstract class MainPage extends JFrame {
    // Common properties for all pages
    protected String pageTitle;
    protected Color headerBackground = Color.WHITE;
    protected Color headerTextColor = Color.BLACK;
    protected Color cardBackground = Color.WHITE;
    protected Color cardHeaderColor = new Color(144, 238, 144); // Light green
    protected Color pageBackground = new Color(240, 240, 240); // Light grey
    protected Color buttonColor = new Color(70, 130, 180); // Steel blue
    protected Color buttonHoverColor = new Color(100, 149, 237); // Cornflower blue

    public MainPage(String title) {
        this.pageTitle = title;
        setupPage();
    }

    // Template method: Defines the skeleton of the page
    private void setupPage() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Header (common to all pages)
        add(createHeader(), BorderLayout.NORTH);

        // Content (specific to each subclass)
        add(createContent(), BorderLayout.CENTER);

        setVisible(true);
    }

    // Common header for all pages - redesigned to match reference image
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(headerBackground);
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        // Left side: Logo
        JLabel logoLabel = new JLabel("MMU STUDENT EVENT PORTAL");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(new Color(0, 120, 215)); // Microsoft blue
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // Center: Navigation buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonPanel.setBackground(headerBackground);
        
        String[] buttonLabels = {"EVENT", "MY EVENT"};
        for (String label : buttonLabels) {
            JButton button = createHeaderButton(label);
            buttonPanel.add(button);
        }
        headerPanel.add(buttonPanel, BorderLayout.CENTER);

        // Right side: Search and profile
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(headerBackground);

        // Search field with icon
        JTextField searchField = new JTextField(15);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(0, 5, 0, 5)
        ));
        rightPanel.add(searchField);

        // Profile icon (round)
        JLabel profileIcon = new JLabel("MK");
        profileIcon.setOpaque(true);
        profileIcon.setBackground(new Color(200, 200, 200));
        profileIcon.setForeground(Color.WHITE);
        profileIcon.setFont(new Font("Arial", Font.BOLD, 14));
        profileIcon.setHorizontalAlignment(SwingConstants.CENTER);
        profileIcon.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Make it round
        profileIcon.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        profileIcon.setPreferredSize(new Dimension(40, 40));
        profileIcon.setBackground(new Color(70, 130, 180)); // Steel blue
        rightPanel.add(profileIcon);

        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JButton createHeaderButton(String text) {
        // Use your custom Button class instead of JButton
        ButtonOutLine button = new ButtonOutLine();
        
        // Configure the button
        button.setText(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(headerBackground);
        button.setForeground(headerTextColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorderPainted(true);
        // Set preferred size to ensure proper spacing
        button.setPreferredSize(new Dimension(120, 40));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(38, 187, 237)); // Lighter blue on hover
                button.setForeground(buttonColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 215)); 
                button.setForeground(headerTextColor);
            }
        });

        return button;
    }

    // Abstract method for content (to be implemented by subclasses)
    protected abstract JComponent createContent();

    // Shared helper method for creating course cards (updated to match reference)
    protected JPanel createCourseCard(String courseCode, String courseName, String status, String imagePath) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(cardBackground);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        card.setPreferredSize(new Dimension(200, 290));

        //-- Image (80% height) ---
        JLabel imageLabel = new JLabel();
        try {
            // Use the classloader to find resources in the classpath
            java.net.URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                    500,  // Width matches card
                    250,  // 80% of 150px height
                    Image.SCALE_SMOOTH
                );
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                System.err.println("Could not find image: " + imagePath);
                imageLabel.setText("No Image");
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageLabel.setText("No Image");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        card.add(imageLabel, BorderLayout.CENTER);

        // --- Bottom Label (20% height) ---
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Create a panel for code and name
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel codeLabel = new JLabel(courseCode);
        codeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        leftPanel.add(codeLabel);
        
        // Add course name below the course code
        JLabel nameLabel = new JLabel(courseName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        leftPanel.add(nameLabel);
        
        infoPanel.add(leftPanel, BorderLayout.WEST);

        JLabel statusLabel = new JLabel(status, SwingConstants.RIGHT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        statusLabel.setForeground(Color.GRAY);
        infoPanel.add(statusLabel, BorderLayout.EAST);

        card.add(infoPanel, BorderLayout.SOUTH);

        return card;
    }
}
