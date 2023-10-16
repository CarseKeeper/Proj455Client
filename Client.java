import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    Scanner scan = new Scanner(System.in); // Scanner for client input
    ArrayList<Event> EVENTS = new ArrayList<Event>(); // List of all Events from the database

    public void main(String[] args) {
        // While loop for getting a connection to the server
        while (true) {
            try {
                Socket server = connectToServer();

                InputStream inBytes = server.getInputStream();
                InputStreamReader in = new InputStreamReader(inBytes);

                OutputStream outBytes = server.getOutputStream();
                OutputStreamWriter outs = new OutputStreamWriter(outBytes);

                break;

            } catch (Exception e) {
                System.err.println(e);
            }

        }
    }

    /*
     * Gets the active events from all events
     */
    private ArrayList<Event> currentEvents(ArrayList<Event> EVENTS) {
        ArrayList<Event> curEvents = new ArrayList<Event>();
        for (Event event : EVENTS) {
            if (event.hasEnded())
                continue;
            else
                curEvents.add(event);
        }

        return curEvents;
    }

    /*
     * Sends information to create a new event
     */
    private void createEvent(Event newEvent) {

    }

    /*
     * tries to connect to the server
     */
    private Socket connectToServer() {
        try {
            Socket server = new Socket(getHost(), getPort(), null, 0);
            return server;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    /*
     * helper method of connectToServer to prompt the client for a server host
     */
    private String getHost() {
        System.out.print("Enter IP address: ");
        try {
            String host = scan.nextLine();
            return host;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    /*
     * helper method of connectToServer to prompt the client for a port number
     */
    private int getPort() {
        System.out.print("Enter port number: ");
        try {
            int port = scan.nextInt();
            return port;
        } catch (Exception e) {
            System.out.println(e);
        }
        return 10000;
    }

    private ArrayList<Event> getEvents(InputStreamReader in, OutputStreamWriter out) {
        ArrayList<Event> events = new ArrayList<Event>();
        try {
            out.write(1);
        } catch (Exception e) {
            System.err.println(e);
        }

        return events;
    }
}
