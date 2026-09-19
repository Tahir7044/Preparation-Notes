package LowLevelDesign.ATM.model;

import LowLevelDesign.ATM.enums.ATMStatus;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ATM {

    private final String id;
    private final TreeMap<Integer, Integer> denominations;

    public ATM(String id, Map<Integer, Integer> denominations){
        this.id = id;
        this.denominations = new TreeMap<>(Collections.reverseOrder());
        this.denominations.putAll(denominations);
    }

    public String getId() {
        return id;
    }

    public TreeMap<Integer, Integer> getDenominations() {
        return denominations;
    }

    public double getAvailableCash() {
        return denominations.entrySet().stream()
                .mapToLong(e -> (long) e.getKey() * e.getValue())
                .sum();
    }

}
