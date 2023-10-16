import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

public class Event implements Request {
    private int ID;
    private String title;
    private String description;
    private double goal;
    private double currentPool;
    private LocalDateTime endDate;
    private Account founder;

    public Event() {
        this.goal = 0.0;
        this.currentPool = 0.0;
        this.endDate = LocalDateTime.now();
        this.title = "You forgot something";
        this.description = "Go back and do it again.";
        this.founder = null;
    }

    public Event(String event, String description, double goal, LocalDateTime endDate, Account founder) {
        setTitle(event);
        setDescription(description);
        setGoal(goal);
        this.currentPool = 0.0;
        setEndDate(endDate);
        setFounder(founder);
    }

    // Getters
    public int getID() {
        return this.ID;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public double getGoal() {
        return this.goal;
    }

    public double getCurrentPool() {
        return this.currentPool;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public Account getFounder() {
        return this.founder;
    }

    // Setters
    private void setTitle(String event) {
        this.title = event;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private void setGoal(double goal) {
        if (goal >= 0.0)
            this.goal = goal;
        else
            this.goal = 0.0;
    }

    private void setCurrentPool(double donation) {
        if (donation >= 0.0)
            this.currentPool += donation;
    }

    private void setEndDate(LocalDateTime endDate) {
        if ((endDate.isAfter(this.endDate) && endDate.isAfter(LocalDateTime.now())) || this.endDate == null)
            this.endDate = endDate;
    }

    private void setFounder(Account founder) {
        this.founder = founder;
    }

    // Methods
    public boolean goalReached() {
        if (this.currentPool >= this.goal)
            return true;
        return false;
    }

    public String timeLeft() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime timeLeft = this.endDate.minusYears(now.getYear());
        timeLeft.minusMonths(now.getMonthValue());
        timeLeft.minusDays(now.getDayOfMonth());
        timeLeft.minusHours(now.getHour());
        timeLeft.minusMinutes(now.getMinute());
        timeLeft.minusSeconds(now.getSecond());
        return timeLeft.toString();
    }

    public boolean hasEnded() {
        if (LocalDateTime.now().isBefore(this.endDate))
            return false;
        return true;
    }

    public String formatEndDate() {
        return this.endDate.toString();
    }

    // pass Id not index

}
