import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventRequest extends Request {
    public int eventId;

    @JsonCreator
    public EventRequest(@JsonProperty("id") int id) {
        super(RequestType.EVENT);
        this.eventId = id;
    }

}
