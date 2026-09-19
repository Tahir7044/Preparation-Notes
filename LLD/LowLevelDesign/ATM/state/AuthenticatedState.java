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
        if(option.equals("withdrawal")){
            atmMachine.setAtmState(new DispenseCashState(atmMachine));
        } else if(option.equals("balance")){
            atmMachine.setAtmState(new BalanceEnquiryState(atmMachine));
        } else {
            System.out.println("Invalid option.");
        }
    }

    @Override
    public void dispenseCash(int amount) {
        System.out.println("Please select an option first.");
    }

    @Override
    public void ejectCard() {
        atmMachine.setCurrentCard(null);
        System.out.println("Card ejected.");
        atmMachine.setAtmState(new IdleState(atmMachine));
    }

    @Override
    public void balanceEnquiry() {
        System.out.println("Please select an option first.");
    }

    @Override
    public ATMStatus getStatus() {
        return ATMStatus.AUTHENTICATED;
    }
}
