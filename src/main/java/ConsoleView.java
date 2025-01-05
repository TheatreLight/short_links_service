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

    private boolean answer() {
        var ans = reader();
        return ans.equals("Y") || ans.equals("y");
    }

    private void userHandler() throws Exception {
        if (authorized) return;

        message("Do you have the UUID (Y/N)?");
        var ans = reader();
        if (ans.equals("Y") || ans.equals("y")) {
            message("Enter your UUID.");
            while (!controller.authorize(reader())) {
                message("Wrong UUID. Try another.");
            }
        } else if(ans.equals("N") || ans.equals("n")) {
            message(controller.getNewUUID());
        }
        authorized = true;
    }

    private void linksHandler() {
        message("You have the next short links:");
        var list = controller.getListOfLinks();
        for (var link : list) {
            message(link);
        }
        if (list.isEmpty()) {
            message("No created links. You can create the new one.");
        }
        message(newLinkCreating());
        message("Enter the link you'd like to follow or space for skip:");
        followLink(reader());
    }

    private void followLink(String link) {
        if (link.isEmpty()) return;
        controller.followLink(link);
    }

    private String newLinkCreating() {
        message("Create link? (Y/N)");
        var ans = reader();
        while(true) {
            if (ans.equals("Y") || ans.equals("y")) {
                message("Enter the link you'd like shorten: ");
                return controller.createLink(reader());
            } else if (ans.equals("N") || ans.equals("n")) {
                return "";
            } else {
                ans = reader();
            }
        }
    }

    private void quitHandler() {
        message("'Q' for quit or any key for continue:");
        var ans = reader();
        if (ans.equals("Q") || ans.equals("q")) {
            isExited = true;
        }
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
