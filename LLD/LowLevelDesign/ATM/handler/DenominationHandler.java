package LowLevelDesign.ATM.handler;

import java.util.Map;

public class DenominationHandler implements CashDispenseHandler {
    private final int denomination;
    private CashDispenseHandler next;

    public DenominationHandler(int denomination) {
        this.denomination = denomination;
    }

    @Override
    public void setNext(CashDispenseHandler next) {
        this.next = next;
    }

    @Override
    public int handle(int amount, Map<Integer, Integer> denominations) {
        int available = denominations.getOrDefault(denomination, 0);
        int notesNeeded = amount / denomination;
        int notesUsed = Math.min(notesNeeded, available);
        int notesRemaining = available - notesUsed;

        denominations.put(denomination, notesRemaining);

        int remainder = amount - (notesUsed * denomination);

        if (remainder == 0 || next == null) {
            return remainder;
        }

        return next.handle(remainder, denominations);
    }
}
