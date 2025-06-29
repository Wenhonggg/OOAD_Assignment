package main;

public enum EventType {
    SEMINAR, WORKSHOP, SPORTS_EVENT, CULTURAL_EVENT;

    @Override
    public String toString() {
        switch (this) {
            case SEMINAR:
                return "Seminar";
            case WORKSHOP:
                return "Workshop";
            case CULTURAL_EVENT:
                return "Cultural Event";
            case SPORTS_EVENT:
                return "Sports Event";
            default:
                return super.toString();
        }
    }
}
