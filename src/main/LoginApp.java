package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import util.ExcelUtil;

public class LoginApp {
    public static void main(String[] args) {
        // First check if database exists
        if (!ExcelUtil.isDatabaseAvailable()) {
            JOptionPane.showMessageDialog(null, 
                "Database file not found in 'database' folder!\nPlease ensure users.xlsx exists.",
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame frame = new JFrame("Login");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        // Create components
        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton loginBtn = new JButton("Login");

        // Layout configuration
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Add components to frame
        gbc.gridx = 0; gbc.gridy = 0; frame.add(userLabel, gbc);
        gbc.gridx = 1; frame.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; frame.add(passLabel, gbc);
        gbc.gridx = 1; frame.add(passField, gbc);

        gbc.gridx = 1; gbc.gridy = 2; frame.add(loginBtn, gbc);

        // Login button action
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, 
                        "Username and password cannot be empty!", 
                        "Validation Error", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Map<String, String> users = ExcelUtil.readUsers();

                if (users.containsKey(username) && users.get(username).equals(password)) {
                    JOptionPane.showMessageDialog(frame, "Login successful!");
                    // Here you can proceed to main application
                } else {
                    JOptionPane.showMessageDialog(frame, 
                        "Invalid username or password", 
                        "Login Failed", 
                        JOptionPane.ERROR_MESSAGE);
                    // Clear password field on failed attempt
                    passField.setText("");
                }
            }
        });

        // Center and show the frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}