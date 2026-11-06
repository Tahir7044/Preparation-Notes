package LowLevelDesign.SplitWise.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

// One user's view inside a group, keyed by the OTHER user's id.
// Positive = that user owes me, negative = I owe that user.
public class BalanceSheet {
    private final Map<String, BigDecimal> balances = new HashMap<>();

    public void addBalance(String userId, BigDecimal amount) {
        BigDecimal current = balances.getOrDefault(userId, BigDecimal.ZERO);
        balances.put(userId, current.add(amount));
    }

    public BigDecimal getBalance(String userId) {
        return balances.getOrDefault(userId, BigDecimal.ZERO);
    }

    public Map<String, BigDecimal> getBalances() {
        return balances;
    }
}
