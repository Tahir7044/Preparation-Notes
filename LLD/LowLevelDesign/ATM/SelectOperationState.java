package LowLevelDesign.ATM;

public class SelectOperationState implements ATMState {
    ATM atm;
    
    public SelectOperationState(ATM atm) {
        this.atm = atm;
    }
    
    public void insertCard(Card card) {
        System.out.println("Card already inserted");
    }
    
    public void authenticatePin(Pin pin) {
        System.out.println("Pin already authenticated");
    }
    
    public void selectOperation(Option option) {
        System.out.println("Operation selected");
        atm.setCurrentATMState(new CashWithdrawalState(atm));
    }
    
    public void cashWithdrawal(double amount) {
        System.out.println("Please first select the operation");
    }

    public void cashDispense() {
        System.out.println("First select the operation to dispense cash");
    }
    
    public void displayBalance() {
        System.out.println("Please first select the operation");
    }
    
    public void depositCash(double amount) {
        System.out.println("Please first select the operation");
    }
    
    public void returnCard() {
        System.out.println("Please first select the operation");
    }
    
    public void exit() {
        System.out.println("Please first select the operation");
    }

    @Override
    public ATMState getNextState() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNextState'");
    }
}
