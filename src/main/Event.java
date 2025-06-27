package main;

import java.util.*;
import java.io.*;

public class Event {
    private String eventID;
    private String eventName;
    private String imagePath;

    public Event(String eventID, String eventName, String imagePath) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.imagePath = imagePath;
    }

    public String getEventID() { return eventID; }
    public String getEventName() { return eventName; }
    public String getImagePath() { return imagePath; }

    public static List<Event> readEventsFromCSV(String csvPath) {
        List<Event> events = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // skip header
                String[] parts = line.split(",", -1);
                if (parts.length < 2) continue;
                String eventID = parts[0].trim();
                String eventName = parts[1].trim();
                // You can set a default image or logic for imagePath
                String imagePath = "icon/default_event.png";
                events.add(new Event(eventID, eventName, imagePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return events;
    }
}
