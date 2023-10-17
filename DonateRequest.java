import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DonateRequest extends Request {
    public double amount;
    public int eventid;

    @JsonCreator
    public DonateRequest(@JsonProperty("id") int eventid,
            @JsonProperty("amount") double amount) {
        super(RequestType.DONATE);
        if (eventid > 0)
            this.eventid = eventid;
        else
            this.eventid = Math.abs(eventid);

        if (amount > 0.0)
            this.amount = amount;
        else
            this.amount = Math.abs(amount);
    }
}
