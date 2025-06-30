package main;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import util.SwingUtils;

public class MainPageOrganizer extends MainPage {
    private String currentFilter = "All";

    public MainPageOrganizer(EventOrganizer eo) {
        super("Event Organizer", eo);
        System.out.println(events.size());

        linkTicketsToEvents();
    }

    @Override
    protected JComponent createCategoryButton() {
        return null;
    }

    @Override
    protected JComponent createMainMenuBtn() {
        JPanel mainMenuBtn = createStandardCategoryButton("MAIN MENU", true);
        mainMenuBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                remove(MainPageOrganizer.this.contentPanel);
                contentPanel = createContent();
                MainPageOrganizer.this.add(contentPanel);
                revalidate();
                repaint();
            };
        });
        return mainMenuBtn;
    }

    @Override
    protected JComponent createContent() {
        return createContent(currentFilter);
    }

    protected JComponent createContent(String filter) {
        if (filter == null) {
            filter = "All";
        }

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

        // Add the special "Create Event" card as the first card
        JPanel createEventCard = createCreateEventCard();
        gridPanel.add(createEventCard);

        // Read events from CSV file

        // Filter events based on the selected filter
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events) {
            if (filter.equals("All") ||
                    (filter.equals("Student") && event.getEventRole().toString().equalsIgnoreCase("STUDENT")) ||
                    (filter.equals("Staff") && event.getEventRole().toString().equalsIgnoreCase("STAFF"))) {
                filteredEvents.add(event);
            }
        }

        // Add filtered event cards
        for (Event event : filteredEvents) {
            String imagePath = getImagePathForEventType(event.getEventType());
            JPanel eventCard = createEventCard(event.getEventID(), event.getEventName(), imagePath);
            eventCard.putClientProperty("EVENT_DATA", event); // Store the Event object with the card
            gridPanel.add(eventCard);
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
        showMorePanel.add(new JLabel("Show " + (filteredEvents.size() + 1)) {
            { // +1 for the create event card
                setFont(new Font("Arial", Font.PLAIN, 12));
                setForeground(Color.GRAY);
            }
        });
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
        final Color[] bgColor = { Color.BLACK };

        JPanel buttonPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor[0]);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
            }
        };

        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(130, 30));

        JLabel buttonLabel = new JLabel("Filter: " + currentFilter);
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
                    switch (currentFilter) {
                        case "All":
                            currentFilter = "Student";
                            break;
                        case "Student":
                            currentFilter = "Staff";
                            break;
                        case "Staff":
                            currentFilter = "All";
                            break;
                    }
                    filterEventCards(currentFilter);
                }

                bgColor[0] = buttonPanel.contains(e.getPoint()) ? Color.GRAY : Color.BLACK;
                buttonPanel.repaint();
            }
        });

        return buttonPanel;
    }

    private void filterEventCards(String filter) {
        // Remove current content and recreate with filtered events
        remove(MainPageOrganizer.this.contentPanel);
        contentPanel = createContent(filter);
        MainPageOrganizer.this.add(contentPanel);
        revalidate();
        repaint();
    }

    @Override
    protected JPanel createEventCard(String eventID, String eventName, String imagePath) {
        JPanel card = super.createEventCard(eventID, eventName, imagePath);

        // Find the info panel at the bottom of the card
        for (Component c : card.getComponents()) {
            if (c instanceof JPanel && card.getLayout() instanceof BorderLayout &&
                    ((BorderLayout) card.getLayout()).getConstraints(c) == BorderLayout.SOUTH) {

                JPanel infoPanel = (JPanel) c;
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
        final boolean[] isHovered = { false };

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
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered[0] = true;
                circleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                circleButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered[0] = false;
                circleButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                circleButton.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isHovered[0] = true;
                circleButton.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (circleButton.contains(e.getPoint())) {
                    JPanel card = findParentCard(circleButton);
                    if (card != null)
                        showEventOptions(card);
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

        // Get the Event data stored with this card
        Event cardEvent = (Event) eventCard.getClientProperty("EVENT_DATA");

        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(_ -> openCreateEventPage(cardEvent));

        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEvent(cardEvent);
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
                g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 30, 30);

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
        openCreateEventPage(null);
    }

    private void openCreateEventPage(Event eventToEdit) {
        remove(MainPageOrganizer.this.contentPanel);
        System.out.println(events.size());
        contentPanel = new Create_Event_Page_Organiser(eventToEdit, events);
        MainPageOrganizer.this.add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void deleteEvent(Event eventToDelete) {
        if (eventToDelete == null) {
            return;
        }

        // Show confirmation dialog
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel this event '" + eventToDelete.getEventName() + "'?",
                "Confirm Cancel",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            eventToDelete.setIsCancelled(true);
            try {
                // Read all events from CSV
                List<List<String>> allRows = SwingUtils.readFromCsv("database/events.csv");

                if (allRows.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No events found in database.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean eventFound = false;

                // Update the isCancelled column for the matching event
                for (int i = 1; i < allRows.size(); i++) { // skip header
                    List<String> row = allRows.get(i);
                    if (!row.isEmpty() && row.get(0).equals(eventToDelete.getEventID())) {
                        // Set the last column ("Is Cancelled") to "true"
                        if (row.size() < 17) {
                            // pad with empty strings to ensure column 16 exists
                            while (row.size() < 17)
                                row.add("");
                        }
                        row.set(16, "true");
                        eventFound = true;
                    }
                }

                if (!eventFound) {
                    JOptionPane.showMessageDialog(this, "Event not found in database.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Write updated data back to CSV
                java.io.FileWriter clearWriter = new java.io.FileWriter("database/events.csv", false);
                clearWriter.close();

                for (List<String> row : allRows) {
                    SwingUtils.writeToCsv("database/events.csv", row);
                }

                JOptionPane.showMessageDialog(this, "Event cancelled successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Refresh the page/UI to reflect change
                refreshPage();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error cancelling event: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshPage() {
        // Remove current content and recreate with current filter
        remove(MainPageOrganizer.this.contentPanel);
        contentPanel = createContent(currentFilter);
        MainPageOrganizer.this.add(contentPanel);
        revalidate();
        repaint();
    }
}