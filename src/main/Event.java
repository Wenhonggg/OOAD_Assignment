package main;

import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event {
    private String eventID;
    private EventType eventType;
    private String eventName;
    private LocalDate eventDate;
    private String eventTime;
    private String eventVenue;
    private int eventCapacity;
    private double eventFee;
    private String eventDetails;
    private UserRole eventRole;
    private int eventGrpDiscReq;
    private double eventGrpDiscPercentage;
    private LocalDate eventEarlyBirdDiscDeadline;
    private double eventEarlyBirdDiscPercentage;
    private double eventTransportationFee;
    private double eventCateringFee;
    private String imagePath;

    public Event(String eventID, String eventType, String eventName, String eventDate, String eventTime,
            String eventVenue, int eventCapacity, double eventFee, String eventDetails, String eventRole,
            int eventGrpDiscReq, double eventGrpDiscPercentage, String eventEarlyBirdDiscDeadline,
            double eventEarlyBirdDiscPercentage, double eventTransportationFee, double eventCateringFee,
            String imagePath) {
        this.eventID = eventID;
        switch (eventType.toUpperCase()) {
            case "SEMINAR":
                this.eventType = EventType.SEMINAR;
                break;
            case "WORKSHOP":
                this.eventType = EventType.WORKSHOP;
                break;
            case "SPORTS EVENT":
                this.eventType = EventType.SPORTS_EVENT;
                break;
            case "CULTURAL EVENT":
                this.eventType = EventType.CULTURAL_EVENT;
                break;
            default:
                break;
        }
        this.eventName = eventName;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.eventDate = LocalDate.parse(eventDate, formatter);
        this.eventTime = eventTime;
        this.eventCapacity = eventCapacity;
        this.eventFee = eventFee;
        this.eventDetails = eventDetails;
        switch (eventRole.toUpperCase()) {
            case "STUDENT":
                this.eventRole = UserRole.STUDENT;
                break;
            case "STAFF":
                this.eventRole = UserRole.STAFF;
                break;
            default:
                System.err.println("Unknown event role");
                break;
        }
        this.eventGrpDiscReq = eventGrpDiscReq;
        this.eventGrpDiscPercentage = eventGrpDiscPercentage;
        this.eventEarlyBirdDiscDeadline = LocalDate.parse(eventEarlyBirdDiscDeadline, formatter);
        this.eventEarlyBirdDiscPercentage = eventEarlyBirdDiscPercentage;
        this.eventTransportationFee = eventTransportationFee;
        this.eventCateringFee = eventCateringFee;
        this.imagePath = imagePath;
    }

    public String getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public int getEventCapacity() {
        return eventCapacity;
    }

    public double getEventFee() {
        return eventFee;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public UserRole getEventRole() {
        return eventRole;
    }

    public int getEventGrpDiscReq() {
        return eventGrpDiscReq;
    }

    public double getEventGrpDiscPercentage() {
        return eventGrpDiscPercentage;
    }

    public LocalDate getEventEarlyBirdDiscDeadline() {
        return eventEarlyBirdDiscDeadline;
    }

    public double getEventEarlyBirdDiscPercentage() {
        return eventEarlyBirdDiscPercentage;
    }

    public double getEventTransportationFee() {
        return eventTransportationFee;
    }

    public double getEventCateringFee() {
        return eventCateringFee;
    }

    public String getImagePath() {
        return imagePath;
    }

    public static List<Event> readEventsFromCSV(String csvPath) {
        List<Event> events = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                } // skip header
                String[] parts = line.split(",", -1);
                if (parts.length < 16)
                    continue;
                String eventID = parts[0].trim();
                String eventName = parts[1].trim();
                String eventDate = parts[2].trim();
                String eventTime = parts[3].trim();
                String eventVenue = parts[4].trim();
                String eventType = parts[5].trim();
                int capacity = Integer.parseInt(parts[6].trim());
                double fee = Double.parseDouble(parts[7].trim());
                String details = parts[8].trim();
                String role = parts[9].trim();
                int grpDiscReq = Integer.parseInt(parts[10].trim());
                double grpDiscPercent = Double.parseDouble(parts[11].trim());
                double earlyBirdPercent = Double.parseDouble(parts[12].trim());
                String earlyBirdDate = parts[13].trim();
                double transportation = Double.parseDouble(parts[14].trim());
                double catering = Double.parseDouble(parts[15].trim());
                // You can set a default image or logic for imagePath
                String imagePath = "icon/default_event.png";
                events.add(new Event(eventID, eventType, eventName, eventDate, eventTime, eventVenue, capacity, fee,
                        details, role, grpDiscReq, grpDiscPercent, earlyBirdDate, earlyBirdPercent, transportation,
                        catering, imagePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return events;
    }
}
