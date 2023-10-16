import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Client {

    ArrayList<Event> EVENTS = new ArrayList<Event>();

    public void main(String[] args) {

    }

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

    private void createEvent(Event newEvent) {

    }

    private static Socket connectToServer() {

    }
}
