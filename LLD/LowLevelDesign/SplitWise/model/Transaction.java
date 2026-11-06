package LowLevelDesign.SplitWise.model;

import java.math.BigDecimal;

// "from pays to amount"
public class Transaction {
    private final String fromUserId;
    private final String toUserId;
    private final BigDecimal amount;

    public Transaction(String fromUserId, String toUserId, BigDecimal amount) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return fromUserId + " pays " + toUserId + " " + amount;
    }
}
