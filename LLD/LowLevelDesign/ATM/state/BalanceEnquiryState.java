package LowLevelDesign.ATM.state;

import LowLevelDesign.ATM.ATMMachine;
import LowLevelDesign.ATM.enums.ATMStatus;
import LowLevelDesign.ATM.model.Card;

public class BalanceEnquiryState implements ATMState {
    private final ATMMachine atmMachine;

    public BalanceEnquiryState(ATMMachine atmMachine){
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
        System.out.println("Option already selected.");
    }

    @Override
    public void balanceEnquiry() {
        System.out.println("Your current account balance is: "+ this.atmMachine.getCurrentCard().getAccount().getBalance());
        ejectCard();
    }

    @Override
    public void dispenseCash(int amount) {
        System.out.println("Please select withdrawal option first.");
    }

    @Override
    public void ejectCard() {
        atmMachine.setCurrentCard(null);
        System.out.println("Card ejected.");
        atmMachine.setAtmState(new IdleState(atmMachine));
    }

    @Override
    public ATMStatus getStatus() {
        return ATMStatus.AUTHENTICATED;
    }
}
