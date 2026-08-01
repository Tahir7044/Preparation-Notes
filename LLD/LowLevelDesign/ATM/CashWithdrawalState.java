package LowLevelDesign.ATM;

public class CashWithdrawalState implements ATMState {
    ATM atm;
    
    public CashWithdrawalState(ATM atm) {
        this.atm = atm;
    }
    
    public void insertCard(Card card) {
        System.out.println("Card already inserted");
    }
    
    public void authenticatePin(Pin pin) {
        System.out.println("Pin already authenticated");
    }
    
    public void selectOperation(Option option) {
        System.out.println("Operation already selected");
    }
    
    public void cashDispense() {
        System.out.println("First authenticate the pin to dispense cash");
    }
    
    public void cashWithdrawal(double amount) {
        System.out.println("Cash withdrawn");
        atm.setCurrentATMState(new SelectOperationState(atm));
    }
    
    public void displayBalance() {
        System.out.println("Balance displayed");
    }
    
    public void depositCash(double amount) {
        System.out.println("Cash deposited");
    }
    
    public void returnCard() {
        System.out.println("Card returned");
    }
    
    public void exit() {
        System.out.println("Exit");
    }
    
    public ATMState getNextState() {
        return new SelectOperationState(atm);
    }
}
