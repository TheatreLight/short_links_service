import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
    private UserLinkModel model;

    public Controller(UserLinkModel ulModel) {
        model = ulModel;
    }

    public boolean authorize(String uuid) throws SQLException {
        return model.authorizeUser(uuid);
    }

    public String getNewUUID() throws Exception {
        String uuid = model.registerNewUUID();
        if (!authorize(uuid)) {
            throw new Exception("Something goes wrong. " +
                    "Authorization with newly created uuid if failed.");
        }
        return uuid;
    }

    public ArrayList<String> getListOfLinks() throws SQLException {
        return model.getLinksList();
    }

    public String createLink(String link, int clicks) throws NoSuchAlgorithmException, SQLException {
        return model.createLink(link, clicks);
    }

    public boolean followLink(String link) throws URISyntaxException, IOException, SQLException, NoSuchAlgorithmException {
        return model.followLink(link);
    }

    public void saveCurrentState() throws SQLException, NoSuchAlgorithmException {
        model.saveStateToDb();
    }
}
