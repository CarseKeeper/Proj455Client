import java.text.SimpleDateFormat;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateEventRequest {
    public String title;
    public String description;
    public double target;
    public Date deadline;
    public double balance;

    @JsonCreator
    public CreateEventRequest(@JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("target") double target,
            @JsonProperty("deadline") String deadline) {

        this.title = title;
        this.description = description;
        if (target > 0)
            this.target = target;
        else
            this.target = Math.abs(target);

        try {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            if (date.parse(deadline).after(new Date()))
                this.deadline = date.parse(deadline);
            else
                this.deadline = new Date();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.balance = 0.0;

    }
}
