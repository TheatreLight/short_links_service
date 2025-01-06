import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;

public class ConsoleView {
    private Controller controller;
    private boolean isExited = false;
    private boolean authorized = false;
    private Scanner scanner;

    private void message(String line) {
        System.out.println(line);
    }

    private String reader() {
        String line = "";
        while (line.isEmpty()) {
            line = scanner.nextLine();
        }
        return line;
    }

    private void userHandler() throws Exception {
        if (authorized) return;

        message("Do you have the UUID (Y/N)?");
        var ans = reader();
        while (!authorized) {
            if (ans.equals("Y") || ans.equals("y")) {
                message("Enter your UUID.");
                while (!controller.authorize(reader())) {
                    message("Wrong UUID. Try another.");
                }
                authorized = true;
            } else if (ans.equals("N") || ans.equals("n")) {
                message(controller.getNewUUID());
                authorized = true;
            } else {
                ans = reader();
            }
        }
    }

    private void linksHandler() throws SQLException, URISyntaxException, IOException, NoSuchAlgorithmException {
        message("You have the next short links:");
        var list = controller.getListOfLinks();
        for (var link : list) {
            message(link);
        }
        if (list.isEmpty()) {
            message("No created links. You can create the new one.");
        }
        message(newLinkCreating());
        message("Enter the link you'd like to follow or 'S' for skip:");
        followLink(reader());
    }

    private void followLink(String link) throws URISyntaxException, IOException, SQLException, NoSuchAlgorithmException {
        if (link.equals("S") || link.equals("s")) return;
        if (!controller.followLink(link)) {
            message("Not existed or expired link.");
        }
    }

    private String newLinkCreating() throws NoSuchAlgorithmException, SQLException {
        message("Create link? (Y/N)");
        var ans = reader();
        while(true) {
            if (ans.equals("Y") || ans.equals("y")) {
                message("Enter the link you'd like shorten (start with 'http(s)://...'): ");
                String link = reader();
                message("Enter amounts of clicks you 'd like (not less then default): ");
                int clcks = Integer.parseInt(reader());
                message("Enter the link's lifetime in minutes (but no more then default): ");
                int duration = Integer.parseInt(reader());
                return controller.createLink(link, clcks, duration);
            } else if (ans.equals("N") || ans.equals("n")) {
                return "";
            } else {
                ans = reader();
            }
        }
    }

    private void quitHandler() throws SQLException, NoSuchAlgorithmException {
        message("'Q' for quit or any key for continue:");
        var ans = reader();
        if (ans.equals("Q") || ans.equals("q")) {
            isExited = true;
        }
        controller.saveCurrentState();
    }

    public ConsoleView(Controller cntrl) {
        controller = cntrl;
        scanner = new Scanner(System.in);
    }

    public void run() throws Exception {
        while (!isExited) {
            userHandler();
            linksHandler();
            quitHandler();
        }
    }
}
