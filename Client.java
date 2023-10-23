import java.io.*;
import java.net.Socket;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ser.Serializers;

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
                    server = connectToServer();
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
                        "(4):", "Update an event", "(5):", "List ALL events", "(q):", "Quit");
                String answer = scan.nextLine();

                // LIST CURRENT EVENTS
                if (answer.startsWith("1")) {
                    EVENTS = currentEvents(EVENTS);
                    listEvents(EVENTS);
                    System.out.println();
                }
                // CREATE A NEW EVENT
                else if (answer.startsWith("2")) {
                    CreateEventRequest newEvent = formEvent();
                    createEvent(EVENTS, newEvent, out, in, json);
                }
                // DONATE AN AMOUNT TO AN EVENT
                else if (answer.startsWith("3")) {
                    donateToEvent(EVENTS, in, out, json);
                }
                // UPDATES AN EVENT FROM ALL EVENTS
                else if (answer.startsWith("4")) {
                    listEvents(EVENTS);
                    updateEvent(EVENTS, in, out, json);
                }
                // PRINTS OUT CURRENT EVENTS AND PAST EVENTS
                else if (answer.startsWith("5")) {
                    ArrayList<Event> curEvents = currentEvents(EVENTS);
                    ArrayList<Event> pastEvents = pastEvents(EVENTS);
                    System.out.printf("  %-25s  |  %-25s  %n", "Current Events", "Past Events");
                    System.out.println("-----------------------------------------------------------");
                    for (int i = 0; i < curEvents.size() || i < pastEvents.size(); i++) {
                        String ce = "";
                        String pe = "";
                        if (i < curEvents.size())
                            ce = curEvents.get(i).getTitle();
                        if (i < pastEvents.size())
                            pe = pastEvents.get(i).getTitle();

                        System.out.printf("  %-25s  |  %-25s  %n", ce, pe);
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

    // -------------------------------------------------------------------------------------------------------------------------------------//
    // -------------------------------------------------------------------------------------------------------------------------------------//

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
    private static CreateEventRequest formEvent() {
        Scanner scan = new Scanner(System.in);
        String title = null;
        String description =null;
        double target = -1;
        String deadline = null;
        try {
            while(title == null) {
                System.out.print("Enter the title for Event: ");
                title = scan.nextLine();
            }
            while(description == null) {
                System.out.print("Enter the description for the Event: ");

                description = scan.nextLine();
            }
            while(target < 0) {
                System.out.print("Enter the target amount as a double (####.##): ");
                String temp = scan.nextLine();
                Scanner tempScan = new Scanner(temp);
                if (tempScan.hasNextDouble())
                    target = tempScan.nextDouble();
            }
            while (deadline == null) {
                System.out.println(
                        "Enter the deadline in the form YYYY-MM-DDTHH:MM.SSSZ\ne.g. Oct 25, 2023 at 1:45:30PM would be 2023-10-25T13:45.300Z: ");
                deadline = scan.nextLine();
            }

            return (new CreateEventRequest(title, description, target, deadline));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /*
     * tries to connect to the server
     */
    private static Socket connectToServer() {
        try {
            return new Socket(getHost(), getPort(), null, 0);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    /*
     * helper method of connectToServer to prompt the client for a server host
     */
    private static String getHost() {
        Scanner scan = new Scanner(System.in);
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
    private static int getPort() {
        Scanner scan = new Scanner(System.in);
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
            out.writeBytes(json.serialize(new Request(RequestType.EVENTS, json.serialize(new EventsRequest()))) + "\n");

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
    private static void updateEvent(ArrayList<Event> EVENTS, BufferedReader in, DataOutputStream out,
            WriteJsonObject json) {
        Scanner scan = new Scanner(System.in);
        try {
            System.out.println("choose an event or -1 to go back: ");
            int index = scan.nextInt() - 1;
            if (index < EVENTS.size()  && index >= 0) {
                Event newEvent = EVENTS.get(index);
                String update = changeEvent(newEvent, json);
                out.writeBytes(json.serialize(new Request(RequestType.UPDATE, update)) + "\n");
            } else if (index == -2) {

            } else {
                System.out.println("That index is not available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Prompts the user to pick an event and donates money to it
     */
    private static void donateToEvent(ArrayList<Event> EVENTS, BufferedReader in, DataOutputStream out,
            WriteJsonObject json) {
        Scanner scan = new Scanner(System.in);
        try {
            ArrayList<Event> curEvents = currentEvents(EVENTS);
            listEvents(curEvents);
            System.out.print("Choose and active event or -1 to go back: ");
            int index = scan.nextInt();
            if (index > 0 && index < curEvents.size()) {
                System.out.print("Enter a donation amount (####.##): ");
                double amount = scan.nextDouble();
                String donate = json.serialize(new DonateRequest(curEvents.get(index).getId(), amount));
                out.writeBytes(json.serialize(new Request(RequestType.DONATE, donate)));
            } else if (index == -1) {
                return;
            } else {
                System.out.println("That index is not available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Response response = json.deserialize(in.readLine(), Response.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * Prints current variables and prompts for new variable
     */
    private static String changeEvent(Event event, WriteJsonObject json) {
        Scanner scan = new Scanner(System.in);
        String title = null;
        while (title == null){
            System.out.printf("Current Title: %25s%n", event.getTitle());
            System.out.print("New Title: ");
            title = scan.nextLine();
        }
        if (!title.isEmpty())
            event.setTitle(title);

        String description = null;
        while(description == null) {
            System.out.printf("Current Description: %s%n", event.getDescription());
            System.out.print("New Description: ");
            description = scan.nextLine();
        }
        if (!description.isEmpty())
            event.setDescription(description);

        double target = -1.0;
        String temp;

        while(target < 0) {
            System.out.printf("Current Target: %-15f%n", event.getTarget());
            System.out.print("New Target: ");
            temp = scan.nextLine();
            Scanner scantemp = new Scanner(temp);
            if(scantemp.hasNextDouble())
                target = scantemp.nextDouble();
        }
        if (target != 0)
            event.setGoal(target);

        double balance = -1.0;

        while(balance < 0) {
            System.out.printf("Current Balance: %-15f%n", event.getBalance());
            System.out.print("New Balance: ");
            temp = scan.nextLine();
            Scanner scantemp = new Scanner(temp);
            if(scantemp.hasNextDouble())
                balance = scantemp.nextDouble();
        }
        if (balance != 0)
            event.setCurrentPool(balance);

        String date = null;
        while(date == null) {
            System.out.printf("Current Date: %-20s%n", event.getDeadlineString());
            System.out.print("New Date (YYYY-MM-DDTHH:MM.SSSZ): ");
            date = scan.nextLine();
        }
        if (!date.isEmpty())
            event.setEndDate(date);
        return json.serialize(event);
    }

    /*
     * helper method of chooseEvent that lists all events
     */
    private static void listEvents(ArrayList<Event> events) {
        int i = 1;
        for (Event event : events) {
            System.out.printf("%8d.    %-35s%n               = %-1s =%n", i++, event.getTitle(), event.getDescription());
        }
    }
}
