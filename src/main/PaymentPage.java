package main;

import javax.swing.*;
import util.SwingUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentPage extends JPanel {
    private MainPageParticipant frame;
    private Event event;
    private int qty;
    private boolean cateringSelected;
    private boolean transportationSelected;
    private boolean paymentMethodSelected = false;
    private final int defaultFontSize = 20;
    private final Font defaultFont = new Font("Arial", Font.PLAIN, defaultFontSize);
    private final int paymentMethodCount = 5;

    public PaymentPage(JFrame f, Event ev, Participant p, int q, boolean c, boolean t) {
        super();
        frame = (MainPageParticipant) f;
        event = ev;
        qty = q;
        cateringSelected = c;
        transportationSelected = t;
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#F8E7F6"));
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setPreferredSize(new Dimension(1200, 500));
        contentPanel.setOpaque(false);
        JPanel summaryPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, 650, 500, 50, 50);
                g2.dispose();
            }
        };
        summaryPanel.setPreferredSize(new Dimension(650, 500));
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setOpaque(false);
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(500, 500));
        rightPanel.setOpaque(false);
        JPanel paymentMethodPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, 500, 400, 50, 50);
                g2.dispose();
            }
        };
        paymentMethodPanel.setPreferredSize(new Dimension(500, 400));
        paymentMethodPanel.setLayout(new BoxLayout(paymentMethodPanel, BoxLayout.Y_AXIS));
        paymentMethodPanel.setOpaque(false);
        JButton payBtn = new JButton("Confirm payment");
        payBtn.setPreferredSize(new Dimension(500, 50));
        payBtn.setBackground(Color.decode("#DD88CF"));
        payBtn.setForeground(Color.WHITE);
        payBtn.setFont(new Font("Arial", Font.BOLD, 20));
        payBtn.setFocusPainted(false);
        payBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!paymentMethodSelected)
                    showErrorMsg();
                else {
                    showThankYouDialog();
                    Ticket ticket = new Ticket(1, ev, null, p, 2, false);
                    List<String> fields = new ArrayList<>();
                    fields.add(String.valueOf(ticket.getTicketID()));
                    fields.add(ticket.getEventID());
                    fields.add(ticket.getEventType());
                    fields.add(ticket.getEventName());
                    fields.add(ticket.getEventDate().format(ticket.getEvent().getFormatter()));
                    fields.add(ticket.getEventTime());
                    fields.add(ticket.getEventVenue());
                    fields.add(ticket.getTicketCode());
                    fields.add(ticket.getParticipantName());
                    fields.add(ticket.getParticipantID());
                    fields.add(ticket.getParticipantEmail());
                    fields.add(String.valueOf(ticket.getPax()));
                    fields.add(String.valueOf(ticket.getEventIsCancelled()));
                    String filePath = "database/tickets_" + ticket.getParticipantID() + ".csv";
                    if (!new File(filePath).exists()) {
                        List<String> header = List.of("Ticket ID", "Event ID", "Event Type", "Event Name", "Event Date",
                                "Event Time", "Event Venue", "Ticket Code", "Participant Name", "Participant ID",
                                "Participant Email", "Pax", "Event Is Cancelled?");
                        try {
                            SwingUtils.writeToCsv(filePath, header);
                        } catch (Exception e1) {
                            System.err.println("Failed to write header to csv file: " + e1.getMessage());
                            e1.printStackTrace();
                        }
                    }
                    try {
                        SwingUtils.writeToCsv(filePath, fields);
                    } catch (Exception e1) {
                        System.err.println("Failed to write row to csv file: " + e1.getMessage());
                        e1.printStackTrace();
                    }
                }
            }
        });

        populateSummary(summaryPanel);
        SwingUtils.applyFontToLabels(summaryPanel, defaultFont);
        populatePaymentMethod(paymentMethodPanel);

        rightPanel.add(paymentMethodPanel, BorderLayout.NORTH);
        rightPanel.add(payBtn, BorderLayout.SOUTH);
        contentPanel.add(summaryPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.EAST);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(contentPanel, gbc);
    }

    private void populateSummary(JPanel panel) {
        double totalFee = event.getEventFee() * qty;
        double extraCharge = 0;
        if (transportationSelected)
            extraCharge += (event.getEventTransportationFee() * qty);
        if (cateringSelected)
            extraCharge += (event.getEventCateringFee() * qty);

        double totalDisc = 0;
        if (event.getEventGrpDiscPercentage() > 0 && qty >= event.getEventGrpDiscReq())
            totalDisc += event.getEventGrpDiscPercentage();
        if (event.getEventEarlyBirdDiscPercentage() > 0
                && LocalDate.now().isBefore(event.getEventEarlyBirdDiscDeadline()))
            totalDisc += event.getEventEarlyBirdDiscPercentage();
        double discAmt = totalDisc / 100 * (totalFee + extraCharge);
        double nettTotal = totalFee + extraCharge - discAmt;
        JLabel nettTotalLabel = new JLabel("Nett total");
        nettTotalLabel.setFont(new Font("Arial", Font.BOLD, defaultFontSize));
        JLabel nettPriceLabel = new JLabel(String.format("%.2f", nettTotal));
        nettPriceLabel.setFont(new Font("Arial", Font.BOLD, defaultFontSize));

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel summaryTitle = new JLabel("Order Summary");
        summaryTitle.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 34));
        summaryTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel totalCalculationGrid = new JPanel(new GridBagLayout());
        totalCalculationGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 2;
        gbc.anchor = GridBagConstraints.WEST;
        totalCalculationGrid.add(new JLabel("Item"), gbc);

        gbc.gridy = 1;
        totalCalculationGrid.add(new JLabel(event.getEventName() + " ticket"), gbc);

        gbc.gridy = 2;
        totalCalculationGrid.add(new JLabel("Additional services"), gbc);

        gbc.gridy = 3;
        totalCalculationGrid.add(new JLabel("Discounts"), gbc);

        gbc.gridy = 4;
        totalCalculationGrid.add(nettTotalLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        totalCalculationGrid.add(new JLabel("Quantity"), gbc);

        gbc.gridy = 1;
        totalCalculationGrid.add(new JLabel(Integer.toString(qty)), gbc);

        gbc.gridy = 3;
        totalCalculationGrid.add(new JLabel(Double.toString(totalDisc) + "%"), gbc);

        gbc.weightx = 0;
        gbc.gridx = 2;
        gbc.gridy = 0;
        totalCalculationGrid.add(new JLabel("Total (RM)"), gbc);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        totalCalculationGrid.add(new JLabel(String.format("%.2f", totalFee)), gbc);

        gbc.gridy = 2;
        totalCalculationGrid.add(new JLabel(String.format("%.2f", extraCharge)), gbc);

        gbc.gridy = 3;
        totalCalculationGrid.add(new JLabel("-" + String.format("%.2f", discAmt)), gbc);

        gbc.gridy = 4;
        totalCalculationGrid.add(nettPriceLabel, gbc);
        panel.add(summaryTitle);
        panel.add(totalCalculationGrid);
    }

    private void populatePaymentMethod(JPanel panel) {
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel paymentMethodLabel = new JLabel("How would you like to pay?");
        paymentMethodLabel.setFont(new Font("Arial", Font.BOLD, 20));
        paymentMethodLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel paymentMethodGrid = new JPanel(new GridLayout(2, 3));
        paymentMethodGrid.setOpaque(false);

        JPanel[] paymentMethodPanels = new JPanel[paymentMethodCount];
        String[] paymentMethodIconPaths = { "src/icon/paymentMethodIcons/grab.png",
                "src/icon/paymentMethodIcons/tng.png",
                "src/icon/paymentMethodIcons/boost.png", "src/icon/paymentMethodIcons/visa_mastercard.png",
                "src/icon/paymentMethodIcons/fpx.png" };
        ImageIcon[] paymentMethodIcons = new ImageIcon[paymentMethodCount];
        JButton[] paymentMethodBtns = new JButton[paymentMethodCount];

        for (int i = 0; i < paymentMethodCount; i++)
            paymentMethodIcons[i] = SwingUtils.loadImage(paymentMethodIconPaths[i], 130, 130);

        for (int i = 0; i < paymentMethodCount; i++) {
            paymentMethodBtns[i] = new JButton();
            paymentMethodBtns[i].setOpaque(false);
            paymentMethodBtns[i].setBorderPainted(false);
            paymentMethodBtns[i].setContentAreaFilled(false);
            paymentMethodBtns[i].setFocusPainted(false);
            paymentMethodBtns[i].setIcon(paymentMethodIcons[i]);
            paymentMethodBtns[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            paymentMethodBtns[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (paymentMethodSelected) {
                        for (JButton otherBtn : paymentMethodBtns)
                            otherBtn.setBorderPainted(false);
                    }
                    paymentMethodSelected = true;
                    JButton btn = (JButton) e.getSource();
                    btn.setBorderPainted(true);
                    btn.setBorder(BorderFactory.createLineBorder(Color.decode("#DD88CF"), 5));
                }
            });
        }

        for (int i = 0; i < paymentMethodCount; i++) {
            paymentMethodPanels[i] = new JPanel(new GridBagLayout());
            paymentMethodPanels[i].setOpaque(false);
            paymentMethodPanels[i].add(paymentMethodBtns[i]);
            paymentMethodGrid.add(paymentMethodPanels[i]);
        }
        panel.add(paymentMethodLabel);
        panel.add(paymentMethodGrid);
    }

    private void showThankYouDialog() {
        JWindow overlayWindow = new JWindow(frame);
        overlayWindow.setBackground(new Color(0, 0, 0, 150));
        overlayWindow.setSize(frame.getSize());
        overlayWindow.setLocation(frame.getLocation());
        overlayWindow.setLayout(null);
        overlayWindow.setVisible(true);
        JDialog tqDialog = new JDialog(frame);
        tqDialog.setUndecorated(true);
        tqDialog.setSize(500, 280);
        tqDialog.setBackground(new Color(0, 0, 0, 0));
        JPanel dialogPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        dialogPanel.setOpaque(false);
        JLabel tickIcon = new JLabel(SwingUtils.loadImage("src/icon/tick.png", 100, 100));
        tickIcon.setAlignmentX(CENTER_ALIGNMENT);
        JLabel label1 = new JLabel("Payment Successful");
        label1.setFont(defaultFont);
        label1.setAlignmentX(CENTER_ALIGNMENT);
        JLabel label2 = new JLabel("You can view your ticket in the \"My Events\" page.");
        label2.setFont(defaultFont);
        label2.setAlignmentX(CENTER_ALIGNMENT);
        JButton backBtn = new JButton("Back to Main Menu");
        backBtn.setBackground(Color.decode("#FF6D97"));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.setPreferredSize(new Dimension(200, 40));
        backBtn.setAlignmentX(CENTER_ALIGNMENT);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tqDialog.dispose();
                frame.remove(frame.contentPanel);
                frame.contentPanel = frame.createContent();
                frame.add(frame.contentPanel);
                frame.revalidate();
                frame.repaint();
            };
        });
        JPanel btnPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        btnPanel.setOpaque(false);
        btnPanel.add(backBtn);
        tqDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                overlayWindow.setVisible(false);
            }
        });
        dialogPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        dialogPanel.add(tickIcon);
        dialogPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        dialogPanel.add(label1);
        dialogPanel.add(label2);
        dialogPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        dialogPanel.add(btnPanel);
        tqDialog.add(dialogPanel, BorderLayout.CENTER);
        tqDialog.setLocationRelativeTo(frame);
        tqDialog.setVisible(true);
    }

    private void showErrorMsg() {
        JPopupMenu errMsgPopup = new JPopupMenu() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(247, 59, 94));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };
        errMsgPopup.setLayout(new FlowLayout());
        JLabel errIcon = new JLabel(SwingUtils.loadImage("src/icon/error.png", 26, 26));
        errIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
        JLabel msg = new JLabel("Please select a payment method!");
        msg.setFont(new Font("Arial", Font.BOLD, defaultFontSize));
        msg.setForeground(Color.WHITE);
        errMsgPopup.setPreferredSize(new Dimension(380, 50));
        errMsgPopup.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        errMsgPopup.add(errIcon);
        errMsgPopup.add(msg);
        int frameWidth = frame.getWidth();
        int popupWidth = errMsgPopup.getPreferredSize().width;
        errMsgPopup.show(frame, (frameWidth - popupWidth) / 2, 124);
    }
}