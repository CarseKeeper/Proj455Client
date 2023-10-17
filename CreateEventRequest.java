import java.util.Date;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateEventRequest extends Request {
    public String title;
    public String description;
    public double target;
    public Date deadline;
    public double balance;

    @JsonCreator
    public CreateEventRequest(@JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("target") double target,
            @JsonProperty("deadline") Date deadline) {
        super(RequestType.CREATE);

        this.title = title;
        this.description = description;
        if (target > 0)
            this.target = target;
        else
            this.target = Math.abs(target);

        if (deadline.after(new Date()))
            this.deadline = deadline;
        else
            this.deadline = new Date();
        this.balance = 0.0;

    }
}
