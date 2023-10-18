import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventRequest {
    public int eventId;

    @JsonCreator
    public EventRequest(@JsonProperty("id") int id) {
        this.eventId = id;
    }

}
