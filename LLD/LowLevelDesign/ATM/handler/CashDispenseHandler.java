package LowLevelDesign.ATM.handler;

import java.util.Map;

public interface CashDispenseHandler {
    void setNext(CashDispenseHandler next);
    int handle(int amount, Map<Integer, Integer> denominations);
}
