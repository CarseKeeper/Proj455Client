import com.fasterxml.jackson.annotation.JsonCreator;

public class DonateRequest extends Request {
    private double amount;
    private int eventid;

    @JsonCreator
    public DonateRequest(int eventid, double amount) {
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
