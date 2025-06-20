package main;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;

public class MainPageOrganizer extends MainPage {
    public MainPageOrganizer() {
        super("Event Organizer");
    }
    
    @Override
    protected JComponent createCategoryButton() {
        return null;
    }

    @Override
    protected JComponent createContent() {
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
        
        // Add mock event cards
        String[] eventIDs = {"C#10 TO 2010", "C#09 TO 2010", "C#20 TO 2010", "C#30 TO 2010", "C#50 TO 2010"};
        String[] eventNames = {"CMA6134-COMPUTATIONAL METHODS", "COP6214-ALGORITHM DESIGN AND ANALYSIS", 
                              "COP6224-0040", "CSN6224-COMPUTER NETWORKS", "CCS6214-CYBERSECURITY FUNDAMENTALS"};
        String[] images = {"icon/celebration.png", "icon/cyber.png", "icon/earth-day.png", "icon/glass.png", 
                          "icon/olympia.png", "icon/singing.png", "icon/soccer.png", "icon/valentine.png", 
                          "icon/volunteer.png"};
        
        for (int i = 0; i < 10; i++) {
            gridPanel.add(createEventCard(eventIDs[i % 5], eventNames[i % 5], images[i % 9]));
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

    @Override
    protected JPanel createEventCard(String eventID, String eventName, String imagePath) {
        JPanel card = super.createEventCard(eventID, eventName, imagePath);
        
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

    private void showEventOptions(JPanel eventCard) {
        JPopupMenu optionsMenu = new JPopupMenu();
        
        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(e -> {});
        
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(e -> {});
        
        optionsMenu.add(editItem);
        optionsMenu.addSeparator();
        optionsMenu.add(deleteItem);
        
        optionsMenu.show(eventCard, eventCard.getWidth() - 110, 200);
    }
}