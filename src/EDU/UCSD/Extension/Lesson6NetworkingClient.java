package EDU.UCSD.Extension;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Lesson6NetworkingClient {

    private String url = null;
    private int port;

    public Lesson6NetworkingClient(String url, int port) {
        this.url = url;
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static void syntaxSummary() {
        var commandName = "Lesson6NetworkingClient";
        System.out.printf("%n%-7s%-24s%-37s%s%n%-7s%-24s%-37s%s%n",
                "Usage:",
                commandName,
                "[--help]",
                "# Displays this command syntax summary",
                "",
                commandName,
                "{--server <address>} {--port <port>}",
                "# Connect to the server at <address>, listening on port <port>");
    }

    public void runSocketTest() throws IOException {
        try (var socket = new Socket(url, port);
             var in = new Scanner(socket.getInputStream(), StandardCharsets.UTF_8)) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                System.out.println(line);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%nObject of: %s%nServer: %s%nPort: %d%n",
                this.getClass().getSimpleName(),
                this.getUrl(),
                this.getPort());
    }

    public static void main(String[] options) throws IOException {
        String option = null;
        String url = null;
        int port = 0;

        try {
            option = options[0];
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            System.err.println("\nOops, a required option is missing!");
            Lesson6NetworkingClient.syntaxSummary();
            System.exit(1);
        }
        // Process the commandline options…
        for (int index = 0; index < options.length; index++) {
            switch (options[index]) {
                case "--help":
                    Lesson6NetworkingClient.syntaxSummary();
                    System.exit(0);
                    // break;
                case "--server":
                    try {
                        url = options[++index];
                    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                        // I know! Modifying the control variable from within the loop is a no-no!
                        System.err.printf("%nThe \"%s\" option requires a a URL or IP address argument.%n", options[--index]);
                        Lesson6NetworkingClient.syntaxSummary();
                        System.exit(1);
                    }
                    break;
                case "--port":
                    try {
                        port = Integer.parseInt(options[++index]);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
                        // I know! Modifying the control variable from within the loop is a no-no!
                        System.err.printf("%nThe \"%s\" option requires a numeric argument.%n", options[--index]);
                        Lesson6NetworkingClient.syntaxSummary();
                        System.exit(1);
                    }
                    break;
                default:
                    System.err.printf("%n\"%s\" is not a valid option!%n", options[index]);
                    Lesson6NetworkingClient.syntaxSummary();
                    System.exit(1);
            }
        }

        if (port == 0) {
            System.err.println("\nThe \"--port\" option is required!");
            Lesson6NetworkingClient.syntaxSummary();
        } else if (url == null) {
            System.err.println("\nThe \"--server\" option is required!");
            Lesson6NetworkingClient.syntaxSummary();
        } else {
            Lesson6NetworkingClient lesson6NetworkingClient = new Lesson6NetworkingClient(url, port);
            System.out.println(lesson6NetworkingClient);
            lesson6NetworkingClient.runSocketTest();
        }
    }

}