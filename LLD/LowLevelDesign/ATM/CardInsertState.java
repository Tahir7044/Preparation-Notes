package LowLevelDesign.ATM;

public class CardInsertState implements ATMState {

    ATM atm;
    
    @Override
    public void insertCard(Card card) {
        System.out.println("Card inserted");
        atm.setCard(card);

        atm.setCurrentATMState(new AuthenticatePinState(atm));
        
    }

    @Override
    public void authenticatePin(Pin pin) {
        System.out.println("Please first insert the card");
    }

    @Override
    public void selectOperation(Option option) {
        System.out.println("Please first insert the card");
    }

    @Override
    public void cashWithdrawal(double amount) {
        System.out.println("Please first insert the card");
    }

    @Override
    public void displayBalance() {
        System.out.println("Please first insert the card");
    }

    @Override
    public void depositCash(double amount) {
        System.out.println("Please first insert the card");
    }

    @Override
    public void returnCard() {
        System.out.println("Please first insert the card");
    }

    @Override
    public void exit() {
        System.out.println("Please first insert the card");
    }

    @Override
    public ATMState getNextState() {
        return null;
    }
    
}
