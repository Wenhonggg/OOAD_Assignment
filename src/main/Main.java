package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // For participant view
        // SwingUtilities.invokeLater(() -> new MainPageParticipant());

        // For organizer view (uncomment to test)
        SwingUtilities.invokeLater(() -> new MainPageOrganizer());
    }
}