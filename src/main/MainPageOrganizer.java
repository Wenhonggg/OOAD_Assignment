package main;

import javax.swing.*;
import java.awt.*;

public class MainPageOrganizer extends MainPage {
    public MainPageOrganizer() {
        super("Event Management (Organizer)"); // Different title
    }

    @Override
    protected JComponent createContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout()); // Different layout
        contentPanel.setBackground(pageBackground);

        // Example: Add organizer-specific components (e.g., "Create Event" button)
        JButton createEventButton = new JButton("Create New Event");
        createEventButton.setBackground(cardHeaderColor);
        createEventButton.setForeground(headerTextColor);
        contentPanel.add(createEventButton, BorderLayout.NORTH);

        return contentPanel;
    }
}