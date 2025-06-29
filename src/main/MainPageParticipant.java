package main;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class MainPageParticipant extends MainPage {
    private JLabel logoLabel; 

    public MainPageParticipant(Participant p) {
        super("Event Participant", p);
        updateLogo();
    }

    public void setUserType(UserRole r) {
        user.role = r;
        // Update the logo label if it exists
        if (logoLabel != null) {
            if (user.role.equals(UserRole.STUDENT)) {
                logoLabel.setText("STUDENT EVENT PORTAL");
            } else if (user.role.equals(UserRole.STAFF)) {
                logoLabel.setText("STAFF EVENT PORTAL");
            } else {
                logoLabel.setText("EVENT PORTAL");
            }
        }
        refreshEventDisplay();
    }

    // Method to refresh the event display based on user type
    private void refreshEventDisplay() {
        // Update window title based on user type
        if (user.role.equals(UserRole.STUDENT)) {
            setTitle("Student Event Portal");
        } else if (user.role.equals(UserRole.STAFF)) {
            setTitle("Staff Event Portal");
        }

        // Refresh the content to show events filtered by user role
        remove(this.contentPanel);
        contentPanel = createContent();
        this.add(contentPanel);
        revalidate();
        repaint();
    }

    @Override
    protected JComponent createCategoryButton() {
        JPanel myEventButton = createStandardCategoryButton("MY EVENT", true);
        myEventButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                remove(MainPageParticipant.this.contentPanel);
                contentPanel = new MyEventsPage(MainPageParticipant.this, (Participant) user);
                MainPageParticipant.this.add(contentPanel);
                revalidate();
                repaint();
            }
        });
        return myEventButton;
    }

    @Override
    protected JComponent createMainMenuBtn() {
        JPanel mainMenuBtn = createStandardCategoryButton("MAIN MENU", true);
        mainMenuBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                remove(MainPageParticipant.this.contentPanel);
                contentPanel = createContent();
                MainPageParticipant.this.add(contentPanel);
                revalidate();
                repaint();
            };
        });
        return mainMenuBtn;
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

        // Read events from CSV file
        List<Event> events = Event.readEventsFromCSV("database/events.csv");
        
        // Filter events based on the current user's role
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.getEventRole().equals(user.role)) {
                filteredEvents.add(event);
            }
        }

        // Create event cards from CSV data
        for (Event event : filteredEvents) {
            String imagePath = getImagePathForEventType(event.getEventType());
            JPanel eventCard = createEventCard(event.getEventID(), event.getEventName(), imagePath);
            eventCard.putClientProperty("EVENT_DATA", event); 
            gridPanel.add(eventCard);
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
        showMorePanel.add(new JLabel("Show " + filteredEvents.size()) {
            {
                setFont(new Font("Arial", Font.PLAIN, 12));
                setForeground(Color.GRAY);
            }
        });

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
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return new JButton() {
                {
                    setPreferredSize(new Dimension(0, 0));
                }
            };
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return new JButton() {
                {
                    setPreferredSize(new Dimension(0, 0));
                }
            };
        }

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
        if (user.role.equals(UserRole.STUDENT)) {
            displayText = "STUDENT EVENT PORTAL";
        } else if (user.role.equals(UserRole.STAFF)) {
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
            if (user.role.equals(UserRole.STUDENT)) {
                logoLabel.setText("STUDENT EVENT PORTAL");
            } else if (user.role.equals(UserRole.STAFF)) {
                logoLabel.setText("STAFF EVENT PORTAL");
            } else {
                logoLabel.setText("EVENT PORTAL");
            }
        }
    }
}