package LowLevelDesign.ATM.state;

import LowLevelDesign.ATM.enums.ATMStatus;
import LowLevelDesign.ATM.model.Card;

public interface ATMState {
    void insertCard(Card card);
    void enterPin(String pin);
    void selectOption(String option);
    void dispenseCash(int amount);
    void ejectCard();
    ATMStatus getStatus();
}
