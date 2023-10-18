import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ser.Serializers;

public class Client {

    public static void main(String[] args) {
        try {
            Scanner scan = new Scanner(System.in); // Scanner for client input
            ArrayList<Event> EVENTS = new ArrayList<Event>(); // List of all Events from the database
            Socket server;
            BufferedReader in;
            DataOutputStream out;

            // While loop for getting a connection to the server
            while (true) {
                try {
                    server = connectToServer(scan);
                    out = new DataOutputStream(server.getOutputStream());
                    in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    WriteJsonObject json = new WriteJsonObject();
                    Request req = new Request(RequestType.CREATE,
                            json.serialize(new CreateEventRequest("tilt", "very tilt", 200, new Date())));
                    out.writeBytes(json.serialize(req) + "\n");
                    Request request = json.deserialize(in.readLine(), Request.class);
                    CreateEventRequest cr = json.deserialize(request.requestBody, CreateEventRequest.class);
                    System.out.println(cr.toString());

                    break;

                } catch (Exception e) {
                    System.err.println(e);
                }
            }

            server.close();
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    // ----------------------------------------------------------------- //
    // ----------------------------------------------------------------- //

    /*
     * Gets the active events from all events
     */
    private static ArrayList<Event> currentEvents(ArrayList<Event> EVENTS) {
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
    private static void createEvent(Event newEvent, ObjectOutputStream out) {
        try {
            out.writeObject(newEvent);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /*
     * tries to connect to the server
     */
    private static Socket connectToServer(Scanner scan) {
        try {
            Socket server = new Socket(getHost(scan), getPort(scan), null, 0);
            return server;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    /*
     * helper method of connectToServer to prompt the client for a server host
     */
    private static String getHost(Scanner scan) {
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
    private static int getPort(Scanner scan) {
        System.out.print("Enter port number: ");
        try {
            int port = scan.nextInt();
            return port;
        } catch (Exception e) {
            System.out.println(e);
        }
        return 6789;
    }

    /*
     * gets all events from server and returns them as an ArrayList
     */
    private static ArrayList<Event> getEvents(BufferedReader in, OutputStreamWriter out) {
        ArrayList<Event> events = new ArrayList<Event>();
        WriteJsonObject json = new WriteJsonObject();
        try {
            out.write(json.serialize(new EventsRequest()));

            // events.add();

        } catch (Exception e) {
            System.err.println(e);
        }

        return events;
    }

    private static void chooseEvent(ArrayList<Event> events) {
        try {
            listEvents(currentEvents(events));
            System.out.println("Enter the Event you wish to check: ");

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /*
     * helper method of chooseEvent that lists all events
     */
    private static void listEvents(ArrayList<Event> events) {
        int i = 1;
        for (Event event : events) {
            System.out.println(i++ + ".\t" + event.getTitle());
        }
    }

}
