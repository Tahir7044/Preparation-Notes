package LowLevelDesign.SplitWise.model;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Group {
    private final String groupId;
    private final String name;
    private final Map<String, User> members = new LinkedHashMap<>();
    private final List<Expense> expenses = new ArrayList<>();
    private final Map<String, BalanceSheet> balanceSheets = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public Group(String groupId, String name, List<User> initialMembers) {
        this.groupId = groupId;
        this.name = name;
        for (User user : initialMembers) {
            addMember(user);
        }
    }

    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public List<User> getMembers() {
        return new ArrayList<>(members.values());
    }

    public boolean isMember(String userId) {
        return members.containsKey(userId);
    }

    public BalanceSheet getBalanceSheet(String userId) {
        return balanceSheets.get(userId);
    }

    public Map<String, BalanceSheet> getBalanceSheets() {
        return balanceSheets;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void addMember(User user) {
        lock.lock();
        try {
            members.put(user.getUserId(), user);
            balanceSheets.putIfAbsent(user.getUserId(), new BalanceSheet());
        } finally {
            lock.unlock();
        }
    }

    // payer is owed every other participant's share; the payer's own share nets to zero
    public void recordExpense(Expense expense) {
        lock.lock();
        try {
            String payerId = expense.getPaidBy().getUserId();
            for (Split split : expense.getSplits()) {
                String userId = split.getUser().getUserId();
                if (!userId.equals(payerId)) {
                    transfer(payerId, userId, split.getAmount());
                }
            }
            expenses.add(expense);
        } finally {
            lock.unlock();
        }
    }

    // fromUserId pays toUserId, so fromUserId owes less
    public void recordPayment(String fromUserId, String toUserId, BigDecimal amount) {
        lock.lock();
        try {
            transfer(toUserId, fromUserId, amount.negate());
        } finally {
            lock.unlock();
        }
    }

    // creditor is owed `amount` more by debtor
    private void transfer(String creditorId, String debtorId, BigDecimal amount) {
        balanceSheets.get(creditorId).addBalance(debtorId, amount);
        balanceSheets.get(debtorId).addBalance(creditorId, amount.negate());
    }
}
