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

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Ticket[] getTickets() {
        List<List<String>> data = new ArrayList<>();
        try {
            data = SwingUtils.readFromCsv(ticketFilePath);
        } catch (Exception e) {
        }
        int TICKET_COUNT = data.size() - 1;
        if(TICKET_COUNT < 1) {
            return null;
        }
        Ticket[] tickets = new Ticket[TICKET_COUNT];
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            Event e = new Event(row.get(1), row.get(2), row.get(3), row.get(4), row.get(5), row.get(6));
            Participant p = new Participant(row.get(8), row.get(9), row.get(10));
            tickets[i - 1] = new Ticket(Integer.parseInt(row.get(0)), e, row.get(7), p,
                    Integer.parseInt(row.get(11)), Boolean.parseBoolean(row.get(12)));
        }
        return tickets;
    }
}
