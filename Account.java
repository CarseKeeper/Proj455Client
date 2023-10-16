public class Account implements Request {
    private String name;
    private double balance = 0.0;

    public Account() {
        this.name = "";
        this.balance = 0.0;
    }

    public Account(String name, double balance) {
        setName(name);
        deposit(balance);
    }

    // Getters
    public String getName() {
        return this.name;
    }

    public double getBalance() {
        return this.balance;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    // Methods
    public void deposit(double balance) {
        if (balance >= 0.0)
            this.balance += balance;
    }

    public void withdraw(double balance) {
        if (balance >= 0.0)
            this.balance -= balance;
    }
}
