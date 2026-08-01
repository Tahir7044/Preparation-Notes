package LowLevelDesign.ATM;

public class AuthenticatePinState implements ATMState {
    ATM atm;
    
    public AuthenticatePinState(ATM atm) {
        this.atm = atm;
    }
    
    public void insertCard(Card card) {
        System.out.println("Card already inserted");
    }
    
    public void authenticatePin(Pin pin) {
        System.out.println("Pin authenticated");
        atm.setCurrentATMState(new SelectOperationState(atm));
    }

    public void cashDispense() {
        System.out.println("First authenticate the pin to dispense cash");
    }
    
    public void selectOperation(Option option) {
        System.out.println("Please first authenticate the pin");
    }
    
    public void cashWithdrawal(double amount) {
        System.out.println("Please first authenticate the pin");
    }
    
    public void displayBalance() {
        System.out.println("Please first authenticate the pin");
    }
    
    public void depositCash(double amount) {
        System.out.println("Please first authenticate the pin");
    }
    
    public void returnCard() {
        System.out.println("Please first authenticate the pin");
    }
    
    public void exit() {
        System.out.println("Please first authenticate the pin");
    }
    
    public ATMState getNextState() {
        return new SelectOperationState(atm);
    }
}
