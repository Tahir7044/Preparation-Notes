package LowLevelDesign.ATM;

// Functional Requirements:
// 1. User can insert a card into the ATM
// 2. User can enter a PIN — wrong PIN stays in CardInserted state, correct PIN moves to Authenticated
// 3. User can select an option after authentication: "withdrawal" or "balance"
// 4. User can withdraw cash — ATM dispenses using available denominations (greedy, largest first)
// 5. User can check account balance via balance enquiry option
// 6. Card is ejected automatically after each transaction (success or failure)

// Non-Functional Requirements:
// - Consistency: balance and ATM denomination counts update atomically — only on full dispense success
// - Extensible denominations: adding ₹200 note requires zero code changes in dispense logic
// - State safety: invalid operations in the wrong state are rejected with clear messages
// - Extensible operations: new ATM operations can be added as new states without touching existing ones

// Out of scope:
// - Deposit money
// - PIN change
// - Multiple PIN retry lockout

/*

Classes

- ATM
    - id: String
    - denominations: TreeMap<Integer, Integer>   ← sorted descending (2000 → 500 → 100)
    
    + getAvailableCash(): double                  ← computed from denominations map
    + getDenominations(): TreeMap

- Account
    - number: String
    - balance: double

- Card
    - number: String
    - pin: String
    - account: Account

- ATMMachine                                      ← facade over the current ATMState
    - atm: ATM
    - atmState: ATMState
    - currentCard: Card
    
    + insertCard(card)
    + enterPin(pin)
    + selectOption(option)
    + dispenseCash(amount)
    + ejectCard()
    + getCurrentState(): ATMStatus                ← returns current state as ATMStatus enum

State Pattern (ATMState interface — default methods handle invalid ops):
    IdleState            → insertCard transitions to CardInsertedState
    CardInsertedState    → enterPin: correct→AuthenticatedState, wrong→stays
    AuthenticatedState   → selectOption("withdrawal")→DispenseCashState
                           selectOption("balance")   →BalanceEnquiryState
    DispenseCashState    → dispenseCash: Chain of Responsibility across denominations
    BalanceEnquiryState  → balanceEnquiry: prints account balance, then ejectCard

Chain of Responsibility (inside DispenseCashState):
    CashDispenseHandler  (interface)
    DenominationHandler  (generic — one per denomination, built from ATM's TreeMap)

enum ATMStatus { IDLE, CARD_INSERTED, AUTHENTICATED, DISPENSE_CASH }
    ← each ATMState implementation returns its corresponding enum value via getStatus()

*/

import LowLevelDesign.ATM.model.ATM;
import LowLevelDesign.ATM.model.Account;
import LowLevelDesign.ATM.model.Card;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        // --- Setup ---
        ATM atm = new ATM("ATM-001", Map.of(2000, 5, 500, 10, 100, 20));
        ATMMachine machine = new ATMMachine(atm);

        Account account = new Account("ACC-001", 15000);
        Card card = new Card("4111-1111-1111-1111", "1234", account);

        // --- Scenario 1: Happy path withdrawal ---
        System.out.println("===== Scenario 1: Successful withdrawal =====");
        machine.insertCard(card);
        machine.enterPin("9999");           // wrong PIN
        machine.enterPin("1234");           // correct PIN
        machine.selectOption("withdrawal");
        machine.dispenseCash(4600);         // 2×₹2000 + 1×₹500 + 1×₹100
        System.out.println("Account balance after: ₹" + account.getBalance());
        System.out.println("ATM cash remaining:    ₹" + atm.getAvailableCash());

        // --- Scenario 2: Insufficient account balance ---
        System.out.println("\n===== Scenario 2: Insufficient account balance =====");
        machine.insertCard(card);
        machine.enterPin("1234");
        machine.selectOption("withdrawal");
        machine.dispenseCash(20000);        // more than account balance

        // --- Scenario 3: Amount not dispensable with available denominations ---
        System.out.println("\n===== Scenario 3: Amount not dispensable (e.g. ₹50) =====");
        machine.insertCard(card);
        machine.enterPin("1234");
        machine.selectOption("withdrawal");
        machine.dispenseCash(50);           // no ₹50 notes available

        // --- Scenario 4: Balance enquiry ---
        System.out.println("\n===== Scenario 4: Balance enquiry =====");
        machine.insertCard(card);
        machine.enterPin("1234");
        machine.selectOption("balance");
        machine.balanceEnquiry();           // prints balance, ejects card

        // --- Scenario 5: Invalid option after authentication ---
        System.out.println("\n===== Scenario 5: Invalid option =====");
        machine.insertCard(card);
        machine.enterPin("1234");
        machine.selectOption("transfer");   // unsupported — should print invalid option
        machine.ejectCard();                // clean up — card still in machine

        // --- Scenario 6: Invalid state operations ---
        System.out.println("\n===== Scenario 6: Invalid operations =====");
        machine.dispenseCash(500);          // no card inserted — should be rejected
        machine.enterPin("1234");           // no card inserted — should be rejected
        machine.balanceEnquiry();           // no card inserted — should be rejected
    }
}