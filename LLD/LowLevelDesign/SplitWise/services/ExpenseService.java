package LowLevelDesign.SplitWise.services;

import LowLevelDesign.SplitWise.Strategy.EqualSplitStrategy;
import LowLevelDesign.SplitWise.Strategy.ExactSplitStrategy;
import LowLevelDesign.SplitWise.Strategy.PercentageSplitStrategy;
import LowLevelDesign.SplitWise.Strategy.SplitStrategy;
import LowLevelDesign.SplitWise.enums.SplitType;
import LowLevelDesign.SplitWise.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ExpenseService {

    private final Map<SplitType, SplitStrategy> strategies = Map.of(
            SplitType.EQUAL, new EqualSplitStrategy(),
            SplitType.PERCENTAGE, new PercentageSplitStrategy(),
            SplitType.EXACT, new ExactSplitStrategy());

    public void addExpense(Group group, String description, BigDecimal amount, User paidBy,
                              List<User> participants, SplitType splitType, Map<String, BigDecimal> metadata) {
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (!group.isMember(paidBy.getUserId())) {
            throw new IllegalArgumentException("payer is not a group member");
        }
        for (User user : participants) {
            if (!group.isMember(user.getUserId())) {
                throw new IllegalArgumentException(user.getName() + " is not a group member");
            }
        }

        List<Split> splits = strategies.get(splitType).split(amount, participants, metadata);
//        splits = giveRemainderToPayer(splits, amount, paidBy);

        Expense expense = new Expense(description, splits, amount, paidBy, splitType);
        group.recordExpense(expense);
    }

//    // rounding leaves a few cents unassigned; the payer absorbs them
//    private List<Split> giveRemainderToPayer(List<Split> splits, BigDecimal amount, User paidBy) {
//        BigDecimal assigned = BigDecimal.ZERO;
//        for (Split split : splits) {
//            assigned = assigned.add(split.getAmount());
//        }
//        BigDecimal remainder = amount.subtract(assigned);
//        if (remainder.signum() == 0) {
//            return splits;
//        }
//
//        List<Split> result = new ArrayList<>();
//        boolean payerFound = false;
//        for (Split split : splits) {
//            if (split.getUser().getUserId().equals(paidBy.getUserId())) {
//                result.add(new Split(split.getUser(), split.getAmount().add(remainder)));
//                payerFound = true;
//            } else {
//                result.add(split);
//            }
//        }
//        if (!payerFound) {
//            result.add(new Split(paidBy, remainder));
//        }
//        return result;
//    }
}
