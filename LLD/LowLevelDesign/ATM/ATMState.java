package LowLevelDesign.ATM;

public interface ATMState {
    void insertCard(Card card);
    void authenticatePin(Pin pin);
    void selectOperation(Option option);
    void cashWithdrawal(double amount);
    void displayBalance();
    void cashDispense();
    void depositCash(double amount);
    void returnCard();
    void exit();
    ATMState getNextState();
}
