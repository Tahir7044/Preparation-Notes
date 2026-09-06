package LowLevelDesign.ATM.state;

import LowLevelDesign.ATM.ATMMachine;
import LowLevelDesign.ATM.enums.ATMStatus;
import LowLevelDesign.ATM.model.Card;

public class CardInsertedState implements ATMState {
    private final ATMMachine atmMachine;

    public CardInsertedState(ATMMachine atmMachine){
        this.atmMachine = atmMachine;
    }

    @Override
    public void insertCard(Card card) {
        System.out.println("card  is inserted.");
    }

    @Override
    public void enterPin(String pin) {
        if (atmMachine.getCurrentCard().getPin().equals(pin)) {
            System.out.println("PIN correct. Authenticated.");
            atmMachine.setAtmState(new AuthenticatedState(atmMachine));
        } else {
            System.out.println("Invalid PIN.");
        }
    }

    @Override
    public void selectOption(String option) {
        System.out.println("Enter PIN first.");
    }

    @Override
    public void dispenseCash(int amount) {
        System.out.println("Enter PIN first.");
    }

    @Override
    public void ejectCard() {
        System.out.println("Enter PIN first.");
    }

    @Override
    public ATMStatus getStatus() {
        return ATMStatus.CARD_INSERTED;
    }
}