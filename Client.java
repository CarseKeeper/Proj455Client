import java.io.*;
import java.net.Socket;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ser.Serializers;

import scala.collection.immutable.HashMap;

public class Client {

    public static void main(String[] args) {
        try {
            WriteJsonObject json = new WriteJsonObject();
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

                    // ---------------------------------------------------------Attempt of Create
                    // request
                    // Request req = new Request(RequestType.CREATE,
                    // json.serialize(
                    // new CreateEventRequest("tilt", "extra tilt", 8458,
                    // "2024-05-04T09:10:10.000Z")));

                    // ---------------------------------------------------------Attempt of Events
                    // request and response
                    // Request req = new Request(RequestType.EVENTS, json.serialize(new
                    // EventsRequest()));
                    // out.writeBytes(json.serialize(req) + "\n");
                    // String st = in.readLine();
                    // System.out.println(st);
                    // EVENTS = json.deserialize(json.deserialize(st, Response.class).responseBody,
                    // EVENTS.getClass());
                    // System.out.println(EVENTS);

                    // ---------------------------------------------------------Attempt of Update
                    // request and response
                    // Request req = new Request(RequestType.UPDATE,
                    // json.serialize(new Event(1, "TILT", "MORE TILT", 4, 0,
                    // "2023-10-18T03:38:23.331Z")));
                    // out.writeBytes(json.serialize(req) + "\n");
                    // String st = in.readLine();
                    // System.out.println(st);
                    // Event ev = json.deserialize(json.deserialize(st,
                    // Response.class).responseBody, Event.class);
                    // System.out.println(ev.getTitle());

                    // ---------------------------------------------------------Attempt of parsing
                    // response as a request object...FAILED
                    // System.out.println((json.deserialize(st, Event.class)).getTitle());
                    // Request request = json.deserialize(in.readLine(), Request.class);
                    // CreateEventRequest cr = json.deserialize(request.requestBody,
                    // CreateEventRequest.class);
                    // System.out.println(cr.title);

                    break;

                } catch (Exception e) {
                    System.err.println(e);
                }
            }

            while (true) {
                EVENTS = getEvents(in, out);
                System.out.printf("%-4s    %30s%n%-4s    %30s%n%-4s    %30s%n%-4s    %30s%n%-4s    %30s%n%-4s    %30s",
                        "(1):", "List the current events", "(2):", "Create a new event", "(3):", "Donate to an event",
                        "(4):", "Update an event", "(5)", "List ALL events", "(q):", "Quit");
                String answer = scan.nextLine();

                // LIST CURRENT EVENTS
                if (answer.startsWith("1")) {
                    EVENTS = getEvents(in, out);
                    listEvents(EVENTS);
                }
                // CREATE A NEW EVENT
                else if (answer.startsWith("2")) {
                    CreateEventRequest newEvent = formEvent(scan);
                    createEvent(EVENTS, newEvent, out, in, json);
                }
                // DONATE AN AMOUNT TO AN EVENT
                else if (answer.startsWith("3")) {

                }
                // UPDATES AN EVENT AND CHANGES IT IN THE CURRENT LIST
                else if (answer.startsWith("4")) {
                    listEvents(EVENTS);
                    System.out.print("Pick an event: ");
                    int index = scan.nextInt();
                    if (index < EVENTS.size()) {
                        Event event = EVENTS.get(index);

                    } else {
                        System.out.println("That index is not available.");
                    }
                }
                // PRINTS OUT CURRENT EVENTS AND PAST EVENTS
                else if (answer.startsWith("5")) {
                    ArrayList<Event> curEvents = currentEvents(EVENTS);
                    ArrayList<Event> pastEvents = pastEvents(EVENTS);
                    System.out.printf("  %-25s  |  %-25s  ", "Current Events", "Past Events");
                    System.out.println("-----------------------------------------------------------");
                    for (int i = 0; i < curEvents.size() || i < pastEvents.size(); i++) {
                        String ce = "";
                        String pe = "";
                        if (i < curEvents.size())
                            ce = curEvents.get(i).getTitle();
                        if (i < pastEvents.size())
                            pe = pastEvents.get(i).getTitle();

                        System.out.printf("  %-25s  |  %-25s  ", ce, pe);
                    }
                    System.out.println();
                }
                // QUITS THE PROGRAM
                else if (answer.toLowerCase().startsWith("q")) {
                    break;
                }
                // INVALID INPUT DETECTED, TRY AGAIN
                else {
                    System.out.println("Invalid input.");
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
     * Gets the past events from all events
     */
    private static ArrayList<Event> pastEvents(ArrayList<Event> EVENTS) {
        ArrayList<Event> pastEvents = new ArrayList<Event>();
        for (Event event : EVENTS) {
            if (event.hasEnded())
                pastEvents.add(event);
        }

        return pastEvents;
    }

    /*
     * Sends information to create a new event
     */
    private static void createEvent(ArrayList<Event> EVENTS, CreateEventRequest newEvent, DataOutputStream out,
            BufferedReader in,
            WriteJsonObject json) {
        try {
            String body = json.serialize(newEvent);
            out.writeBytes(json.serialize(new Request(RequestType.CREATE, body)));
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            Response response = getResponse(in, json);
            if (response.responseType == RequestType.CREATE) {
                EVENTS.add(json.deserialize(response.responseBody, Event.class));
            } else {
                System.err.println("error occured, response body: " + response.responseBody);
            }
        }
    }

    /*
     * Creates a new Event object to be sent
     */
    private static CreateEventRequest formEvent(Scanner scan) {
        try {
            System.out.print("Enter the title for Event: ");
            String title = scan.nextLine();
            System.out.print("Enter the description for the Event: ");
            String description = scan.nextLine();
            System.out.print("Enter the target amount as a double (####.##): ");
            double target = scan.nextDouble();
            System.out.print(
                    "Enter the deadline in the form YYYY-MM-DDTHH:MM:SSSZ\ne.g. Oct 25, 2023 at 1:45:30PM would be 2023-10-25T13:45:300Z: ");
            String deadline = scan.nextLine();

            return (new CreateEventRequest(title, description, target, deadline));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            System.err.println(e);
        }
        return 6789;
    }

    /*
     * gets all events from server and returns them as an ArrayList
     */
    private static ArrayList<Event> getEvents(BufferedReader in, DataOutputStream out) {
        ArrayList<Event> events = new ArrayList<Event>();
        WriteJsonObject json = new WriteJsonObject();
        try {
            out.writeBytes(json.serialize(new EventsRequest()));

        } catch (Exception e) {
            System.err.println(e);
        } finally {
            try {
                Response response = json.deserialize(in.readLine(), Response.class);
                if (response.responseType == RequestType.EVENTS)
                    events = json.deserialize(response.responseBody, events.getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return events;
    }

    /*
     * lists all the current events and prompts user to choose one
     */
    private static void chooseEvent(ArrayList<Event> EVENTS) {
        try {
            listEvents(currentEvents(EVENTS));
            System.out.println("Enter the Event you wish to check: ");

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /*
     * Prints current variables and prompts for new variable
     */
    private static UpdateEventRequest changeEvent(Event event, Scanner scan, WriteJsonObject json) {
        System.out.printf("Current Title: %25s", event.getTitle());
        System.out.print("New Title: ");
        String title = scan.nextLine();
        if (!title.equals(""))
            event.setTitle(title);

        System.out.printf("Current Description: %s", event.getDescription());
        System.out.print("New Description: ");
        String description = scan.nextLine();
        if (!description.equals(""))
            event.setDescription(description);

        System.out.printf("Current Target: %15f", event.getTarget());
        System.out.print("New Target: ");
        double target = scan.nextDouble();
        if (target != 0)
            event.setGoal(target);
        return new UpdateEventRequest(json.serialize(json));
    }

    /*
     * helper method of chooseEvent that lists all events
     */
    private static void listEvents(ArrayList<Event> events) {
        int i = 1;
        for (Event event : events) {
            System.out.printf("%8d.    %-35s%n        %s", i++, event.getTitle(), event.getDescription());
        }
    }

    /*
     * replaces the event with an updated version of the event
     */
    public static void replaceEvent(ArrayList<Event> events, Event event) {
        int index = findIndex(events, event);
        events.set(index, event);
    }

    /*
     * finds the index the old version of the event is stored
     */
    public static int findIndex(ArrayList<Event> events, Event event) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId() == event.getId())
                return i;
        }
        return 0;
    }

    /*
     * 
     */
    private static Response getResponse(BufferedReader in, WriteJsonObject json) {
        try {
            return json.deserialize(in.readLine(), Response.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
