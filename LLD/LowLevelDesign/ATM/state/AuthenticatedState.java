package LowLevelDesign.ATM.state;

import LowLevelDesign.ATM.ATMMachine;
import LowLevelDesign.ATM.enums.ATMStatus;
import LowLevelDesign.ATM.model.Card;

public class AuthenticatedState implements ATMState {
    private final ATMMachine atmMachine;

    public AuthenticatedState(ATMMachine atmMachine){
        this.atmMachine = atmMachine;
    }

    @Override
    public void insertCard(Card card) {
        System.out.println("Card already inserted.");
    }

    @Override
    public void enterPin(String pin) {
        System.out.println("Already authenticated.");
    }

    @Override
    public void selectOption(String option) {
        System.out.println("Option selected: Withdrawal.");  // here we can select option to show balance
        atmMachine.setAtmState(new DispenseCashState(atmMachine));
    }

    @Override
    public void dispenseCash(int amount) {
        System.out.println("Please select an option first.");
    }

    @Override
    public void ejectCard() {
        System.out.println("Please select an option first.");
    }

    @Override
    public ATMStatus getStatus() {
        return ATMStatus.AUTHENTICATED;
    }
}
