package main;
import javax.swing.*;

public class App extends JFrame {
    public App() {
        super();
        // add(new PaymentPage(this));
        add(new MyEventsPage(this));
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
