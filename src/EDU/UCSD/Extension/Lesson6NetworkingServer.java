package EDU.UCSD.Extension;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * This class handles the client input for one server socket connection.
 */
class ThreadedEchoHandler implements Runnable {
    private Socket incoming;

    /**
     * Constructs a handler.
     *
     * @param incomingSocket the incoming socket
     */
    public ThreadedEchoHandler(Socket incomingSocket) {
        incoming = incomingSocket;
    }

    public void run() {
        try (InputStream inStream = incoming.getInputStream();
             OutputStream outStream = incoming.getOutputStream();
             var in = new Scanner(inStream, StandardCharsets.UTF_8);
             var out = new PrintWriter(
                     new OutputStreamWriter(outStream, StandardCharsets.UTF_8),
                     true /* autoFlush */)) {

            out.printf("%n%s%n%s%n%n%n%s%n%s%n%s%n%s%n%s%n%s%n",
                    "HTTP/1.0 200 OK",
                    "Content-Type: text/html",
                    "<html>",
                    "<head><title>Java Networking</title></head>",
                    "<body>",
                    "<hl>Java Networking</hl>",
                    "</body>",
                    "</html>");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Lesson6NetworkingServer {

    private int port;

    public Lesson6NetworkingServer(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static void syntaxSummary() {
        var commandName = "Lesson6NetworkingClient";
        System.out.printf("%n%-7s%-24s%-18s%s%n%-7s%-24s%-18s%s%n",
                "Usage:",
                commandName,
                "[--help]",
                "# Displays this command syntax summary",
                "",
                commandName,
                "{--port <number>}",
                "# Listen for connections on port <number>");
    }

    public void startServer() {

        try (var serverSocket = new ServerSocket(port)) {
            int index = 1;

            while (true) {
                Socket incoming = serverSocket.accept();
                System.out.println("Spawning " + index);
                Runnable runnable = new ThreadedEchoHandler(incoming);
                var thread = new Thread(runnable);
                thread.start();
                index++;
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return String.format("%nObject of: %s%nPort: %s%n",
                this.getClass().getSimpleName(),
                this.getPort());
    }

    public static void main(String[] options) throws IOException {
        String option = null;
        int port = 0;

        try {
            option = options[0];
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            System.err.println("\nOops, a required option is missing!");
            EDU.UCSD.Extension.Lesson6NetworkingServer.syntaxSummary();
            System.exit(1);
        }
        // Process the commandline options…
        for (int index = 0; index < options.length; index++) {
            switch (options[index]) {
                case "--help":
                    EDU.UCSD.Extension.Lesson6NetworkingServer.syntaxSummary();
                    System.exit(0);
                    // break;
                case "--port":
                    try {
                        port = Integer.parseInt(options[++index]);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
                        // I know! Modifying the control variable from within the loop is a no-no!
                        System.err.printf("%nThe \"%s\" option requires a numeric argument.%n", options[--index]);
                        EDU.UCSD.Extension.Lesson6NetworkingServer.syntaxSummary();
                        System.exit(1);
                    }
                    break;
                default:
                    System.err.printf("%n\"%s\" is not a valid option!%n", options[index]);
                    EDU.UCSD.Extension.Lesson6NetworkingServer.syntaxSummary();
                    System.exit(1);
            }
        }

        if (port == 0) {
            System.err.println("\nThe \"--port\" option is required!");
            EDU.UCSD.Extension.Lesson6NetworkingClient.syntaxSummary();
        } else {
            EDU.UCSD.Extension.Lesson6NetworkingServer lesson6NetworkingServer = new EDU.UCSD.Extension.Lesson6NetworkingServer(port);
            System.out.println(lesson6NetworkingServer);
            lesson6NetworkingServer.startServer();
        }
    }

}

