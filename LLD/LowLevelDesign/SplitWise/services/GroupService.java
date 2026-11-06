package LowLevelDesign.SplitWise.services;

import LowLevelDesign.SplitWise.enums.SplitType;
import LowLevelDesign.SplitWise.model.BalanceSheet;
import LowLevelDesign.SplitWise.model.Group;
import LowLevelDesign.SplitWise.model.Transaction;
import LowLevelDesign.SplitWise.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GroupService {
    private final Map<String, Group> groups = new ConcurrentHashMap<>();
    private final ExpenseService expenseService = new ExpenseService();
    private final DebtSimplificationService simplifier = new DebtSimplificationService();

    public String createGroup(String name, List<User> users) {
        String id = UUID.randomUUID().toString();
        groups.put(id, new Group(id, name, users));
        return id;
    }

    public void addMember(String groupId, User user) {
        getGroup(groupId).addMember(user);
    }

    public void addExpense(String groupId, String description, BigDecimal amount, User paidBy,
                           List<User> participants, SplitType splitType, Map<String, BigDecimal> metadata) {
        expenseService.addExpense(getGroup(groupId), description, amount, paidBy, participants, splitType, metadata);
    }

    public void settleUp(String groupId, String fromUserId, String toUserId, BigDecimal amount) {
        getGroup(groupId).recordPayment(fromUserId, toUserId, amount);
    }

    public BalanceSheet getBalanceSheet(String groupId, String userId) {
        return getGroup(groupId).getBalanceSheet(userId);
    }

    public List<Transaction> simplifyDebts(String groupId) {
        return simplifier.simplifyDebts(getGroup(groupId));
    }

    private Group getGroup(String groupId) {
        Group group = groups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("invalid group id");
        }
        return group;
    }
}
