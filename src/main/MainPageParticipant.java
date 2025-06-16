package main;

import javax.swing.*;
import java.awt.*;

public class MainPageParticipant extends MainPage {
    public MainPageParticipant() {
        super("Course Overview"); // Set page title
    }

    @Override
    protected JComponent createContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(pageBackground);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add filter controls at the top
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(pageBackground);
        
        JLabel inProgressLabel = new JLabel("In progress");
        inProgressLabel.setFont(new Font("Arial", Font.BOLD, 14));
        filterPanel.add(inProgressLabel);
        
        // Add spacing
        filterPanel.add(Box.createHorizontalStrut(20));
        
        JLabel sortLabel = new JLabel("Sort by last accessed");
        sortLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        sortLabel.setForeground(Color.GRAY);
        filterPanel.add(sortLabel);
        
        contentPanel.add(filterPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Add course cards in a grid
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        gridPanel.setBackground(pageBackground);

        // Add mock course cards (replace with real data) 
        gridPanel.add(createCourseCard("C#10 TO 2010", "CMA6134-COMPUTATIONAL METHODS", "In progress", "icon/celebration.png"));
        gridPanel.add(createCourseCard("C#09 TO 2010", "COP6214-ALGORITHM DESIGN AND ANALYSIS", "In progress", "icon/cyber.png"));
        gridPanel.add(createCourseCard("C#20 TO 2010", "COP6224-0040", "Not started", "icon/earth-day.png"));
        gridPanel.add(createCourseCard("C#30 TO 2010", "CSN6224-COMPUTER NETWORKS", "Completed", "icon/glass.png"));
        gridPanel.add(createCourseCard("C#50 TO 2010", "CCS6214-CYBERSECURITY FUNDAMENTALS", "In progress", "icon/olympia.png"));
        gridPanel.add(createCourseCard("C#10 TO 2010", "CMA6134-COMPUTATIONAL METHODS", "In progress", "icon/singing.png"));
        gridPanel.add(createCourseCard("C#09 TO 2010", "COP6214-ALGORITHM DESIGN AND ANALYSIS", "In progress", "icon/soccer.png"));
        gridPanel.add(createCourseCard("C#20 TO 2010", "COP6224-0040", "Not started", "icon/valentine.png"));
        gridPanel.add(createCourseCard("C#30 TO 2010", "CSN6224-COMPUTER NETWORKS", "Completed", "icon/volunteer.png"));
        gridPanel.add(createCourseCard("C#50 TO 2010", "CCS6214-CYBERSECURITY FUNDAMENTALS", "In progress", "icon/celebration.png"));
        contentPanel.add(gridPanel);
        
        // Add "Show more" at the bottom
        JPanel showMorePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        showMorePanel.setBackground(pageBackground);
        
        JLabel showMoreLabel = new JLabel("Show 12");
        showMoreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        showMoreLabel.setForeground(Color.GRAY);
        showMorePanel.add(showMoreLabel);
        
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(showMorePanel);

        return new JScrollPane(contentPanel); // Make scrollable
    }
}