import com.fasterxml.jackson.annotation.JsonCreator;

public class EventsRequest extends Request {
    @JsonCreator
    public EventsRequest() {
        super(RequestType.EVENTS);
    }
}
