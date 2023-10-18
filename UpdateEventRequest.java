import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateEventRequest {
    Event event;

    @JsonCreator
    UpdateEventRequest(@JsonProperty("Event") Event event) {
        this.event = event;
    }
}
