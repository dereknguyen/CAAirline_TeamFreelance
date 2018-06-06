package src;

public class Session {
    private static Session uniqueInstance;
    private String username;

    private Session() {
        this.username = null;
    }

    static public Session getInstance() {
        if (uniqueInstance == null)
        {
            uniqueInstance = new Session();
        }
        return uniqueInstance;
    }

    public String getUsername() {
        if (username == null) {
            return "";
        }
        return username;
    }

    public void setUsername(String newName) {
        username = newName;
    }
}
