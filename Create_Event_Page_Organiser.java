
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Timer;

public class Create_Event_Page_Organiser extends JFrame {
  // Modern color palette
private static final Color PRIMARY_DARK = new Color(30, 41, 59);      // Slate 800
private static final Color PRIMARY_MEDIUM = new Color(51, 65, 85);    // Slate 700
private static final Color PRIMARY_LIGHT = new Color(71, 85, 105);    // Slate 600
private static final Color ACCENT_BLUE = new Color(59, 130, 246);     // Blue 500
private static final Color ACCENT_HOVER = new Color(37, 99, 235);     // Blue 600
private static final Color SUCCESS_GREEN = new Color(34, 197, 94);    // Green 500
private static final Color WARNING_ORANGE = new Color(251, 146, 60);  // Orange 400
private static final Color DANGER_RED = new Color(239, 68, 68);       // Red 500
private static final Color SURFACE_WHITE = new Color(248, 250, 252);  // Slate 50
private static final Color TEXT_DARK = new Color(15, 23, 42);         // Slate 900
private static final Color TEXT_LIGHT = new Color(100, 116, 139);     // Slate 500
private static final Color BORDER_LIGHT = new Color(226, 232, 240);   // Slate 200

private static final String CSV_FILE_PATH = "events.csv";
private static final String[] CSV_HEADER = {
    "Event Code", "Event Name", "Date", "Time", "Venue", "Event Type", 
    "Capacity", "Registration Fee", "Event Details", "Role", 
    "Group Pax", "Group Discount", "Early Bird Discount", "Early Bird Date"
};


    private JTextField nameField, dateField, timeField, menuField, capacityField;
    private JTextField registrationFeeField, groupPaxField;
    private JComboBox<String> roleComboBox;
    private JTextField groupDiscountField, earlyBirdDiscountField, earlyDateField;
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
        setVisible(true);
    }

    private void initializeComponents() {
        setTitle("Event Management Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(SURFACE_WHITE);

        
        nameField = createModernTextField();
        dateField = createModernTextField();
        timeField = createModernTextField();
        menuField = createModernTextField();
        capacityField = createModernTextField();
        registrationFeeField = createModernTextField();
        groupPaxField = createModernTextField();
        groupDiscountField = createModernTextField();
        earlyBirdDiscountField = createModernTextField();
        earlyDateField = createModernTextField();

       
        String[] eventTypes = {"🎓 Seminars", "🔧 Workshops", "🎭 Cultural Events", "⚽ Sports Events"};
        typeComboBox = new JComboBox<>(eventTypes);
        styleComboBox(typeComboBox);

        String[] roles = {" Student", " Staff"};
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
            new EmptyBorder(12, 16, 12, 16)
        ));
        field.setForeground(TEXT_DARK);
        
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(ACCENT_BLUE, 2, true),
                    new EmptyBorder(11, 15, 11, 15)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_LIGHT, 1, true),
                    new EmptyBorder(12, 16, 12, 16)
                ));
            }
        });
        
        return field;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_LIGHT, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
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
                    updateStatus("Event selected: " + selectedEvent.getName());
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

        
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
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
            new EmptyBorder(16, 16, 16, 16)
        ));

        // Card header
        JPanel cardHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardHeader.setBackground(Color.WHITE);
        JLabel cardTitle = new JLabel(" Event Details");
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cardTitle.setForeground(TEXT_DARK);
        cardHeader.add(cardTitle);

        // Form content with scroll pane
        JPanel formContent = createFormContent();
        JScrollPane formScrollPane = new JScrollPane(formContent);
        formScrollPane.setBorder(null);
        formScrollPane.setBackground(Color.WHITE);
        formScrollPane.getViewport().setBackground(Color.WHITE);
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Action buttons
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

        // Add form fields with modern labels
        addModernFormField(formPanel, gbc, "Event Name", nameField, 0);
        addModernFormField(formPanel, gbc, "Event Date", dateField, 1);
        addModernFormField(formPanel, gbc, "Event Time", timeField, 2);
        addModernFormField(formPanel, gbc, "Event Venue", menuField, 3);
        addModernFormField(formPanel, gbc, "Event Type", typeComboBox, 4);
        addModernFormField(formPanel, gbc, "Event Capacity", capacityField, 5);
        addModernFormField(formPanel, gbc, "Registration Fee", registrationFeeField, 6);
        addModernFormField(formPanel, gbc, "Event Role", roleComboBox, 7);
        addModernFormField(formPanel, gbc, "Group Pax", groupPaxField, 8);
        addModernFormField(formPanel, gbc, "Group Discount", groupDiscountField, 9);
        addModernFormField(formPanel, gbc, "Early Bird Discount", earlyBirdDiscountField, 10);
        addModernFormField(formPanel, gbc, "Early Bird Date", earlyDateField, 11);

        // Details area
        gbc.gridx = 0; gbc.gridy = 12; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0; gbc.weighty = 0;
        JLabel detailsLabel = createModernLabel("Event Details");
        formPanel.add(detailsLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 12; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setBorder(new LineBorder(BORDER_LIGHT, 1, true));
        scrollPane.setPreferredSize(new Dimension(300, 100));
        formPanel.add(scrollPane, gbc);

        return formPanel;
    }

    private void addModernFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0; gbc.weighty = 0;
        
        JLabel label = createModernLabel(labelText);
        panel.add(label, gbc);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        field.setPreferredSize(new Dimension(350, 40));
        panel.add(field, gbc);
    }

    private JLabel createModernLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(55, 65, 81)); // Gray 700
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
        clearBtn.addActionListener(e -> clearForm());

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
            new EmptyBorder(16, 16, 16, 16)
        ));

        // Card header
        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setBackground(Color.WHITE);
        
        JLabel cardTitle = new JLabel("Event List");
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cardTitle.setForeground(TEXT_DARK);
        
        JButton deleteBtn = createModernButton(" Delete", Color.WHITE, Color.BLACK);
        deleteBtn.addActionListener(this::deleteEvent);
        
        cardHeader.add(cardTitle, BorderLayout.WEST);
        cardHeader.add(deleteBtn, BorderLayout.EAST);

        // List content
        JScrollPane scrollPane = new JScrollPane(eventList);
        scrollPane.setBorder(new LineBorder(BORDER_LIGHT, 1, true));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        card.add(cardHeader, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(PRIMARY_DARK);
        footerPanel.setBorder(new EmptyBorder(16, 32, 16, 32));

        statusLabel.setForeground(Color.WHITE);
        footerPanel.add(statusLabel, BorderLayout.WEST);

        JButton backBtn = createModernButton("← Back to Main", ACCENT_BLUE, Color.WHITE);
        backBtn.addActionListener(e -> {
            dispose();
        });
        footerPanel.add(backBtn, BorderLayout.EAST);

        return footerPanel;
    }

    private JButton createModernButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_LIGHT, 1, true),
            new EmptyBorder(12, 24, 12, 24)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effects
        Color originalBg = background;
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SURFACE_WHITE);
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
        Timer timer = new Timer(3000, e -> statusLabel.setText("Ready"));
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
            nameField.getText(),
            dateField.getText(),
            timeField.getText(),
            menuField.getText(),
            ((String) typeComboBox.getSelectedItem()).substring(2),
            Integer.parseInt(capacityField.getText()),
            Double.parseDouble(registrationFeeField.getText()),
            detailsArea.getText(),
            role,
            groupPaxField.getText().isEmpty() ? 0 : Double.parseDouble(groupPaxField.getText()),
            groupDiscountField.getText().isEmpty() ? 0 : Double.parseDouble(groupDiscountField.getText()),
            earlyBirdDiscountField.getText().isEmpty() ? 0 : Double.parseDouble(earlyBirdDiscountField.getText()),
            earlyDateField.getText().isEmpty() ? null : earlyDateField.getText()
);
            
            listModel.addElement(event);
            saveEventToCSV(event); // Save to CSV
            clearForm();
            updateStatus("✅ Event created with code: " + eventCode);
            showModernDialog("Success", "Event created successfully with code: " + eventCode, SUCCESS_GREEN);
        }
    } catch (NumberFormatException ex) {
        showModernDialog("Input Error", "Please enter valid numbers for capacity, fees, and discounts.", DANGER_RED);
    }
}

private void updateEvent(ActionEvent e) {
    if (selectedEvent != null) {
        try {
            if (validateForm()) {
                String newRole = ((String) roleComboBox.getSelectedItem()).substring(1).trim().toUpperCase();
                String oldRole = selectedEvent.getRole();
                
                // If role changed, generate new event code
                if (!newRole.equals(oldRole)) {
                    String newEventCode = generateEventCode(newRole);
                    selectedEvent.setEventCode(newEventCode);
                    updateStatus("✅ Event code updated to: " + newEventCode);
                }
                
                // Update the selected event object
                selectedEvent.setName(nameField.getText());
                selectedEvent.setDate(dateField.getText());
                selectedEvent.setTime(timeField.getText());
                selectedEvent.setVenue(menuField.getText());
                selectedEvent.setType(((String) typeComboBox.getSelectedItem()).substring(2));
                selectedEvent.setCapacity(Integer.parseInt(capacityField.getText()));
                selectedEvent.setRegistrationFee(Double.parseDouble(registrationFeeField.getText()));
                selectedEvent.setDetails(detailsArea.getText());
                selectedEvent.setRole(newRole);
                selectedEvent.setGroupPrice(groupPaxField.getText().isEmpty() ? 0 : Double.parseDouble(groupPaxField.getText()));
                selectedEvent.setGroupDiscount(groupDiscountField.getText().isEmpty() ? 0 : Double.parseDouble(groupDiscountField.getText()));
                selectedEvent.setEarlyBirdDiscount(earlyBirdDiscountField.getText().isEmpty() ? 0 : Double.parseDouble(earlyBirdDiscountField.getText()));
                selectedEvent.setEarlyBirdDate(earlyDateField.getText().isEmpty() ? null : earlyDateField.getText());
                
                // Update the list display
                eventList.repaint();
                
                // Save all events back to CSV
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
    if (eventList.getSelectedValue() != null) {
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete this event?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            listModel.removeElement(eventList.getSelectedValue());
            clearForm();
            
            // Save updated list back to CSV
            saveAllEventsToCSV();
            
            updateStatus("🗑️ Event deleted successfully!");
            showModernDialog("Success", "Event deleted successfully!", SUCCESS_GREEN);
        }
    } else {
        showModernDialog("No Selection", "Please select an event to delete.", WARNING_ORANGE);
    }
}

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty() ||
            dateField.getText().trim().isEmpty() ||
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
    nameField.setText(event.getName());
    dateField.setText(event.getDate());
    timeField.setText(event.getTime());
    menuField.setText(event.getVenue());
    
    // Set event type combo box
    String eventType = event.getType();
    for (int i = 0; i < typeComboBox.getItemCount(); i++) {
        if (typeComboBox.getItemAt(i).contains(eventType)) {
            typeComboBox.setSelectedIndex(i);
            break;
        }
    }
    
    capacityField.setText(String.valueOf(event.getCapacity()));
    registrationFeeField.setText(String.valueOf(event.getRegistrationFee()));
    detailsArea.setText(event.getDetails());
    
    // Set role combo box
    String role = event.getRole();
    roleComboBox.setSelectedIndex(role.equals("STUDENT") ? 0 : 1);
    
    groupPaxField.setText(event.getGroupPrice() > 0 ? String.valueOf(event.getGroupPrice()) : "");
    groupDiscountField.setText(event.getGroupDiscount() > 0 ? String.valueOf(event.getGroupDiscount()) : "");
    earlyBirdDiscountField.setText(event.getEarlyBirdDiscount() > 0 ? String.valueOf(event.getEarlyBirdDiscount()) : "");
    
    updateStatus("Form populated with event: " + event.getName());
}

    private String generateEventCode(String role) {
    String prefix = role.toUpperCase().equals("STUDENT") ? "A" : "B";
    int nextNumber = getNextEventNumber(prefix);
    return prefix + String.format("%03d", nextNumber);
}

private int getNextEventNumber(String prefix) {
    int maxNumber = 0;
    
    // Check existing events in the list model
    for (int i = 0; i < listModel.getSize(); i++) {
        Event event = listModel.getElementAt(i);
        String eventCode = event.getEventCode();
        if (eventCode.startsWith(prefix) && eventCode.length() >= 4) {
            try {
                int number = Integer.parseInt(eventCode.substring(1));
                maxNumber = Math.max(maxNumber, number);
            } catch (NumberFormatException e) {
                // Skip invalid codes
            }
        }
    }
    
    return maxNumber + 1;
}

private void loadEvents() {
    try {
        if (Files.exists(Paths.get(CSV_FILE_PATH))) {
            List<String> lines = Files.readAllLines(Paths.get(CSV_FILE_PATH));
            
            for (String line : lines) {
                // Skip separator lines (lines starting with +) and empty lines
                if (line.trim().isEmpty() || line.trim().startsWith("+")) {
                    continue;
                }
                
                // Skip header line (contains "Event Code")
                if (line.contains("Event Code")) {
                    continue;
                }
                
                // Parse table row format: | data | data | data |
                if (line.trim().startsWith("|") && line.trim().endsWith("|")) {
                    // Remove first and last | and split by |
                    String cleanLine = line.trim().substring(1, line.trim().length() - 1);
                    String[] parts = cleanLine.split("\\|");
                    
                    // Trim whitespace from each part
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].trim();
                    }
                    
                    if (parts.length >= 14) {
                        try {
                            String eventCode = parts[0];
                            String name = parts[1];
                            String date = parts[2];
                            String time = parts[3];
                            String venue = parts[4];
                            String type = parts[5];
                            int capacity = Integer.parseInt(parts[6]);
                            
                            // Parse fee (remove RM prefix)
                            String feeStr = parts[7].replace("RM", "");
                            double fee = Double.parseDouble(feeStr);
                            
                            String details = parts[8];
                            String role = parts[9];
                            
                            // Parse group price
                            double groupPrice = 0;
                            if (!parts[10].equals("N/A")) {
                                groupPrice = Double.parseDouble(parts[10]);
                            }
                            
                            // Parse group discount
                            double groupDiscount = 0;
                            if (!parts[11].equals("N/A")) {
                                groupDiscount = Double.parseDouble(parts[11].replace("%", ""));
                            }
                            
                            // Parse early bird discount
                            double earlyDiscount = 0;
                            if (!parts[12].equals("N/A")) {
                                earlyDiscount = Double.parseDouble(parts[12].replace("%", ""));
                            }
                            
                            // Parse early bird date
                            String earlyBirdDate = null;
                            if (!parts[13].equals("N/A")) {
                                earlyBirdDate = parts[13];
                            }
                            
                            Event event = new Event(eventCode, name, date, time, venue, type, capacity, fee, details, role, groupPrice, groupDiscount, earlyDiscount, earlyBirdDate);
                            listModel.addElement(event);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing line: " + line);
                        }
                    }
                }
            }
            updateStatus("Loaded " + listModel.getSize() + " events from CSV");
        }
    } catch (IOException e) {
        updateStatus("No existing events file found - starting fresh");
    }
}


    // Simple Event class for demonstration
    static class Event {
        private String eventCode, name, date, time, venue, type, details, role, earlyBirdDate;
        private int capacity;
        private double registrationFee, groupPrice, groupDiscount, earlyBirdDiscount;

        public Event(String eventCode, String name, String date, String time, String venue, String type, 
            int capacity, double registrationFee, String details, String role,
            double groupPrice, double groupDiscount, double earlyBirdDiscount, String earlyBirdDate) {
            this.eventCode = eventCode;
            this.name = name;
            this.date = date;
            this.time = time;
            this.venue = venue;
            this.type = type;
            this.capacity = capacity;
            this.registrationFee = registrationFee;
            this.details = details;
            this.role = role;
            this.groupPrice = groupPrice;
            this.groupDiscount = groupDiscount;
            this.earlyBirdDiscount = earlyBirdDiscount;
            this.earlyBirdDate = earlyBirdDate;
        }

        // Getters and setters
        public String getEarlyBirdDate() { return earlyBirdDate; }
        public void setEarlyBirdDate(String earlyBirdDate) { this.earlyBirdDate = earlyBirdDate; }
        public String getEventCode() { return eventCode; }
        public void setEventCode(String eventCode) { this.eventCode = eventCode; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
        public String getVenue() { return venue; }
        public void setVenue(String venue) { this.venue = venue; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDetails() { return details; }
        public void setDetails(String details) { this.details = details; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public int getCapacity() { return capacity; }
        public void setCapacity(int capacity) { this.capacity = capacity; }
        public double getRegistrationFee() { return registrationFee; }
        public void setRegistrationFee(double registrationFee) { this.registrationFee = registrationFee; }
        public double getGroupPrice() { return groupPrice; }
        public void setGroupPrice(double groupPrice) { this.groupPrice = groupPrice; }
        public double getGroupDiscount() { return groupDiscount; }
        public void setGroupDiscount(double groupDiscount) { this.groupDiscount = groupDiscount; }
        public double getEarlyBirdDiscount() { return earlyBirdDiscount; }
        public void setEarlyBirdDiscount(double earlyBirdDiscount) { this.earlyBirdDiscount = earlyBirdDiscount; }

@Override
public String toString() {
    return eventCode + " - " + name + " - " + date + " at " + time;
}
    }

private void saveAllEventsToCSV() {
    try {
        List<String[]> allRows = new ArrayList<>();
        
        // Add header
        allRows.add(CSV_HEADER);
        
        // Add all events from the list model
        for (int i = 0; i < listModel.getSize(); i++) {
            Event event = listModel.getElementAt(i);
            String[] row = {
                event.getEventCode(),
                event.getName(),  
                event.getDate(),
                event.getTime(),
                event.getVenue(),
                event.getType(),
                String.valueOf(event.getCapacity()),
                String.format("RM%.2f", event.getRegistrationFee()),
                event.getDetails().replace("\n", " "),
                event.getRole(),
                event.getGroupPrice() > 0 ? String.valueOf(event.getGroupPrice()) : "N/A",
                event.getGroupDiscount() > 0 ? String.format("%.1f%%", event.getGroupDiscount()) : "N/A",
                event.getEarlyBirdDiscount() > 0 ? String.format("%.1f%%", event.getEarlyBirdDiscount()) : "N/A",
                event.getEarlyBirdDate() != null ? event.getEarlyBirdDate() : "N/A"
            };
            allRows.add(row);
        }
        
        // Calculate column widths
        int[] colWidths = new int[CSV_HEADER.length];
        for (String[] row : allRows) {
            for (int i = 0; i < row.length && i < colWidths.length; i++) {
                colWidths[i] = Math.max(colWidths[i], row[i].length());
            }
        }
        
        // Write to file with formatted table
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write separator line
            pw.println(createSeparatorLine(colWidths));
            
            // Write header
            pw.println(formatTableRow(CSV_HEADER, colWidths));
            pw.println(createSeparatorLine(colWidths));
            
            // Write data rows
            for (int i = 1; i < allRows.size(); i++) {
                pw.println(formatTableRow(allRows.get(i), colWidths));
            }
            
            // Write bottom separator
            pw.println(createSeparatorLine(colWidths));
        }
        
        updateStatus("All events saved to " + CSV_FILE_PATH);
    } catch (IOException e) {
        showModernDialog("File Error", "Failed to save events: " + e.getMessage(), DANGER_RED);
    }
}

// Helper method to format a table row
private String formatTableRow(String[] row, int[] colWidths) {
    StringBuilder sb = new StringBuilder("| ");
    for (int i = 0; i < row.length && i < colWidths.length; i++) {
        sb.append(String.format("%-" + colWidths[i] + "s", row[i]));
        sb.append(" | ");
    }
    return sb.toString();
}

// Helper method to create separator line
private String createSeparatorLine(int[] colWidths) {
    StringBuilder sb = new StringBuilder("+");
    for (int width : colWidths) {
        sb.append("-".repeat(width + 2));
        sb.append("+");
    }
    return sb.toString();
}

private void clearForm() {
    nameField.setText("");
    dateField.setText("");
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
    earlyDateField.setText("");
    selectedEvent = null;
    eventList.clearSelection();
    updateStatus("Form cleared and ready for new event");
}


private void saveEventToCSV(Event event) {
    // Instead of appending individual events, we'll always save all events
    // to maintain proper table formatting
    saveAllEventsToCSV();
}

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Create_Event_Page_Organiser();
        });
    }
}