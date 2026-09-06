package LowLevelDesign.ATM;

import LowLevelDesign.ATM.enums.ATMStatus;
import LowLevelDesign.ATM.model.ATM;
import LowLevelDesign.ATM.model.Card;
import LowLevelDesign.ATM.state.ATMState;
import LowLevelDesign.ATM.state.IdleState;

public class ATMMachine {
    private final ATM atm;
    private ATMState atmState;
    private Card currentCard;

    public ATMMachine(ATM atm){
        this.atm = atm;
        this.atmState = new IdleState(this);
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public Card getCurrentCard(){
        return currentCard;
    }

    public void insertCard(Card card){
        atmState.insertCard(card);
    }

    public void enterPin(String pin){
        atmState.enterPin(pin);
    }

    public void selectOption(String option){
        atmState.selectOption(option);
    }

    public void dispenseCash(int amount){
        atmState.dispenseCash(amount);
    }

    public void ejectCard(){
        atmState.ejectCard();
    }

    public ATM getAtm(){
        return atm;
    }

    public void setAtmState(ATMState state){
        this.atmState = state;
    }

    public ATMStatus getCurrentState(){
        return atmState.getStatus();
    }
}
