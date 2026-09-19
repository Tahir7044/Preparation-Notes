package LowLevelDesign.ATM.model;

public class Account {
    private final String number;
    private double balance;
    public Account(String number, double balance){
        this.balance = balance;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
