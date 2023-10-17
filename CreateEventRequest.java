import java.util.Date;
import com.fasterxml.jackson.annotation.JsonCreator;

public class CreateEventRequest extends Request {
    private String title;
    private String description;
    private double target;
    private Date deadline;
    private double balance;

    @JsonCreator
    public CreateEventRequest(String title, String description, double target, Date deadline) {
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
