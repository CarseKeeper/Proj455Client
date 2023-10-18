import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateEventRequest {
    String event;

    @JsonCreator
    UpdateEventRequest(@JsonProperty("Event") String event) {

    }
}
