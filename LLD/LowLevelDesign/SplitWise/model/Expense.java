package LowLevelDesign.SplitWise.model;

import LowLevelDesign.SplitWise.enums.SplitType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Expense {
    private final String expenseId;
    private final String description;
    private final List<Split> splits;
    private final BigDecimal amount;
    private final User paidBy;
    private final SplitType splitType;

    public Expense(String description, List<Split> splits, BigDecimal amount, User paidBy, SplitType splitType) {
        this.expenseId = UUID.randomUUID().toString();
        this.description = description;
        this.splits = List.copyOf(splits);
        this.amount = amount;
        this.paidBy = paidBy;
        this.splitType = splitType;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public String getDescription() {
        return description;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public SplitType getSplitType() {
        return splitType;
    }
}
