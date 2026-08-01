package LowLevelDesign.ATM;

public class ATM {

        ATMState currentState;
        Card card;

        public void setCurrentATMState(ATMState state) {
            this.currentState = state;
        }

        public void setCard(Card card) {
            this.card = card;
        }


}
