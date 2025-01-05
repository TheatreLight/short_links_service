
public class MainLinksService {
    public static void main(String[] args) throws Exception {
        DbManager dbManager = new DbManager("jdbc:sqlite:base.db");
        if (dbManager.getDbConnection() == null) {
            throw new RuntimeException("Can't create or connect to database");
        }
        dbManager.createTables();

        UserLinkModel model = new UserLinkModel(dbManager);
        Controller controller = new Controller(model);
        ConsoleView view = new ConsoleView(controller);

        view.run();
    }
}
