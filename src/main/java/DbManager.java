import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbManager {
    private final String url;
    private Connection connection;

    public DbManager(String pathToDb) {
        url = pathToDb;
    }

    public Connection getDbConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(url);
        }
        return connection;
    }

    public void createTables() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists users " +
                "(id integer not null primary key autoincrement," +
                "uuid text not null)");
        stmt.execute("create table if not exists links " +
                "(id integer not null primary key autoincrement," +
                "user_id integer not null," +
                "originalLink text not null," +
                "shortLink text not null," +
                "clickLimit integer," +
                "expirationDate timestamp," +
                "foreign key (user_id) references users(id))");
    }
}

