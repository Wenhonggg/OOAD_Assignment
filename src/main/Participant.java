package main;

import java.util.ArrayList;
import java.util.List;
import util.SwingUtils;

public class Participant extends User {
    private String name;
    private String email;
    private String ticketFilePath;
    
    public Participant(String i, UserRole r) {
        super(i, r);
        ticketFilePath = "database/tickets_" + username + ".csv";
    }

    public Participant(String n, String i, String e) {
        name = n;
        email = e;
        username = i;
        ticketFilePath = "database/tickets_" + username + ".csv";
    }

    public String getID() {
        return username;
    }

    public Ticket[] getTickets() {
        List<List<String>> data = new ArrayList<>();
        try {
            data = SwingUtils.readFromCsv(ticketFilePath);
        } catch (Exception e) {
            System.err.println("Failed to fetch ticket data: " + e.getMessage());
            e.printStackTrace();
        }
        int TICKET_COUNT = data.size();
        Ticket[] tickets = new Ticket[TICKET_COUNT];
        for (int i = 0; i < tickets.length; i++) {
            List<String> row = data.get(i);
            tickets[i] = new Ticket(Integer.parseInt(row.get(0)), Integer.parseInt(row.get(1)), row.get(2), row.get(3),
                    row.get(4), row.get(5), row.get(6), row.get(7), row.get(8), row.get(9),
                    Integer.parseInt(row.get(10)));
        }
        return tickets;
    }
}
