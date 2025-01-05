import java.util.UUID;

public class User {
    private String uuid;
    private int id;

    private void generateUUID() {
        UUID randUid = UUID.randomUUID();
        uuid = randUid.toString();
    }

    public User() {
        generateUUID();
        id = -1;
    }

    public User(int id, String uuid) {
        this.id = id;
        this.uuid = uuid;
    }

    public void setNewId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUUID() {
        return uuid;
    }
}
