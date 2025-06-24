package component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Component;
import java.awt.Window;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import swing.Button;
import swing.MyTextField;
import javax.swing.BorderFactory;
import java.awt.geom.RoundRectangle2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JOptionPane;
import util.ExcelUtil;
import java.io.File;
import java.util.List;
import javax.swing.SwingUtilities;
import main.MainPageOrganizer;
import main.MainPageParticipant;

public class PanelLoginAndRegister extends javax.swing.JLayeredPane {
    

    public PanelLoginAndRegister() {
        initComponents();
        initRegister();
        initLogin();
        login.setVisible(false);
        register.setVisible(true);
    }
    
    private void initRegister(){
        register.setLayout(new MigLayout ("wrap", "push[center]push", "60[]30[]25[]push"));
        JLabel label1 = new JLabel ("Our Current Services");
        label1.setFont(new Font ("sanserif", 1, 30));
        label1.setForeground(new Color(75, 22, 76));
        register.add(label1);
        
        // First content card
        RoundedPanel card1 = new RoundedPanel(40); 
        card1.setLayout(new MigLayout("wrap", "15[]15", "15[]10[]15"));
        card1.setBackground(new Color(248, 231, 246));
        card1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add some padding
        
        JLabel cardTitle1 = new JLabel("Event Types");
        cardTitle1.setFont(new Font("sansserif", 1, 18));
        cardTitle1.setForeground(new Color(75, 22, 76));
        card1.add(cardTitle1);
        
        JLabel cardContent1_1 = new JLabel(" # Seminars");
        cardContent1_1.setFont(new Font("sansserif", 0, 14));
        card1.add(cardContent1_1);
        
        JLabel cardContent1_2 = new JLabel(" # Workshops");
        cardContent1_2.setFont(new Font("sansserif", 0, 14));
        card1.add(cardContent1_2);
        
        JLabel cardContent1_3 = new JLabel(" # Cultural Events");
        cardContent1_3.setFont(new Font("sansserif", 0, 14));
        card1.add(cardContent1_3);
        
        JLabel cardContent1_4 = new JLabel(" # Sports Events");
        cardContent1_4.setFont(new Font("sansserif", 0, 14));
        card1.add(cardContent1_4);
        
        register.add(card1, "w 80%");
        
        // Second content card
        RoundedPanel card2 = new RoundedPanel(40);
        card2.setLayout(new MigLayout("wrap", "15[]15", "15[]10[]15"));
        card2.setBackground(new Color(248, 231, 246));
        card2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add some padding
        
        JLabel cardTitle2 = new JLabel("Discounts");
        cardTitle2.setFont(new Font("sansserif", 1, 18));
        cardTitle2.setForeground(new Color(75, 22, 76));
        card2.add(cardTitle2);
        
        JLabel cardContent2_1 = new JLabel(" # Group Discounts up to 20% ");
        cardContent2_1.setFont(new Font("sansserif", 0, 14));
        card2.add(cardContent2_1);

        JLabel cardContent2_2 = new JLabel(" # Early Bird Discounts up to 15% ");
        cardContent2_2.setFont(new Font("sansserif", 0, 14));
        card2.add(cardContent2_2);

        register.add(card2, "w 80%");
    }
    
    private void initLogin(){
        login.setLayout(new MigLayout ("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel ("Sign In");
        label.setFont(new Font ("sansserif", 1, 30));
        label.setForeground(new Color(11,36,71));
        login.add(label);
        
        MyTextField txtUser = new MyTextField();
        txtUser.setPrefixIcon(new ImageIcon(getClass().getResource("/icon/user.png")));
        txtUser.setHint("Username");
        login.add(txtUser, "w 60%");
        
        MyTextField txtPassword = new MyTextField();
        txtPassword.setPrefixIcon(new ImageIcon(getClass().getResource("/icon/pass.png")));
        txtPassword.setHint("Password");
        login.add(txtPassword, "w 60%");
        
        JButton cmdForget = new JButton ("Forgot your password ?");
        cmdForget.setForeground(new Color(100, 100, 100));
        cmdForget.setFont(new Font ("sansserif", 1, 12));
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdForget.setBorderPainted(false);
        cmdForget.setContentAreaFilled(false);
        cmdForget.setFocusPainted(false);
        login.add(cmdForget);
        
        Button cmd = new Button();
        cmd.setBackground(new Color(25,55,109));
        cmd.setForeground(new Color(255, 255, 255));
        cmd.setText("SIGN IN");
        cmd.setFocusPainted(false);
        
        // Add action listener for the Sign In button
        cmd.addActionListener(e -> {
            String username = txtUser.getText().trim();
            String password = txtPassword.getText().trim();
            
            // Validate inputs
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter both username and password", 
                    "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Read users from Excel file
            try {
                // Define the path to users.xlsx file
                String usersFilePath = "database" + File.separator + "users.xlsx";
                
                // Read all data from the Excel file
                List<List<String>> userData = ExcelUtil.readExcelData(usersFilePath, 0);
                
                boolean found = false;
                String userRole = "";
                
                // Check if file has data
                if (userData != null && !userData.isEmpty()) {
                    // Find matching username and password
                    for (List<String> row : userData) {
                        // Check if row has at least 3 columns (username, password, role)
                        if (row.size() >= 3) {
                            String excelUsername = row.get(0); // First column is username
                            String excelPassword = row.get(1); // Second column is password
                            String role = row.get(2);         // Third column is role
                            
                            if (username.equals(excelUsername) && password.equals(excelPassword)) {
                                found = true;
                                userRole = role;
                                break;
                            }
                        }
                    }
                }
                
                // Process login result
                if (found) {
                    JOptionPane.showMessageDialog(null, "Login successful! ", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Find and dispose the parent window
                    Component component = PanelLoginAndRegister.this;
                    while (component != null && !(component instanceof Window)) {
                        component = component.getParent();
                    }
                    
                    if (component instanceof Window) {
                        ((Window) component).dispose();
                    }
                    
                    // Forward to appropriate page based on user role
                    if (userRole.equalsIgnoreCase("EO")) {
                        // Open organizer page
                        SwingUtilities.invokeLater(() -> {
                            new MainPageOrganizer();
                        });
                    } else if (userRole.equalsIgnoreCase("STUDENT")) {
                        // Open participant page for students
                        SwingUtilities.invokeLater(() -> {
                            MainPageParticipant participantPage = new MainPageParticipant();
                            participantPage.setUserType("STUDENT"); // To be implemented in MainPageParticipant
                        });
                    } else if (userRole.equalsIgnoreCase("STAFF")) {
                        // Open participant page for staff
                        SwingUtilities.invokeLater(() -> {
                            MainPageParticipant participantPage = new MainPageParticipant();
                            participantPage.setUserType("STAFF"); // To be implemented in MainPageParticipant
                        });
                    } else {
                        // Fallback for unknown roles
                        JOptionPane.showMessageDialog(null, 
                            "Unknown user role: " + userRole, 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", 
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error accessing user database: " + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        login.add(cmd, "w 40%, h 40");
    }

    public void showRegister(boolean show){
        if (show){
            register.setVisible(true);
            login.setVisible(false);         
        }else{
            register.setVisible(false);
            login.setVisible(true);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 325, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 309, Short.MAX_VALUE)
        );

        add(login, "card3");

        register.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 325, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 309, Short.MAX_VALUE)
        );

        add(register, "card2");
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
    // End of variables declaration//GEN-END:variables
    
    class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
        }
    }
}
