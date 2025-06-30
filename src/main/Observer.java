package main;

public interface Observer {
    void update(boolean isCancelled, String eventID);
}
