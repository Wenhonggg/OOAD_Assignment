package main;

import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event implements Subject {
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
    private String filePath;
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private List<Observer> observers = new ArrayList<>();
    private boolean isCancelled;

    public Event(String eventID, String eventType, String eventName, String eventDate, String eventTime,
            String eventVenue, int eventCapacity, double eventFee, String eventDetails, String eventRole,
            int eventGrpDiscReq, double eventGrpDiscPercentage, String eventEarlyBirdDiscDeadline,
            double eventEarlyBirdDiscPercentage, double eventTransportationFee, double eventCateringFee,
            String imagePath) {
        this.eventID = eventID;
        setEventType(eventType);
        this.eventName = eventName;
        setEventDate(eventDate);
        this.eventTime = eventTime;
        this.eventVenue = eventVenue;
        this.eventCapacity = eventCapacity;
        this.eventFee = eventFee;
        this.eventDetails = eventDetails;
        setEventRole(eventRole);
        this.eventGrpDiscReq = eventGrpDiscReq;
        this.eventGrpDiscPercentage = eventGrpDiscPercentage;
        setEventEarlyBirdDiscDeadline(eventEarlyBirdDiscDeadline);
        this.eventEarlyBirdDiscPercentage = eventEarlyBirdDiscPercentage;
        this.eventTransportationFee = eventTransportationFee;
        this.eventCateringFee = eventCateringFee;
        this.imagePath = imagePath;
        this.filePath = "database/" + eventName.replace(' ', '_') + ".csv";
        this.isCancelled = false;
    }

    public Event(String eventID, String eventType, String eventName, String eventDate, String eventTime,
            String eventVenue) {
        this.eventID = eventID;
        setEventType(eventType);
        this.eventName = eventName;
        setEventDate(eventDate);
        this.eventTime = eventTime;
        this.eventVenue = eventVenue;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setEventType(String eventType) {
        if (eventType == null) {
            this.eventType = null;
            return;
        }

        // Clean the event type string - remove emojis and extra spaces
        String cleanEventType = eventType.replaceAll("[^\\w\\s]", "").trim().toUpperCase();

        switch (cleanEventType) {
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
                // Try partial matching for cases where substring was used incorrectly
                if (cleanEventType.contains("SEMINAR")) {
                    this.eventType = EventType.SEMINAR;
                } else if (cleanEventType.contains("WORKSHOP")) {
                    this.eventType = EventType.WORKSHOP;
                } else if (cleanEventType.contains("CULTURAL")) {
                    this.eventType = EventType.CULTURAL_EVENT;
                } else if (cleanEventType.contains("SPORTS")) {
                    this.eventType = EventType.SPORTS_EVENT;
                } else {
                    System.err.println("Unknown event type: " + eventType + " (cleaned: " + cleanEventType + ")");
                    this.eventType = EventType.SEMINAR; // Default fallback
                }
                break;
        }
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = LocalDate.parse(eventDate, FORMATTER);
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public void setEventCapacity(int eventCapacity) {
        this.eventCapacity = eventCapacity;
    }

    public void setEventFee(double eventFee) {
        this.eventFee = eventFee;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public void setEventRole(String eventRole) {
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
    }

    public void setEventGrpDiscReq(int eventGrpDiscReq) {
        this.eventGrpDiscReq = eventGrpDiscReq;
    }

    public void setEventGrpDiscPercentage(double eventGrpDiscPercentage) {
        this.eventGrpDiscPercentage = eventGrpDiscPercentage;
    }

    public void setEventEarlyBirdDiscDeadline(String eventEarlyBirdDiscDeadline) {
        if (eventEarlyBirdDiscDeadline == null)
            this.eventEarlyBirdDiscDeadline = null;
        else
            this.eventEarlyBirdDiscDeadline = LocalDate.parse(eventEarlyBirdDiscDeadline, FORMATTER);
    }

    public void setEventEarlyBirdDiscPercentage(double eventEarlyBirdDiscPercentage) {
        this.eventEarlyBirdDiscPercentage = eventEarlyBirdDiscPercentage;
    }

    public void setEventTransportationFee(double eventTransportationFee) {
        this.eventTransportationFee = eventTransportationFee;
    }

    public void setEventCateringFee(double eventCateringFee) {
        this.eventCateringFee = eventCateringFee;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
        if (isCancelled)
            notifyObservers();
    }

    public List<Observer> getObservers() {
        return observers;
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

    public String getFilePath() {
        return filePath;
    }

    public boolean getIsCancelled() {
        return isCancelled;
    }

    public DateTimeFormatter getFormatter() {
        return FORMATTER;
    }

    @Override
    public String toString() {
        return eventID + " - " + eventName + " - " + eventDate.format(FORMATTER) + " at " + eventTime;
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
                double earlyBirdPercent = parts[12].trim().equals("N/A") ? 0.0 : Double.parseDouble(parts[12].trim());
                String earlyBirdDate = parts[13].trim().equals("N/A") ? null : parts[13].trim();
                double transportation = Double.parseDouble(parts[14].trim());
                double catering = Double.parseDouble(parts[15].trim());
                // You can set a default image or logic for imagePath
                String imagePath = "icon/default_event.png";
                Event event = new Event(eventID, eventType, eventName, eventDate, eventTime, eventVenue, capacity, fee,
                        details, role, grpDiscReq, grpDiscPercent, earlyBirdDate, earlyBirdPercent, transportation,
                        catering, imagePath);
                
                // Read the "Is Cancelled" column if it exists (index 16)
                if (parts.length > 16 && !parts[16].trim().isEmpty()) {
                    boolean isCancelled = parts[16].trim().equalsIgnoreCase("true");
                    event.setIsCancelled(isCancelled);
                }
                
                events.add(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers)
            o.update(isCancelled, eventID);
    }
}
