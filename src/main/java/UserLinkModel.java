import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserLinkModel {
    private DbManager dbManager;
    User user = null;
    Link userLink = null;

    private int getIdByUUID(String uuid) throws SQLException {
        var connection = dbManager.getDbConnection();
        PreparedStatement pstmt = connection.prepareStatement("select id from users where uuid = ?");
        pstmt.setString(1, uuid);
        pstmt.execute();
        var result = pstmt.getResultSet();
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    private void saveLinkToDb() throws SQLException, NoSuchAlgorithmException {
        var connection = dbManager.getDbConnection();
        PreparedStatement pstmt = connection.prepareStatement(
                "insert into links(user_id, originalLink, " +
                        "shortLink, clickLimit, expirationDate) " +
                        "values(?, ?, ?, ?, ?)");
        pstmt.setInt(1, user.getId());
        pstmt.setString(2, userLink.getOriginalLink());
        pstmt.setString(3, userLink.getShortLink());
        pstmt.setInt(4, userLink.getClicks());
        pstmt.setTimestamp(5, userLink.getExpirationDate());

        if (pstmt.executeUpdate() == 0) {
            throw new SQLException("Can't save link to db");
        }
    }

    private void removeExpired() throws SQLException, NoSuchAlgorithmException {
        var connection = dbManager.getDbConnection();
        PreparedStatement pstmt = connection.prepareStatement(
                "delete from links where shortLink = ? and user_id = ?");
        pstmt.setString(1, userLink.getShortLink());
        pstmt.setInt(2, user.getId());
        pstmt.executeUpdate();
    }

    public void saveStateToDb() throws SQLException, NoSuchAlgorithmException {
        var connection = dbManager.getDbConnection();
        PreparedStatement pstmt = connection.prepareStatement(
                "update links set clickLimit = ? where shortLink = ? and user_id = ?");
        pstmt.setInt(1, userLink.getClicks());
        pstmt.setString(2, userLink.getShortLink());
        pstmt.setInt(3, user.getId());
        pstmt.executeUpdate();
    }

    public boolean retrieveLinkFromDb(String shortLink) throws SQLException, NoSuchAlgorithmException {
        if (userLink == null) {
            userLink = new Link("");
        } else {
            saveStateToDb();
        }

        var connection = dbManager.getDbConnection();
        PreparedStatement pstmt = connection.prepareStatement(
                "select originalLink, clickLimit, expirationDate " +
                        "from links where shortLink = ? and  user_id = ?");
        pstmt.setString(1, shortLink);
        pstmt.setInt(2, user.getId());
        pstmt.execute();
        var result = pstmt.getResultSet();

        userLink.setShortLink(shortLink);
        if (result.next()) {
            userLink.setOriginalLink(result.getString(1));
            userLink.setClickLLimit(result.getInt(2));
            userLink.setExpirationDate(result.getTimestamp(3));
            return true;
        }
        return false;
    }

    public boolean checkLinkExpired() {
        return userLink.getClicks() == 0 || userLink.isExpired();
    }

    public UserLinkModel(DbManager dbMgr) {
        dbManager = dbMgr;
    }

    public boolean authorizeUser(String uuid) throws SQLException {
        int id = getIdByUUID(uuid);
        if (id > 0) {
            user = new User(id, uuid);
        }
        return id > 0;
    }

    public String registerNewUUID() throws SQLException {
        user = new User();
        var connection = dbManager.getDbConnection();
        PreparedStatement pstmt = connection.prepareStatement("insert into users(uuid) values(?)");
        pstmt.setString(1, user.getUUID());
        pstmt.executeUpdate();

        user.setNewId(getIdByUUID(user.getUUID()));
        return user.getUUID();
    }

    public ArrayList<String> getLinksList() throws SQLException {
        var connection = dbManager.getDbConnection();
        PreparedStatement pstmt = connection.prepareStatement("select shortLink from links where user_id = ?");
        pstmt.setInt(1, user.getId());
        pstmt.execute();
        var results = pstmt.getResultSet();
        ArrayList<String> resList = new ArrayList<String>();
        while (results.next()) {
            resList.add(results.getString(1));
        }
        return resList;
    }

    public String createLink(String link, int clicks) throws NoSuchAlgorithmException, SQLException {
        userLink = new Link(link);
        userLink.setClickLLimit(clicks);
        String shortLink = userLink.getShortLink();
        saveLinkToDb();
        return shortLink;
    }

    public boolean followLink(String link) throws URISyntaxException, IOException, SQLException, NoSuchAlgorithmException {
        if (!retrieveLinkFromDb(link)) {
            return false;
        }
        if (checkLinkExpired()){
            removeExpired();
            return false;
        }
        Desktop.getDesktop().browse(new URI(userLink.getOriginalLink()));
        userLink.setClickLLimit(userLink.getClicks()-1);
        return true;
    }
}
