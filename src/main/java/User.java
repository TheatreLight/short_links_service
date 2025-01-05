public class User {
    private String uuid;
    private int id;

    private void generateUUID() {

    }

    public User() {
        generateUUID();
    }

    public int getId() {
        return id;
    }

    public String getUUID() {
        return uuid;
    }
}
