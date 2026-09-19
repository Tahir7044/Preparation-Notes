package LowLevelDesign.ATM.model;

public class Card {

    private final String number;
    private final String pin;
    private final Account account;

    public Card(String number, String pin, Account account){
        this.account = account;
        this.pin = pin;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public Account getAccount() {
        return account;
    }
}
