package main;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;

public class MainPageParticipant extends MainPage {
    private String userType;
    private JLabel logoLabel; // Add this field to store reference

    public MainPageParticipant() {
        this("STUDENT"); // Default to STUDENT
    }
    
    public MainPageParticipant(String userType) {
        super("Event Participant");
        this.userType = userType;
        updateLogo();
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
        // Update the logo label if it exists
        if (logoLabel != null) {
            if (userType.equals("STUDENT")) {
                logoLabel.setText("STUDENT EVENT PORTAL");
            } else if (userType.equals("STAFF")) {
                logoLabel.setText("STAFF EVENT PORTAL");
            } else {
                logoLabel.setText("EVENT PORTAL");
            }
        }
        refreshEventDisplay();
    }

    // Method to refresh the event display based on user type
    private void refreshEventDisplay() {
        // This method will be implemented later when you have Excel files for different event types
        // For now it just shows different titles based on user type
        if (userType.equals("STUDENT")) {
            setTitle("Student Event Portal");
        } else if (userType.equals("STAFF")) {
            setTitle("Staff Event Portal");
        }
        
        // In the future, this will filter events based on userType
        // using Excel data for student vs staff events
        repaint();
        revalidate();
    }

    @Override
    protected JComponent createCategoryButton() {
        JPanel myEventButton = createStandardCategoryButton("MY EVENT", true);
        myEventButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                return ;
            }
        });
        return myEventButton;
    }

    @Override
    protected JComponent createContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(pageBackground);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Grid panel setup with event cards
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 55, 55));
        gridPanel.setBackground(pageBackground);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Add mock event cards with sample data
        String[] eventIDs = {"C#10 TO 2010", "C#09 TO 2010", "C#20 TO 2010", "C#30 TO 2010", "C#50 TO 2010"};
        String[] eventNames = {"CMA6134-COMPUTATIONAL METHODS", "COP6214-ALGORITHM DESIGN AND ANALYSIS", 
                              "COP6224-0040", "CSN6224-COMPUTER NETWORKS", "CCS6214-CYBERSECURITY FUNDAMENTALS"};
        String[] images = {"icon/celebration.png", "icon/cyber.png", "icon/earth-day.png", "icon/glass.png", 
                          "icon/olympia.png", "icon/singing.png", "icon/soccer.png", "icon/valentine.png", 
                          "icon/volunteer.png"};
        
        // Create cards in a loop instead of repeating code
        for (int i = 0; i < 10; i++) {
            gridPanel.add(createEventCard(
                eventIDs[i % 5], 
                eventNames[i % 5], 
                images[i % 9]
            ));
        }
        
        // Add grid to wrapper with margins
        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setBackground(pageBackground);
        gridWrapper.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        gridWrapper.add(gridPanel, BorderLayout.CENTER);
        contentPanel.add(gridWrapper);
        
        // Show more text at bottom right
        JPanel showMorePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        showMorePanel.setBackground(pageBackground);
        showMorePanel.add(new JLabel("Show 12") {{
            setFont(new Font("Arial", Font.PLAIN, 12));
            setForeground(Color.GRAY);
        }});
        
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(showMorePanel);

        // Scrollable panel with modern scrollbar
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        return scrollPane;
    }
    
    // Modern, minimal scroll bar UI
    private class ModernScrollBarUI extends BasicScrollBarUI {
        @Override protected JButton createDecreaseButton(int orientation) { return new JButton() {{ setPreferredSize(new Dimension(0, 0)); }}; }
        @Override protected JButton createIncreaseButton(int orientation) { return new JButton() {{ setPreferredSize(new Dimension(0, 0)); }}; }
        
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new Color(160, 160, 160, 180));
            g2.fillRoundRect(thumbBounds.x + 1, thumbBounds.y + 1, thumbBounds.width - 2, thumbBounds.height - 2, 8, 8);
            g2.dispose();
        }
        
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(new Color(220, 220, 220, 80));
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
    }

    @Override
    protected JLabel createLogo() {
        // Use safer string comparison and provide default
        String displayText = "EVENT PORTAL"; // Default
        if ("STUDENT".equals(userType)) {
            displayText = "STUDENT EVENT PORTAL";
        } else if ("STAFF".equals(userType)) {
            displayText = "STAFF EVENT PORTAL";
        }
        
        logoLabel = new JLabel(displayText);
        
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.BLACK);
        
        try {
            ImageIcon mmuIcon = new ImageIcon(getClass().getClassLoader().getResource("icon/MMU.jpg"));
            Image img = mmuIcon.getImage().getScaledInstance(90, 50, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
            logoLabel.setIconTextGap(10);
        } catch (Exception e) {
            System.err.println("Could not load MMU icon");
        }
        
        return logoLabel;
    }
    
    private void updateLogo() {
        if (logoLabel != null) {
            if ("STUDENT".equals(userType)) {
                logoLabel.setText("STUDENT EVENT PORTAL");
            } else if ("STAFF".equals(userType)) {
                logoLabel.setText("STAFF EVENT PORTAL");
            } else {
                logoLabel.setText("EVENT PORTAL");
            }
        }
    }
}