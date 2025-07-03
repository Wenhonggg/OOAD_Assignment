package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

import javax.swing.border.EmptyBorder;

import util.ExcelUtil;

public abstract class MainPage extends JFrame {

    protected String pageTitle;
    protected JComponent contentPanel;
    protected User user;
    List<Event> events;
    protected Color headerBackground = Color.WHITE;
    protected Color headerTextColor = Color.BLACK;
    protected Color cardBackground = Color.WHITE;
    protected Color cardHeaderColor = new Color(144, 238, 144); 
    protected Color pageBackground = new Color(240, 240, 240); 
    protected Color buttonColor = new Color(70, 130, 180); 
    protected Color buttonHoverColor = new Color(100, 149, 237); 

    protected static String getImagePathForEventType(EventType eventType) {
        if (eventType == null) {
            return "icon/celebration.png"; 
        }

        switch (eventType) {
            case SEMINAR:
                return "icon/Seminar.png";
            case WORKSHOP:
                return "icon/Workshop.jpg";
            case SPORTS_EVENT:
                return "icon/SportEvent.jpg";
            case CULTURAL_EVENT:
                return "icon/CultureEvents.jpg";
            default:
                return "icon/celebration.png"; 
        }
    }

    public MainPage(String title, User u) {
        this.pageTitle = title;
        this.user = u;
        events = Event.readEventsFromCSV("database/events.csv");
        setupPage();
    }

    protected abstract JComponent createCategoryButton();
    protected abstract JComponent createMainMenuBtn();
    protected abstract JComponent createContent();
    protected abstract JLabel createLogo();

    private void setupPage() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setLayout(new BorderLayout());
        add(createHeader(), BorderLayout.NORTH);
        contentPanel = createContent();
        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(headerBackground);
        headerPanel.setBorder(new EmptyBorder(13, 0, 10, 20));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        leftPanel.setBackground(headerBackground);
        leftPanel.add(createLogo());

        JComponent mainMenuBtn = createMainMenuBtn();
        if (mainMenuBtn != null) {
            leftPanel.add(mainMenuBtn);
        }

        JComponent categoryButton = createCategoryButton();
        if (categoryButton != null) {
            leftPanel.add(categoryButton);
        }
        headerPanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightPanel.setBackground(headerBackground);
        rightPanel.add(createSearchPanel());
        rightPanel.add(createLogoutButton());
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    protected JPanel createStandardCategoryButton(String text, boolean isActive) {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(headerBackground);

        JLabel buttonLabel = new JLabel(text);
        buttonLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        buttonLabel.setForeground(Color.BLACK);
        buttonLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        buttonPanel.add(buttonLabel, BorderLayout.CENTER);

        buttonPanel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buttonPanel.setBackground(new Color(240, 240, 240));
                buttonPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                buttonPanel.setBackground(headerBackground);
                buttonPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return buttonPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.setColor(Color.GRAY);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                g2.dispose();
            }
        };
        searchPanel.setOpaque(false);
        searchPanel.setPreferredSize(new Dimension(170, 30));

        JTextField searchField = new JTextField(15);
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        searchField.setOpaque(false);
        searchField.setPreferredSize(new Dimension(140, 30));
        searchField.setToolTipText("Search events by name...");
        
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = searchField.getText().trim();
                performSearch(searchText);
            }
        });
        
        searchPanel.add(searchField, BorderLayout.CENTER);

        try {
            ImageIcon searchIcon = new ImageIcon(getClass().getClassLoader().getResource("icon/search_icon.png"));
            Image img = searchIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(img));
            iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
            iconLabel.setOpaque(false);
            searchPanel.add(iconLabel, BorderLayout.EAST);
        } catch (Exception e) {
            System.err.println("Could not load search icon");
        }

        return searchPanel;
    }

    private JLabel createLogoutButton() {
        final JLabel logOutIcon = new JLabel("LOG OUT") {
            boolean isHovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isHovered ? Color.GRAY : Color.BLACK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }

            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        repaint();
                    }

                    public void mouseClicked(MouseEvent e) {
                        dispose();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new LoginPage().setVisible(true);
                            }
                        });
                    }
                });
            }
        };

        logOutIcon.setOpaque(false);
        logOutIcon.setForeground(Color.WHITE);
        logOutIcon.setFont(new Font("Arial", Font.BOLD, 14));
        logOutIcon.setHorizontalAlignment(SwingConstants.CENTER);
        logOutIcon.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logOutIcon.setPreferredSize(new Dimension(100, 30));

        return logOutIcon;
    }

    protected JPanel createEventCard(String eventID, String eventName, String imagePath) {
        final int hoverRise = 10;

        // Create card with rounded corners and hover effect
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(cardBackground);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                g2.setColor(getClientProperty("hovered") != null ? Color.BLACK : Color.LIGHT_GRAY);
                g2.setStroke(new BasicStroke(getClientProperty("hovered") != null ? 2f : 1f));
                g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 30, 30);

                g2.dispose();
            }
        };

        // Add hover effect
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
        });

        card.setOpaque(false);
        card.setBackground(cardBackground);
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        card.setPreferredSize(new Dimension(180, 250));

        // Add image to card with rounded corners
        JLabel imageLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                if (getIcon() != null) {
                    g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                    getIcon().paintIcon(this, g2, 0, 0);
                }
                g2.dispose();
            }
        };

        imageLabel.setOpaque(false);
        imageLabel.setBackground(cardBackground);

        try {
            java.net.URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(420, 190, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                imageLabel.setText("No Image");
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
        } catch (Exception e) {
            imageLabel.setText("No Image");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        card.add(imageLabel, BorderLayout.CENTER);

        // Add info panel to card
        JPanel infoPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };

        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        // Add event ID and name
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel idLabel = new JLabel(eventID);
        idLabel.setFont(new Font("Arial", Font.BOLD, 12));
        leftPanel.add(idLabel);

        JLabel nameLabel = new JLabel(eventName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        leftPanel.add(nameLabel);

        infoPanel.add(leftPanel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Event eventData = (Event) card.getClientProperty("EVENT_DATA");
                boolean isEventOrganizer = user instanceof EventOrganizer;
                MainPage.this.remove(contentPanel);
                MainPage.this.contentPanel = new ViewInfoPage(MainPage.this, eventData, isEventOrganizer);
                MainPage.this.add(contentPanel);
                MainPage.this.revalidate();
                MainPage.this.repaint();
            }
        });

        return card;
    }

    protected void linkTicketsToEvents() {
        // Map for fast access by eventID
        Map<String, Event> eventMap = new HashMap<>();
        List<Event> eventBackup = new ArrayList<>(events);

        // Clear and rebuild the events list
        events.clear();
        for (Event event : eventBackup) {
            eventMap.put(event.getEventID(), event);
        }

        // Build participants
        List<List<String>> users = ExcelUtil.readExcelData("database/users.xlsx", 0);
        List<Participant> participants = new ArrayList<>();
        for (List<String> row : users) {
            String role = row.get(2);
            UserRole userRole = null;
            switch (role.toUpperCase()) {
                case "STUDENT":
                    userRole = UserRole.STUDENT;
                    break;
                case "STAFF":
                    userRole = UserRole.STAFF;
                    break;
            }
            if (!"EO".equalsIgnoreCase(role))
                participants.add(new Participant(row.get(0), userRole));
        }

        // Collect all tickets
        List<Ticket> allTickets = new ArrayList<>();
        for (Participant p : participants) {
            Ticket[] tickets = p.getTickets();
            if (tickets != null)
                allTickets.addAll(Arrays.asList(tickets));
        }

        // Attach tickets as observers to their events
        for (Ticket t : allTickets) {
            Event event = eventMap.get(t.getEventID());
            if (event != null) {
                event.registerObserver(t);
            }
        }

        // Add ALL events back to the list (with observers updated)
        events.addAll(eventBackup);
    }

    protected void performSearch(String searchText) {
    }
}
