package main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

import util.SwingUtils;

public class Ticket implements Observer {
    private int ticketID;
    private Event event;
    private Participant participant;
    private String registrationType;
    private int pax;
    private String ticketCode;
    private boolean eventIsCancelled;
    private Font defaultFont = new Font("Segoe UI", Font.PLAIN, 20);
    private Font defaultBoldFont = new Font("Segoe UI", Font.BOLD, 20);
    private Color codePanelColour;

    public Ticket(int ticID, Event ev, String ticCode, Participant p, int qty, boolean cancel) {
        super();
        ticketID = ticID;
        event = ev;
        participant = p;
        pax = qty;
        eventIsCancelled = cancel;
        if (pax > 1)
            registrationType = "Group";
        else
            registrationType = "Individual";

        if (ticCode == null)
            ticketCode = generateTicketCode();
        else
            ticketCode = ticCode;

        switch (event.getEventType().toString().toUpperCase()) {
            case "SEMINAR":
                codePanelColour = new Color(208, 189, 252);
                break;
            case "WORKSHOP":
                codePanelColour = new Color(140, 177, 250);
                break;
            case "CULTURAL EVENT":
                codePanelColour = new Color(252, 230, 189);
                break;
            case "SPORTS EVENT":
                codePanelColour = new Color(252, 189, 228);
                break;
            default:
                codePanelColour = new Color(230, 227, 227);
                break;
        }
    }

    public int getTicketID() {
        return ticketID;
    }

    public String getEventID() {
        return event.getEventID();
    }

    public Event getEvent() {
        return event;
    }

    public String getEventType() {
        return event.getEventType().toString();
    }

    public String getEventName() {
        return event.getEventName();
    }

    public LocalDate getEventDate() {
        return event.getEventDate();
    }

    public String getEventTime() {
        return event.getEventTime();
    }

    public String getEventVenue() {
        return event.getEventVenue();
    }

    public String getParticipantName() {
        return participant.getName();
    }

    public String getParticipantID() {
        return participant.getID();
    }

    public String getParticipantEmail() {
        return participant.getEmail();
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public int getPax() {
        return pax;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public boolean getEventIsCancelled() {
        return eventIsCancelled;
    }

    public void setEventIsCancelled(boolean c) {
        eventIsCancelled = c;
    }

    private String generateTicketCode() {
        String chr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 10; i++)
            result.append(chr.charAt(rand.nextInt(chr.length())));
        return result.toString();
    }

    public JComponent display() {
        if (eventIsCancelled) {
            JLabel notice = new JLabel(
                    "<html><div style='text-align:center;'>Unfortunately, this event has been CANCELLED.<br>The amount paid has been refunded to your account.</div></html>");
            notice.setFont(new Font("Calibri", Font.BOLD, 20));
            notice.setForeground(Color.RED);
            return notice;
        }
        JPanel ticket = new JPanel(new BorderLayout());
        ticket.setPreferredSize(new Dimension(600, 600));
        ticket.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        ticket.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        JPanel eventTypePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRoundRect(0, 0, getPreferredSize().width, getPreferredSize().height, 5, 5);
                g2.dispose();
            }
        };
        eventTypePanel.setLayout(new BoxLayout(eventTypePanel, BoxLayout.Y_AXIS));
        JLabel eventTypeLabel = new JLabel(getEventType().toUpperCase());
        eventTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        eventTypeLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        eventTypePanel.add(eventTypeLabel);
        JLabel eventNameLabel = new JLabel("<html>" + getEventName() + "</html>");
        eventNameLabel.setFont(new Font("Cooper Black", Font.PLAIN, 40));
        JLabel eventDateTimeLabel = new JLabel(
                getEventDate().format(event.getFormatter()) + " \u00B7 " + getEventTime() + " ");
        eventDateTimeLabel.setFont(new Font("Arial Narrow", Font.ITALIC, 20));
        JLabel eventVenueLabel = new JLabel(getEventVenue());
        eventVenueLabel.setFont(new Font("Arial Narrow", Font.PLAIN, 20));
        topPanel.add(eventTypePanel);
        topPanel.add(eventNameLabel);
        topPanel.add(eventDateTimeLabel);
        topPanel.add(eventVenueLabel);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setOpaque(false);
        JPanel codePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(codePanelColour);
                g2.fillRoundRect(0, 0, 500, 150, 15, 15);
                g2.dispose();
            }
        };
        codePanel.setPreferredSize(new Dimension(500, 150));
        codePanel.setMaximumSize(new Dimension(500, 150));
        codePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        codePanel.setLayout(new GridBagLayout());
        JLabel codeTextLabel = new JLabel("Your ticket code is:");
        codeTextLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JLabel ticketCodeLabel = new JLabel(ticketCode);
        ticketCodeLabel.setFont(new Font("Consolas", Font.BOLD, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 2;
        codePanel.add(codeTextLabel, gbc);
        gbc.gridy = 1;
        gbc.weighty = 1;
        codePanel.add(ticketCodeLabel, gbc);
        JPanel participantNamePanel = new JPanel(new BorderLayout());
        participantNamePanel.setOpaque(false);
        JLabel nameLabel = new JLabel("Participant name: ");
        nameLabel.setFont(defaultFont);
        JLabel participantNameLabel = new JLabel(
                "<html><div align='right' style='width:300px;'>" + participant.getName() + "</div></html>");
        participantNameLabel.setFont(defaultBoldFont);
        participantNamePanel.add(nameLabel, BorderLayout.WEST);
        participantNamePanel.add(participantNameLabel, BorderLayout.EAST);
        participantNamePanel.setMaximumSize(new Dimension(600, participantNamePanel.getPreferredSize().height));
        JPanel participantIDPanel = new JPanel(new BorderLayout());
        participantIDPanel.setOpaque(false);
        JLabel IDLabel = new JLabel("ID: ");
        IDLabel.setFont(defaultFont);
        JLabel participantIDLabel = new JLabel(participant.getID());
        participantIDLabel.setFont(defaultBoldFont);
        participantIDPanel.add(IDLabel, BorderLayout.WEST);
        participantIDPanel.add(participantIDLabel, BorderLayout.EAST);
        participantIDPanel.setMaximumSize(new Dimension(600, participantIDPanel.getPreferredSize().height));
        JPanel participantEmailPanel = new JPanel(new BorderLayout());
        participantEmailPanel.setOpaque(false);
        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setFont(defaultFont);
        JLabel participantEmailLabel = new JLabel(participant.getEmail());
        participantEmailLabel.setFont(defaultBoldFont);
        participantEmailPanel.add(emailLabel, BorderLayout.WEST);
        participantEmailPanel.add(participantEmailLabel, BorderLayout.EAST);
        participantEmailPanel.setMaximumSize(new Dimension(600, participantEmailPanel.getPreferredSize().height));
        JPanel paxPanel = new JPanel(new BorderLayout());
        paxPanel.setOpaque(false);
        JLabel paxTextLabel = new JLabel("Pax: ");
        paxTextLabel.setFont(defaultFont);
        JLabel paxLabel = new JLabel(Integer.toString(pax));
        paxLabel.setFont(defaultBoldFont);
        paxPanel.add(paxTextLabel, BorderLayout.WEST);
        paxPanel.add(paxLabel, BorderLayout.EAST);
        paxPanel.setMaximumSize(new Dimension(600, paxPanel.getPreferredSize().height));
        JPanel regTypePanel = new JPanel(new BorderLayout());
        regTypePanel.setOpaque(false);
        JLabel regTypeTextLabel = new JLabel("Registration type: ");
        regTypeTextLabel.setFont(defaultFont);
        JLabel regTypeLabel = new JLabel(registrationType);
        regTypeLabel.setFont(defaultBoldFont);
        regTypePanel.add(regTypeTextLabel, BorderLayout.WEST);
        regTypePanel.add(regTypeLabel, BorderLayout.EAST);
        regTypePanel.setMaximumSize(new Dimension(600, regTypePanel.getPreferredSize().height));
        middlePanel.add(Box.createRigidArea(new Dimension(0, 60)));
        middlePanel.add(codePanel);
        middlePanel.add(Box.createRigidArea(new Dimension(0, 60)));
        middlePanel.add(participantNamePanel);
        middlePanel.add(participantIDPanel);
        middlePanel.add(participantEmailPanel);
        middlePanel.add(paxPanel);
        middlePanel.add(regTypePanel);
        middlePanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(0, 20));
        bottomPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JLabel ticID = new JLabel("Ticket ID: " + Integer.toString(ticketID) + " ");
        ticID.setFont(new Font("Arial Narrow", Font.ITALIC, 16));
        ticID.setForeground(Color.GRAY);
        bottomPanel.add(ticID, BorderLayout.WEST);

        ticket.add(topPanel, BorderLayout.NORTH);
        ticket.add(middlePanel, BorderLayout.CENTER);
        ticket.add(bottomPanel, BorderLayout.SOUTH);
        return ticket;
    }

    public JPanel ticketListItem(MyEventsPage page, JPanel rightPanel) {
        String iconPath = null;
        switch (getEventType().toUpperCase()) {
            case "SEMINAR":
                iconPath = "src/icon/eventTypeIcons/seminar.png";
                break;
            case "WORKSHOP":
                iconPath = "src/icon/eventTypeIcons/workshop.png";
                break;
            case "SPORTS EVENT":
                iconPath = "src/icon/eventTypeIcons/sports.png";
                break;
            case "CULTURAL EVENT":
                iconPath = "src/icon/eventTypeIcons/culture.png";
                break;
            default:
                break;
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.setBackground(Color.WHITE);
        JPanel iconPanel = new JPanel();
        JLabel icon = new JLabel(SwingUtils.loadImage(iconPath, 40, 40));
        iconPanel.add(icon);
        iconPanel.setMaximumSize(new Dimension(50, 50));
        iconPanel.setOpaque(false);
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setOpaque(false);
        JLabel eventNameLabel = new JLabel(getEventName());
        eventNameLabel.setFont(new Font("Serif", Font.BOLD, 26));
        JLabel eventDetailsLabel = new JLabel(getEventDate().format(event.getFormatter()) + ", " + getEventTime());
        eventDetailsLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        middlePanel.add(Box.createVerticalGlue());
        middlePanel.add(eventNameLabel);
        middlePanel.add(eventDetailsLabel);
        middlePanel.add(Box.createVerticalGlue());
        JLabel arrow = new JLabel(">");
        arrow.setFont(new Font("Agency FB", Font.PLAIN, 50));
        panel.add(iconPanel);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(middlePanel);
        panel.add(Box.createHorizontalGlue());
        panel.add(arrow);
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                page.setSelectedPanel(panel);
                rightPanel.removeAll();
                rightPanel.add(display());
                rightPanel.revalidate();
                rightPanel.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (panel != page.getSelectedPanel()) {
                    panel.setBackground(new Color(227, 225, 227));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (panel != page.getSelectedPanel())
                    panel.setBackground(Color.WHITE);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
        return panel;
    }

    @Override
    public void update(boolean isCancelled, String eventID) {
        System.out.println("in Ticket.update()");
        eventIsCancelled = isCancelled;
        Ticket[] tickets = participant.getTickets();
        List<List<String>> data = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getTicketID() == ticketID && t.getEventID().equals(eventID)) {
                System.out.println("Matching ticket ID!");
                t.setEventIsCancelled(true);
            }
            List<String> fields = List.of(String.valueOf(t.getTicketID()), t.getEventID(), t.getEventType(),
                    t.getEventName(),
                    t.getEventDate().format(event.getFormatter()), t.getEventTime(), t.getEventVenue(),
                    t.getTicketCode(),
                    t.getParticipantName(), t.getParticipantID(), t.getParticipantEmail(),
                    String.valueOf(t.getPax()),
                    String.valueOf(t.getEventIsCancelled()));
            System.out.println(t.getEventIsCancelled());
            data.add(fields);
        }
        try {
            List<String> header = List.of("Ticket ID", "Event ID", "Event Type", "Event Name", "Event Date",
                    "Event Time", "Event Venue", "Ticket Code", "Participant Name", "Participant ID",
                    "Participant Email", "Pax", "Event Is Cancelled?");
            SwingUtils.writeToCsv("database/tickets_" + getParticipantID() + ".csv", data, false, header);

        } catch (Exception e) {
            System.err.println("Failed to update ticket in csv file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}