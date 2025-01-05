import java.util.ArrayList;
import java.util.List;

public class Controller {
    private UserLinkModel model;

    public Controller(UserLinkModel ulModel) {
        model = ulModel;
    }

    public boolean authorize(String uuid) {
        return true;
    }

    public String getNewUUID() throws Exception {
        String uuid = "";
        
        if (!authorize(uuid)) {
            throw new Exception("Something goes wrong. " +
                    "Authorization with newly created uuid if failed.");
        }
        return uuid;
    }

    public ArrayList<String> getListOfLinks() {
        ArrayList<String> list = new ArrayList<String>();
        return list;
    }

    public String createLink(String link) {
        return "";
    }

    public void followLink(String link) {

    }
}
