package LowLevelDesign.ATM.state;

import LowLevelDesign.ATM.ATMMachine;
import LowLevelDesign.ATM.enums.ATMStatus;
import LowLevelDesign.ATM.handler.CashDispenseHandler;
import LowLevelDesign.ATM.handler.DenominationHandler;
import LowLevelDesign.ATM.model.ATM;
import LowLevelDesign.ATM.model.Card;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class DispenseCashState implements ATMState{
    private final ATMMachine atmMachine;

    public DispenseCashState(ATMMachine atmMachine){
        this.atmMachine = atmMachine;
    }


    public void insertCard(Card card) {
        System.out.println("Card already inserted.");
    }


    public void enterPin(String pin) {
        System.out.println("Already authenticated.");
    }


    public void selectOption(String option) {
        System.out.println("Option already selected.");
    }


    public void dispenseCash(int amount) {

        ATM atm = atmMachine.getAtm();
        double balance = atmMachine.getCurrentCard().getAccount().getBalance();

        if (amount > balance) {
            System.out.println("Insufficient account balance.");
            ejectCard();
            return;
        }

        if (amount > atm.getAvailableCash()) {
            System.out.println("Insufficient cash available in ATM.");
            ejectCard();
            return;
        }

        // Work on a copy — only commit to ATM if dispensing fully succeeds
        TreeMap<Integer, Integer> denominationsCopy = new TreeMap<>(Collections.reverseOrder());
        denominationsCopy.putAll(atm.getDenominations());

        CashDispenseHandler chain = buildChain(denominationsCopy);
        int remainder = chain.handle(amount, denominationsCopy);

        if (remainder != 0) {
            System.out.println("Cannot dispense exact amount with available denominations.");
            ejectCard();
            return;
        }

        atm.getDenominations().putAll(denominationsCopy);
        atmMachine.getCurrentCard().getAccount().setBalance(balance - amount);

        System.out.println("Cash withdrawn successfully: " + amount);
        ejectCard();
    }

    private CashDispenseHandler buildChain(TreeMap<Integer, Integer> denominations) {
        CashDispenseHandler chain = null;
        for (int denom : denominations.descendingKeySet()) {
            DenominationHandler handler = new DenominationHandler(denom);
            handler.setNext(chain);
            chain = handler;
        }
        return chain;
    }


    public void ejectCard() {
        atmMachine.setCurrentCard(null);
        System.out.println("Card ejected.");
        atmMachine.setAtmState(new IdleState(atmMachine));
    }

    @Override
    public ATMStatus getStatus() {
        return ATMStatus.DISPENSE_CASH;
    }
}
