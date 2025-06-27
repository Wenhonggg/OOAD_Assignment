package main;

public class User {
    protected String username;
    protected UserRole role;

    public User() {
        username = null;
        role = null;
    }

    public User(String un, UserRole r) {
        username = un;
        role = r;
    }
}