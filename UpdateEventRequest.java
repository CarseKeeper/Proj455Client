import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateEventRequest {
    public int eventid;
    public String title;
    public String description;
    public double target;
    public Date deadline;
    public double balance;

    @JsonCreator
    UpdateEventRequest(@JsonProperty("Event") Event event) {

    }
}
