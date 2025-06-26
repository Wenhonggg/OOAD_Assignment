package main;
import javax.swing.*;

public class App extends JFrame {
    public App() {
        super();
        // add(new PaymentPage(this));
        Participant p = new Participant("Ali", "0123456789", "alsd");
        add(new MyEventsPage(this, p));
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // setSize(new Dimension(600,800));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new App();
    }
}
