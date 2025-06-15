package main;

import javax.swing.*;
import java.awt.*;

public class MainPageParticipant extends MainPage {
    public MainPageParticipant() {
        super("Available Events (Participant)"); // Set page title
    }

    @Override
    protected JComponent createContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 3, 15, 15)); // 3-column grid
        contentPanel.setBackground(pageBackground);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add mock event cards (replace with real data)
        for (int i = 1; i <= 9; i++) {
            contentPanel.add(createEventCard("Event " + i, "Venue " + i, "Date " + i));
        }

        return new JScrollPane(contentPanel); // Make scrollable
    }
}