package main;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.util.List; 
import main.Create_Event_Page_Organiser;


public class MainPageOrganizer extends MainPage {
    public MainPageOrganizer() {
        super("Event Organizer");
    }
    
    
    @Override
    protected JComponent createCategoryButton() {
        return null;
    }

    private static final Color[] CARD_COLORS = {
    new Color(0xA5D7E8),
    new Color(0xB8E0FF),
    new Color(0xC9E4FF)
};



private JPanel gridPanel; 
protected JComponent createContent() {
    gridPanel = new JPanel(new GridLayout(0, 3, 55, 55));
    gridPanel.setBackground(pageBackground);
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setBackground(pageBackground);
    contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // Filter panel with button
    JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 15));
    filterPanel.setBackground(pageBackground);
    filterPanel.add(createFilterButton());
    contentPanel.add(filterPanel);
    contentPanel.add(Box.createVerticalStrut(15));

    // Event cards grid
    JPanel gridPanel = new JPanel(new GridLayout(0, 3, 55, 55));
    gridPanel.setBackground(pageBackground);
    gridPanel.setBorder(BorderFactory.createEmptyBorder(9, 0, 0, 0));
    
    JPanel createEventCard = createCreateEventCard(); // "Create New Event" card is white
    gridPanel.add(createEventCard);

    List<Event> events = Event.readEventsFromCSV("database/events.csv");

    int colorIdx = 0; // Start from the first color for event cards
    for (Event event : events) {
        Color cardColor = CARD_COLORS[colorIdx % CARD_COLORS.length];
        gridPanel.add(createEventCard(event.getEventID(), event.getEventName(), event.getImagePath(), cardColor));
        colorIdx++;
    }
    

    // Wrap grid panel in a container with margins
    JPanel gridWrapper = new JPanel(new BorderLayout());
    gridWrapper.setBackground(pageBackground);
    gridWrapper.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
    gridWrapper.add(gridPanel, BorderLayout.CENTER);
    contentPanel.add(gridWrapper);

    // Show more label
    JPanel showMorePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    showMorePanel.setBackground(pageBackground);
    showMorePanel.add(new JLabel("Show 12") {{
        setFont(new Font("Arial", Font.PLAIN, 12));
        setForeground(Color.GRAY);
    }});
    contentPanel.add(Box.createVerticalStrut(15));
    contentPanel.add(showMorePanel);

    // Scrollable panel
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
    scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    
    return scrollPane; 
}
    
    // Simplified modern scrollbar UI
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
        JLabel logoLabel = new JLabel("ORGANISER EVENT PORTAL");
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
    
    private JPanel createFilterButton() {
        final String[] currentFilter = {"All"};
        final Color[] bgColor = {Color.BLACK};
        
        JPanel buttonPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor[0]);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
            }
        };
        
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(130, 30));
        
        JLabel buttonLabel = new JLabel("Filter: All");
        buttonLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        buttonLabel.setForeground(Color.WHITE);
        buttonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buttonLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        buttonPanel.add(buttonLabel, BorderLayout.CENTER);
        
        buttonPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bgColor[0] = Color.GRAY;
                buttonPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                buttonPanel.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                bgColor[0] = Color.BLACK;
                buttonPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                buttonPanel.repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (buttonPanel.contains(e.getPoint())) {
                    // Cycle through filter options
                    switch(currentFilter[0]) {
                        case "All": currentFilter[0] = "Student"; break;
                        case "Student": currentFilter[0] = "Staff"; break;
                        case "Staff": currentFilter[0] = "All"; break;
                    }
                    buttonLabel.setText("Filter: " + currentFilter[0]);
                    filterEventCards(currentFilter[0]);
                }
                
                bgColor[0] = buttonPanel.contains(e.getPoint()) ? Color.GRAY : Color.BLACK;
                buttonPanel.repaint();
            }
        });
        
        return buttonPanel;
    }

    private void filterEventCards(String filter) {
        // Filter implementation would go here
        repaint();
        revalidate();
    }


    protected JPanel createEventCard(String eventID, String eventName, String imagePath, Color cardColor) {
        JPanel card = super.createEventCard(eventID, eventName, imagePath);
        card.putClientProperty("eventID", eventID);

        setPanelBackgrounds(card, cardColor);
        
    // Find the info panel at the bottom of the card
    for (Component c : card.getComponents()) {
        if (c instanceof JPanel && card.getLayout() instanceof BorderLayout && 
            ((BorderLayout)card.getLayout()).getConstraints(c) == BorderLayout.SOUTH) {
            
            JPanel infoPanel = (JPanel)c;
            card.remove(infoPanel);
                
            // Create bottom panel with info panel and circle button
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);
            bottomPanel.add(infoPanel, BorderLayout.CENTER);
            bottomPanel.add(createCircleButton(), BorderLayout.EAST);

            card.add(bottomPanel, BorderLayout.SOUTH);
            break;
        }
    }
    return card;
}

    private void setPanelBackgrounds(Component comp, Color color) {
    if (comp instanceof JPanel) {
        comp.setBackground(color);
        ((JPanel) comp).setOpaque(true);
        for (Component child : ((JPanel) comp).getComponents()) {
            setPanelBackgrounds(child, color);
        }
    }
}

    private JLabel createCircleButton() {
        final boolean[] isHovered = {false};

        JLabel circleButton = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = Math.min(getWidth(), getHeight()) - 4;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                
                g2.setColor(Color.WHITE);
                g2.fillOval(x, y, size, size);

                if (isHovered[0]) {
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawOval(x, y, size, size);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        circleButton.setPreferredSize(new Dimension(30, 30));
        circleButton.setOpaque(false);
        
        try {
            ImageIcon dotIcon = new ImageIcon(getClass().getClassLoader().getResource("icon/dot.png"));
            circleButton.setIcon(new ImageIcon(dotIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
            circleButton.setHorizontalAlignment(SwingConstants.CENTER);
            circleButton.setVerticalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            System.err.println("Could not load dot icon: " + e.getMessage());
        }
        
        circleButton.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                isHovered[0] = true;
                circleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                circleButton.repaint(); 
            }
            
            @Override public void mouseExited(MouseEvent e) {
                isHovered[0] = false;
                circleButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                circleButton.repaint();
            }
            
            @Override public void mousePressed(MouseEvent e) {
                isHovered[0] = true;
                circleButton.repaint();
            }
            
            @Override public void mouseReleased(MouseEvent e) {
                if (circleButton.contains(e.getPoint())) {
                    JPanel card = findParentCard(circleButton);
                    if (card != null) showEventOptions(card);
                }
                isHovered[0] = circleButton.contains(e.getPoint());
                circleButton.repaint();
            }
        });
        
        return circleButton;
    }

    private JPanel findParentCard(Component component) {
        Container parent = component.getParent();
        while (parent != null) {
            if (parent instanceof JPanel && parent.getParent() != null && 
                parent.getParent().getLayout() instanceof GridLayout) {
                return (JPanel) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }


    private void refreshEventGrid(JPanel gridPanel) {
    gridPanel.removeAll();
    JPanel createEventCard = createCreateEventCard();
    gridPanel.add(createEventCard);

    List<Event> events = Event.readEventsFromCSV("database/events.csv");
    int colorIdx = 0;
    for (Event event : events) {
        Color cardColor = CARD_COLORS[colorIdx % CARD_COLORS.length];
        gridPanel.add(createEventCard(event.getEventID(), event.getEventName(), event.getImagePath(), cardColor));
        colorIdx++;
    }
    gridPanel.revalidate();
    gridPanel.repaint();
}
    private void showEventOptions(JPanel eventCard) {
        JPopupMenu optionsMenu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(e -> {
            String eventID = (String) eventCard.getClientProperty("eventID");
            new Create_Event_Page_Organiser(eventID);
        });

        JMenuItem deleteItem = new JMenuItem("Delete");
         deleteItem.addActionListener(e -> {
            String eventID = (String) eventCard.getClientProperty("eventID");
            int confirm = JOptionPane.showConfirmDialog(
                eventCard,
                "Are you sure you want to delete this event?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION && eventID != null) {
                try {
                    java.nio.file.Path path = java.nio.file.Paths.get("database/events.csv");
                    java.util.List<String> lines = java.nio.file.Files.readAllLines(path);
                    java.util.List<String> updated = new java.util.ArrayList<>();
                    updated.add(lines.get(0)); // keep header
                    for (int i = 1; i < lines.size(); i++) {
                        String[] parts = lines.get(i).split(",");
                        if (!parts[0].trim().equals(eventID)) {
                            updated.add(lines.get(i));
                        }
                    }
                    java.nio.file.Files.write(path, updated);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(eventCard, "Failed to delete event from CSV.");
                }
                // Refresh the grid panel from the CSV
                refreshEventGrid(gridPanel);
    }
});

    optionsMenu.add(editItem);
    optionsMenu.addSeparator();
    optionsMenu.add(deleteItem);

    optionsMenu.show(eventCard, eventCard.getWidth() - 110, 200);
}

    // New method to create the special Create Event card
    private JPanel createCreateEventCard() {
        final int hoverRise = 10;
        
        // Create card with rounded corners and hover effect
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                g2.setColor(getClientProperty("hovered") != null ? Color.BLACK : Color.LIGHT_GRAY);
                g2.setStroke(new BasicStroke(getClientProperty("hovered") != null ? 2f : 1f));
                g2.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 30, 30);
                
                g2.dispose();
            }
        };
        
        // Center panel to hold icon and text
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        
        // Icon
        JLabel iconLabel = new JLabel();
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon createIcon = new ImageIcon(getClass().getClassLoader().getResource("icon/create.png"));
            Image img = createIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Could not load create icon: " + e.getMessage());
        }
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(iconLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        
        // Text
        JLabel textLabel = new JLabel("Create New Event");
        textLabel.setFont(new Font("Arial", Font.BOLD, 18));
        textLabel.setForeground(Color.BLACK);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(textLabel);
        centerPanel.add(Box.createVerticalGlue());
        
        card.add(centerPanel, BorderLayout.CENTER);
        
        // Add hover and click effects
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setLocation(card.getX(), card.getY() - hoverRise);
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                card.putClientProperty("hovered", Boolean.TRUE);
                card.repaint();
            }
            
            public void mouseExited(MouseEvent e) {
                card.setLocation(card.getX(), card.getY() + hoverRise);
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                card.putClientProperty("hovered", null);
                card.repaint();
            }
            
            public void mouseClicked(MouseEvent e) {
                // Forward to create event page
                openCreateEventPage();
            }
        });
        
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        card.setPreferredSize(new Dimension(180, 250));
        
        return card;
    }

private void openCreateEventPage() {
    new Create_Event_Page_Organiser();
}

    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainPageOrganizer();
    });
}
}