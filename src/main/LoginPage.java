package main;

import component.PanelCover;
import component.PanelLoginAndRegister;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class LoginPage extends javax.swing.JFrame {

    private MigLayout layout;
    private PanelCover cover;
    private PanelLoginAndRegister loginAndRegister;
    private boolean isLogin;
    private final double addSize = 30;
    private final double coverSize = 40;
    private final double loginSize = 60;
    private final DecimalFormat df=new DecimalFormat("##0.###"); // for acceleraton and deceleration
    
    public LoginPage() {
        initComponents();
        init();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    }

    private void init(){
        layout = new MigLayout("fill, insets 0, debug");  // 1. Create layout
        cover = new PanelCover();   // 2. Create gradient panel
        loginAndRegister = new PanelLoginAndRegister();
        TimingTarget target = new TimingTargetAdapter(){
            @Override
            public void timingEvent( float fraction){
                double fractionCover;
                double fractionLogin;
                double size = coverSize;
                if (fraction <= 0.5f){
                    size += fraction * addSize;
                }else {
                    size += addSize - fraction * addSize;
                    
                }
                
                if(isLogin){
                    fractionCover = 1f - fraction; //reverse animation , 0 to 1
                    fractionLogin = fraction;
                    if (fraction >= 0.5f) {
                        cover.registerRight(fractionCover * 100);
                    }else{
                        cover.loginRight((1f - fractionLogin) * 100);
                    }
                }else{
                    fractionCover = fraction; //reverse animation , 1 to 0
                    fractionLogin = 1f - fraction;
                    if (fraction <= 0.5f) {
                        cover.registerLeft(fraction * 100);
                    } else {
                        cover.loginLeft((1f - fraction) * 100);
                    }
                }
                if(fraction >= 0.5f){
                    loginAndRegister.showRegister(isLogin);
                }
                fractionCover=Double.valueOf(df.format(fractionCover)); // for acceleraton and deceleration
                fractionLogin=Double.valueOf(df.format(fractionLogin)); // for handle keep switching
                layout.setComponentConstraints(cover, "width "+ size+ "%, pos "+ fractionCover+ "al 0 n 100%");
                layout.setComponentConstraints(loginAndRegister, "width "+ loginSize + "%, pos "+ fractionLogin + "al 0 n 100%");
                bg.revalidate(); //refresh the layout
            }

            @Override
            public void end() {
                isLogin = !isLogin; // toggle to not isLogin after finish animation
            }   

        };
        
        Animator animator = new Animator(800, target); //1000ms = 1s
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        animator.setResolution(0); // for smooth animation
        
        
        bg.setLayout(layout);       // 3. Apply layout to container
        bg.add(cover, "width "+ coverSize + "%, pos 0al 0 n 100%"); // 4. Add panel , n 100 means full height
        bg.add(loginAndRegister, "width "+ loginSize+"%, pos 1al 0 n 100%");  //1al as 100%
        cover.addEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(!animator.isRunning()){
                    animator.start();
                }
            }
        });
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setOpaque(true);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 933, Short.MAX_VALUE)
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 536, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane bg;
    // End of variables declaration//GEN-END:variables
}
