package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import com.toedter.calendar.JDateChooser;
import util.SwingUtils;

import javax.swing.Timer;

public class Create_Event_Page_Organiser extends JPanel {
    // Modern color palette
    private static final Color PRIMARY_DARK = new Color(30, 41, 59);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);   
    private static final Color SUCCESS_GREEN = new Color(34, 197, 94);
    private static final Color WARNING_ORANGE = new Color(251, 146, 60);
    private static final Color DANGER_RED = new Color(239, 68, 68);
    private static final Color SURFACE_WHITE = new Color(248, 250, 252);
    private static final Color TEXT_DARK = new Color(15, 23, 42);
    private static final Color TEXT_LIGHT = new Color(100, 116, 139);
    private static final Color BORDER_LIGHT = new Color(226, 232, 240);

    // Added "Is Cancelled" column to header
    private static final String[] CSV_HEADER = {
            "Event Code", "Event Name", "Date", "Time", "Venue", "Event Type",
            "Capacity", "Registration Fee", "Event Details", "Role",
            "Group Pax", "Group Discount", "Early Bird Discount", "Early Bird Date",
            "Transportation", "Catering", "Is Cancelled"
    };

    private static final String CSV_FILE_PATH = "database/events.csv";

    private JTextField nameField, timeField, menuField, capacityField;
    private JTextField registrationFeeField, groupPaxField, transportationField, cateringField;
    private JDateChooser dateChooser;
    private JComboBox<String> roleComboBox;
    private JTextField groupDiscountField, earlyBirdDiscountField;
    private JDateChooser earlyBirdDateChooser;
    private JComboBox<String> typeComboBox;
    private JTextArea detailsArea;
    private JList<Event> eventList;
    private DefaultListModel<Event> listModel;
    private Event selectedEvent;
    private JLabel statusLabel;

    public Create_Event_Page_Organiser() {
        initializeComponents();
        setupLayout();
        loadEvents();
    }

    private void initializeComponents() {
        setBackground(SURFACE_WHITE);

        nameField = createModernTextField();
        timeField = createModernTextField();
        menuField = createModernTextField();
        capacityField = createModernTextField();
        registrationFeeField = createModernTextField();
        groupPaxField = createModernTextField();
        groupDiscountField = createModernTextField();
        earlyBirdDiscountField = createModernTextField();
        earlyBirdDateChooser = new JDateChooser();
        earlyBirdDateChooser.setDateFormatString("dd/MM/yyyy");
        earlyBirdDateChooser.getCalendarButton().setText("");
        earlyBirdDateChooser.setBackground(Color.WHITE);
        earlyBirdDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        earlyBirdDateChooser.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_LIGHT, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        transportationField = createModernTextField();
        cateringField = createModernTextField();
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.getCalendarButton().setText("");
        dateChooser.setBackground(Color.WHITE);
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateChooser.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_LIGHT, 1, true),
                new EmptyBorder(8, 12, 8, 12)));

        String[] eventTypes = { "🎓Seminar", "🔧Workshop", "🎭Cultural Event", "⚽Sports Event" };
        typeComboBox = new JComboBox<>(eventTypes);
        styleComboBox(typeComboBox);

        String[] roles = { " Student", " Staff" };
        roleComboBox = new JComboBox<>(roles);
        styleComboBox(roleComboBox);

        detailsArea = new JTextArea(4, 20);
        styleTextArea(detailsArea);

        listModel = new DefaultListModel<>();
        eventList = new JList<>(listModel);
        styleEventList(eventList);

        statusLabel = new JLabel("Ready to create events");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_LIGHT);
    }

    private JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setBackground(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_LIGHT, 1, true),
                new EmptyBorder(12, 16, 12, 16)));
        field.setForeground(TEXT_DARK);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(ACCENT_BLUE, 2, true),
                        new EmptyBorder(11, 15, 11, 15)));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_LIGHT, 1, true),
                        new EmptyBorder(12, 16, 12, 16)));
            }
        });

        return field;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_LIGHT, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        comboBox.setForeground(TEXT_DARK);
    }

    private void styleTextArea(JTextArea textArea) {
        textArea.setBackground(Color.WHITE);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBorder(new EmptyBorder(12, 16, 12, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setForeground(TEXT_DARK);
    }

    private void styleEventList(JList<Event> list) {
        list.setBackground(Color.WHITE);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setSelectionBackground(ACCENT_BLUE);
        list.setSelectionForeground(Color.WHITE);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBorder(new EmptyBorder(8, 8, 8, 8));
        list.setForeground(TEXT_DARK);

        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(new EmptyBorder(12, 16, 12, 16));
                if (!isSelected) {
                    setBackground(index % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
                }
                return this;
            }
        });

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedEvent = list.getSelectedValue();
                if (selectedEvent != null) {
                    populateForm(selectedEvent);
                    updateStatus("Event selected: " + selectedEvent.getEventName());
                }
            }
        });
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_DARK);
        headerPanel.setBorder(new EmptyBorder(24, 32, 24, 32));

        JPanel titleSection = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleSection.setBackground(PRIMARY_DARK);

        JLabel titleLabel = new JLabel("🎪 Event Management Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Create, manage, and organize your events efficiently");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_LIGHT);

        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setBackground(PRIMARY_DARK);
        titleContainer.add(titleLabel, BorderLayout.NORTH);
        titleContainer.add(Box.createVerticalStrut(4), BorderLayout.CENTER);
        titleContainer.add(subtitleLabel, BorderLayout.SOUTH);

        titleSection.add(titleContainer);
        headerPanel.add(titleSection, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(SURFACE_WHITE);
        mainPanel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel formCard = createFormCard();
        JPanel listCard = createListCard();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formCard, listCard);
        splitPane.setDividerLocation(650);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
        splitPane.setBackground(SURFACE_WHITE);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createFormCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_LIGHT, 1, true),
                new EmptyBorder(16, 16, 16, 16)));

        JPanel cardHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardHeader.setBackground(Color.WHITE);
        JLabel cardTitle = new JLabel(" Event Details");
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cardTitle.setForeground(TEXT_DARK);
        cardHeader.add(cardTitle);

        JPanel formContent = createFormContent();
        JScrollPane formScrollPane = new JScrollPane(formContent);
        formScrollPane.setBorder(null);
        formScrollPane.setBackground(Color.WHITE);
        formScrollPane.getViewport().setBackground(Color.WHITE);
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel actionPanel = createActionButtons();

        card.add(cardHeader, BorderLayout.NORTH);
        card.add(formScrollPane, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createFormContent() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 16);
        gbc.anchor = GridBagConstraints.WEST;

        addModernFormField(formPanel, gbc, "Event Name", nameField, 0);
        addModernFormField(formPanel, gbc, "Event Date", dateChooser, 1);
        addModernFormField(formPanel, gbc, "Event Time", timeField, 2);
        addModernFormField(formPanel, gbc, "Event Venue", menuField, 3);
        addModernFormField(formPanel, gbc, "Event Type", typeComboBox, 4);
        addModernFormField(formPanel, gbc, "Event Capacity", capacityField, 5);
        addModernFormField(formPanel, gbc, "Registration Fee", registrationFeeField, 6);
        addModernFormField(formPanel, gbc, "Event Role", roleComboBox, 7);
        addModernFormField(formPanel, gbc, "Group Pax", groupPaxField, 8);
        addModernFormField(formPanel, gbc, "Group Discount", groupDiscountField, 9);
        addModernFormField(formPanel, gbc, "Early Bird Discount", earlyBirdDiscountField, 10);
        addModernFormField(formPanel, gbc, "Early Bird Date", earlyBirdDateChooser, 11);
        addModernFormField(formPanel, gbc, "Transportation", transportationField, 12);
        addModernFormField(formPanel, gbc, "Catering", cateringField, 13);

        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        JLabel detailsLabel = createModernLabel("Event Details");
        formPanel.add(detailsLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setBorder(new LineBorder(BORDER_LIGHT, 1, true));
        scrollPane.setPreferredSize(new Dimension(300, 100));
        formPanel.add(scrollPane, gbc);

        return formPanel;
    }

    private void addModernFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;

        JLabel label = createModernLabel(labelText);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        field.setPreferredSize(new Dimension(350, 40));
        panel.add(field, gbc);
    }

    private JLabel createModernLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(55, 65, 81));
        return label;
    }

    private JPanel createActionButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 16));
        buttonPanel.setBackground(Color.WHITE);

        JButton createBtn = createModernButton(" Create Event", Color.WHITE, Color.BLACK);
        JButton updateBtn = createModernButton(" Update Event", Color.WHITE, Color.BLACK);
        JButton clearBtn = createModernButton(" Clear Form", Color.WHITE, Color.BLACK);

        createBtn.addActionListener(this::createEvent);
        updateBtn.addActionListener(this::updateEvent);
        clearBtn.addActionListener(event -> clearForm());

        buttonPanel.add(createBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(clearBtn);

        return buttonPanel;
    }

    private JPanel createListCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_LIGHT, 1, true),
                new EmptyBorder(16, 16, 16, 16)));

        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setBackground(Color.WHITE);

        JLabel cardTitle = new JLabel("Event List");
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cardTitle.setForeground(TEXT_DARK);

        JButton deleteBtn = createModernButton(" Delete", Color.WHITE, Color.BLACK);
        deleteBtn.addActionListener(this::deleteEvent);

        cardHeader.add(cardTitle, BorderLayout.WEST);
        cardHeader.add(deleteBtn, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(eventList);
        scrollPane.setBorder(new LineBorder(BORDER_LIGHT, 1, true));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        card.add(cardHeader, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }


    private JButton createModernButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_LIGHT, 1, true),
                new EmptyBorder(12, 24, 12, 24)));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color originalBg = background;
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (originalBg.equals(Color.WHITE)) {
                    button.setBackground(SURFACE_WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
            }
        });

        return button;
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
        Timer timer = new Timer(3000, event -> statusLabel.setText("Ready"));
        timer.setRepeats(false);
        timer.start();
    }

    private void createEvent(ActionEvent e) {
        try {
            if (validateForm()) {
                String role = ((String) roleComboBox.getSelectedItem()).substring(1).trim().toUpperCase();
                String eventCode = generateEventCode(role);
                Event event = new Event(
                        eventCode,
                        ((String) typeComboBox.getSelectedItem()).substring(2),
                        nameField.getText(),
                        new SimpleDateFormat("dd/MM/yyyy").format(dateChooser.getDate()),
                        timeField.getText(),
                        menuField.getText(),
                        Integer.parseInt(capacityField.getText()),
                        Double.parseDouble(registrationFeeField.getText()),
                        detailsArea.getText(),
                        role,
                        groupPaxField.getText().isEmpty() ? 0 : Integer.parseInt(groupPaxField.getText()),
                        groupDiscountField.getText().isEmpty() ? 0 : Double.parseDouble(groupDiscountField.getText()),
                        earlyBirdDateChooser.getDate() != null
                                ? new SimpleDateFormat("dd/MM/yyyy").format(earlyBirdDateChooser.getDate())
                                : null,
                        earlyBirdDiscountField.getText().isEmpty() ? 0
                                : Double.parseDouble(earlyBirdDiscountField.getText()),
                        transportationField.getText().isEmpty() ? 0 : Double.parseDouble(transportationField.getText()),
                        cateringField.getText().isEmpty() ? 0 : Double.parseDouble(cateringField.getText()),
                        "src/icon/cyber.png");
                event.setIsCancelled(false);
                listModel.addElement(event);
                saveEventToCSV(event);
                clearForm();
                updateStatus("✅ Event created with code: " + eventCode);
                showModernDialog("Success", "Event created successfully with code: " + eventCode, SUCCESS_GREEN);
            }
        } catch (NumberFormatException ex) {
            showModernDialog("Input Error", "Please enter valid numbers for capacity, fees, and discounts.",
                    DANGER_RED);
        }
    }

    private void updateEvent(ActionEvent e) {
        if (selectedEvent != null) {
            try {
                if (validateForm()) {
                    String newRole = ((String) roleComboBox.getSelectedItem()).substring(1).trim().toUpperCase();
                    String oldRole = selectedEvent.getEventRole().toString();

                    if (!newRole.equals(oldRole)) {
                        String newEventCode = generateEventCode(newRole);
                        selectedEvent.setEventID(newEventCode);
                        updateStatus("✅ Event code updated to: " + newEventCode);
                    }

                    selectedEvent.setEventName(nameField.getText());
                    selectedEvent.setEventDate(new SimpleDateFormat("dd/MM/yyyy").format(dateChooser.getDate()));
                    selectedEvent.setEventTime(timeField.getText());
                    selectedEvent.setEventVenue(menuField.getText());
                    selectedEvent.setEventType(((String) typeComboBox.getSelectedItem()).substring(2));
                    selectedEvent.setEventCapacity(Integer.parseInt(capacityField.getText()));
                    selectedEvent.setEventFee(Double.parseDouble(registrationFeeField.getText()));
                    selectedEvent.setEventDetails(detailsArea.getText());
                    selectedEvent.setEventRole(newRole);
                    selectedEvent.setEventGrpDiscReq(
                            groupPaxField.getText().isEmpty() ? 0 : Integer.parseInt(groupPaxField.getText()));
                    selectedEvent.setEventGrpDiscPercentage(groupDiscountField.getText().isEmpty() ? 0
                            : Double.parseDouble(groupDiscountField.getText()));
                    selectedEvent.setEventEarlyBirdDiscPercentage(earlyBirdDiscountField.getText().isEmpty() ? 0
                            : Double.parseDouble(earlyBirdDiscountField.getText()));
                    selectedEvent.setEventEarlyBirdDiscDeadline(
                            new SimpleDateFormat("dd/MM/yyyy").format(dateChooser.getDate()));
                    selectedEvent.setIsCancelled(false);

                    eventList.repaint();
                    saveAllEventsToCSV();
                    updateStatus("✅ Event updated successfully!");
                    showModernDialog("Success", "Event updated successfully!", SUCCESS_GREEN);
                }
            } catch (Exception ex) {
                showModernDialog("Error", "Error updating event: " + ex.getMessage(), DANGER_RED);
            }
        } else {
            showModernDialog("No Selection", "Please select an event to update.", WARNING_ORANGE);
        }
    }

    private void deleteEvent(ActionEvent e) {
        Event eventToDelete = eventList.getSelectedValue();
        if (eventToDelete != null) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to cancel this event?",
                    "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                eventToDelete.setIsCancelled(true);
                listModel.removeElement(eventToDelete); // Remove from visible list immediately
                saveAllEventsToCSV(); // Save updated cancellation status to CSV
                clearForm();
                updateStatus("🗑️ Event cancelled successfully!");
                showModernDialog("Success", "Event cancelled successfully!", SUCCESS_GREEN);
            }
        } else {
            showModernDialog("No Selection", "Please select an event to cancel.", WARNING_ORANGE);
        }
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty() ||
                dateChooser.getDate() == null ||
                timeField.getText().trim().isEmpty() ||
                menuField.getText().trim().isEmpty() ||
                capacityField.getText().trim().isEmpty() ||
                registrationFeeField.getText().trim().isEmpty()) {

            showModernDialog("Validation Error", "Please fill in all required fields.", DANGER_RED);
            return false;
        }
        return true;
    }

    private void showModernDialog(String title, String message, Color color) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setVisible(true);
    }

    private void populateForm(Event event) {
        nameField.setText(event.getEventName());
        timeField.setText(event.getEventTime());
        menuField.setText(event.getEventVenue());
        transportationField.setText(
                event.getEventTransportationFee() > 0 ? String.valueOf(event.getEventTransportationFee()) : "");
        cateringField.setText(event.getEventCateringFee() > 0 ? String.valueOf(event.getEventCateringFee()) : "");
        try {
            Date earlyDate = new SimpleDateFormat("yyyy-MM-dd").parse(event.getEventEarlyBirdDiscDeadline().toString());
            earlyBirdDateChooser.setDate(earlyDate);
        } catch (Exception ex) {
            earlyBirdDateChooser.setDate(null);
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(event.getEventDate().toString());
            dateChooser.setDate(date);
        } catch (Exception ex) {
            dateChooser.setDate(null);
            System.out.println(ex.getMessage());
        }
        // Set event type combo box: match by type name at end
        String eventType = event.getEventType().toString().trim();
        for (int i = 0; i < typeComboBox.getItemCount(); i++) {
            String item = typeComboBox.getItemAt(i).trim();
            if (item.endsWith(eventType)) {
                typeComboBox.setSelectedIndex(i);
                break;
            }
        }

        capacityField.setText(String.valueOf(event.getEventCapacity()));
        registrationFeeField.setText(String.valueOf(event.getEventFee()));
        detailsArea.setText(event.getEventDetails());

        String role = event.getEventRole().toString();
        roleComboBox.setSelectedIndex(role.equals("STUDENT") ? 0 : 1);

        groupPaxField.setText(event.getEventGrpDiscReq() > 0 ? String.valueOf(event.getEventGrpDiscReq()) : "");
        groupDiscountField.setText(
                event.getEventGrpDiscPercentage() > 0 ? String.valueOf(event.getEventGrpDiscPercentage()) : "");
        earlyBirdDiscountField
                .setText(event.getEventEarlyBirdDiscPercentage() > 0
                        ? String.valueOf(event.getEventEarlyBirdDiscPercentage())
                        : "");

        updateStatus("Form populated with event: " + event.getEventName());
    }

    private String generateEventCode(String role) {
        String prefix = role.toUpperCase().equals("STUDENT") ? "A" : "B";
        int nextNumber = getNextEventNumber(prefix);
        return prefix + String.format("%03d", nextNumber);
    }

    private int getNextEventNumber(String prefix) {
        int maxNumber = 0;
        for (int i = 0; i < listModel.getSize(); i++) {
            Event event = listModel.getElementAt(i);
            String eventCode = event.getEventID();
            if (eventCode.startsWith(prefix) && eventCode.length() >= 4) {
                try {
                    int number = Integer.parseInt(eventCode.substring(1));
                    maxNumber = Math.max(maxNumber, number);
                } catch (NumberFormatException e) {
                }
            }
        }
        return maxNumber + 1;
    }

    private void loadEvents() {
        try {
            if (Files.exists(Paths.get(CSV_FILE_PATH))) {
                List<List<String>> rows = SwingUtils.readFromCsv(CSV_FILE_PATH);

                for (int i = 0; i < rows.size(); i++) {
                    List<String> columns = rows.get(i);

                    if (columns.isEmpty() || columns.size() < 17) {
                        continue;
                    }
                    if (i == 0 || columns.get(0).contains("Event Code")) {
                        continue;
                    }
                    try {
                        String eventCode = columns.get(0).trim();
                        String name = columns.get(1).trim();
                        String date = columns.get(2).trim();
                        String time = columns.get(3).trim();
                        String venue = columns.get(4).trim();
                        String type = columns.get(5).trim();
                        int capacity = Integer.parseInt(columns.get(6).trim());
                        double fee = Double.parseDouble(columns.get(7).trim());
                        String details = columns.get(8).trim();
                        if (details.startsWith("\"") && details.endsWith("\"")) {
                            details = details.substring(1, details.length() - 1);
                            details = details.replace("\"\"", "\"");
                        }
                        String role = columns.get(9).trim();
                        int groupPrice = 0;
                        if (!columns.get(10).trim().equals("N/A")) {
                            groupPrice = Integer.parseInt(columns.get(10).trim());
                        }
                        double groupDiscount = 0;
                        if (!columns.get(11).trim().equals("N/A")) {
                            groupDiscount = Double.parseDouble(columns.get(11).trim());
                        }
                        double earlyDiscount = 0;
                        if (!columns.get(12).trim().equals("N/A")) {
                            earlyDiscount = Double.parseDouble(columns.get(12).trim());
                        }
                        String earlyBirdDate = null;
                        if (!columns.get(13).trim().equals("N/A")) {
                            earlyBirdDate = columns.get(13).trim();
                        }
                        double transportation = 0;
                        double catering = 0;
                        if (columns.size() >= 16) {
                            transportation = Double.parseDouble(columns.get(14).trim());
                            catering = Double.parseDouble(columns.get(15).trim());
                        }
                        boolean isCancelled = false;
                        if (columns.size() > 16) {
                            isCancelled = columns.get(16).trim().equalsIgnoreCase("true");
                        }

                        if (!isCancelled) { // Only add not-cancelled events
                            Event event = new Event(eventCode, type, name, date, time, venue, capacity, fee, details,
                                    role, groupPrice, groupDiscount, earlyBirdDate, earlyDiscount, transportation,
                                    catering, "src/icon/cyber.png");
                            event.setIsCancelled(isCancelled);
                            listModel.addElement(event);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing line: " + columns);
                        e.printStackTrace();
                    }
                }
                updateStatus("Loaded " + listModel.getSize() + " events from CSV");
            }
        } catch (IOException e) {
            updateStatus("No existing events file found - starting fresh");
        }
    }

    private String formatDateForCsv(String dateString, SimpleDateFormat csvDateFormat) {
        if (dateString == null || dateString.isEmpty())
            return "N/A";
        String[] tryFormats = { "dd/MM/yyyy", "yyyy-MM-dd" };
        for (String fmt : tryFormats) {
            try {
                return csvDateFormat.format(new SimpleDateFormat(fmt).parse(dateString));
            } catch (Exception ignore) {
            }
        }
        return dateString;
    }

    private void saveEventToCSV(Event event) {
        try {
            SimpleDateFormat csvDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedEventDate = formatDateForCsv(event.getEventDate().toString(), csvDateFormat);
            String formattedEarlyBirdDate = event.getEventEarlyBirdDiscDeadline() != null
                    ? formatDateForCsv(event.getEventEarlyBirdDiscDeadline().toString(), csvDateFormat)
                    : "N/A";
            String isCancelled = event.getIsCancelled() ? "true" : "false";

            List<String> row = new ArrayList<>(Arrays.asList(
                    event.getEventID(),
                    event.getEventName(),
                    formattedEventDate,
                    event.getEventTime(),
                    event.getEventVenue(),
                    event.getEventType().toString(),
                    String.valueOf(event.getEventCapacity()),
                    String.format("%.2f", event.getEventFee()),
                    "\"" + event.getEventDetails().replace("\"", "\"\"").replace("\n", " ") + "\"",
                    event.getEventRole().toString(),
                    event.getEventGrpDiscReq() > 0 ? String.valueOf(event.getEventGrpDiscReq()) : "N/A",
                    event.getEventGrpDiscPercentage() > 0 ? String.format("%.1f", event.getEventGrpDiscPercentage())
                            : "N/A",
                    event.getEventEarlyBirdDiscPercentage() > 0
                            ? String.format("%.1f", event.getEventEarlyBirdDiscPercentage())
                            : "N/A",
                    formattedEarlyBirdDate,
                    String.valueOf(event.getEventTransportationFee()),
                    String.valueOf(event.getEventCateringFee()),
                    isCancelled));

            File csvFile = new File(CSV_FILE_PATH);
            boolean fileExists = csvFile.exists();

            try (FileWriter fw = new FileWriter(csvFile, true);
                    BufferedWriter bw = new BufferedWriter(fw)) {
                if (!fileExists) {
                    bw.write(String.join(",", CSV_HEADER));
                    bw.newLine();
                }
                bw.write(String.join(",", row));
                bw.newLine();
            }

            updateStatus("Event saved to " + CSV_FILE_PATH);
        } catch (IOException e) {
            showModernDialog("File Error", "Failed to save event: " + e.getMessage(), DANGER_RED);
        }
    }

    private void saveAllEventsToCSV() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE_PATH));
            pw.println(String.join(",", CSV_HEADER));
            pw.close();

            SimpleDateFormat csvDateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < listModel.getSize(); i++) {
                Event event = listModel.getElementAt(i);

                String formattedEventDate = event.getEventDate() != null
                        ? formatDateForCsv(event.getEventDate().toString(), csvDateFormat)
                        : "";
                String formattedEarlyBirdDate = event.getEventEarlyBirdDiscDeadline() != null
                        ? formatDateForCsv(event.getEventEarlyBirdDiscDeadline().toString(), csvDateFormat)
                        : "N/A";
                String isCancelled = event.getIsCancelled() ? "true" : "false";

                List<String> row = new ArrayList<>(Arrays.asList(
                        event.getEventID(),
                        event.getEventName(),
                        formattedEventDate,
                        event.getEventTime(),
                        event.getEventVenue(),
                        event.getEventType().toString(),
                        String.valueOf(event.getEventCapacity()),
                        String.format("%.2f", event.getEventFee()),
                        "\"" + event.getEventDetails().replace("\"", "\"\"").replace("\n", " ") + "\"",
                        event.getEventRole().toString(),
                        event.getEventGrpDiscReq() > 0 ? String.valueOf(event.getEventGrpDiscReq()) : "N/A",
                        event.getEventGrpDiscPercentage() > 0 ? String.format("%.1f", event.getEventGrpDiscPercentage())
                                : "N/A",
                        event.getEventEarlyBirdDiscPercentage() > 0
                                ? String.format("%.1f", event.getEventEarlyBirdDiscPercentage())
                                : "N/A",
                        formattedEarlyBirdDate,
                        String.valueOf(event.getEventTransportationFee()),
                        String.valueOf(event.getEventCateringFee()),
                        isCancelled));
                SwingUtils.writeToCsv(CSV_FILE_PATH, row);
            }

            updateStatus("All events saved to " + CSV_FILE_PATH);
        } catch (IOException e) {
            showModernDialog("File Error", "Failed to save events: " + e.getMessage(), DANGER_RED);
        }
    }

    private void clearForm() {
        nameField.setText("");
        dateChooser.setDate(null);
        timeField.setText("");
        menuField.setText("");
        typeComboBox.setSelectedIndex(0);
        capacityField.setText("");
        registrationFeeField.setText("");
        detailsArea.setText("");
        roleComboBox.setSelectedIndex(0);
        groupPaxField.setText("");
        groupDiscountField.setText("");
        earlyBirdDiscountField.setText("");
        earlyBirdDateChooser.setDate(null);
        transportationField.setText("");
        cateringField.setText("");
        selectedEvent = null;
        eventList.clearSelection();
        updateStatus("Form cleared and ready for new event");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Create a test frame to hold the panel
            JFrame testFrame = new JFrame("Event Management Dashboard");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setSize(1200, 800);
            testFrame.setLocationRelativeTo(null);
            
            Create_Event_Page_Organiser panel = new Create_Event_Page_Organiser();
            testFrame.add(panel);
            testFrame.setVisible(true);
        });
    }
}