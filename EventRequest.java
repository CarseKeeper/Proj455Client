import com.fasterxml.jackson.annotation.JsonCreator;

public class EventRequest extends Request {
    private int eventId;

    @JsonCreator
    public EventRequest(int id) {
        super(RequestType.EVENT);
        this.eventId = id;
    }

}
