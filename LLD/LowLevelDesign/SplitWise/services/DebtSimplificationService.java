package LowLevelDesign.SplitWise.services;

import LowLevelDesign.SplitWise.model.Group;
import LowLevelDesign.SplitWise.model.Transaction;
import LowLevelDesign.SplitWise.model.User;

import java.math.BigDecimal;
import java.util.*;

public class DebtSimplificationService {

    // Returns the fewest "X pays Y" transactions that settle the group. Stored balances are not changed.
    public List<Transaction> simplifyDebts(Group group) {
        group.getLock().lock();
        try {
            // Step 1: net balance per user (positive = is owed money, negative = owes money)
            Map<String, BigDecimal> net = new HashMap<>();
            for (User user : group.getMembers()) {
                BigDecimal total = BigDecimal.ZERO;
                for (BigDecimal amount : group.getBalanceSheet(user.getUserId()).getBalances().values()) {
                    total = total.add(amount);
                }
                net.put(user.getUserId(), total);
            }

            // Step 2: largest creditor first, largest debtor first
            PriorityQueue<String> creditors = new PriorityQueue<>((a, b) -> net.get(b).compareTo(net.get(a)));
            PriorityQueue<String> debtors = new PriorityQueue<>((a, b) -> net.get(a).compareTo(net.get(b)));
            for (Map.Entry<String, BigDecimal> entry : net.entrySet()) {
                if (entry.getValue().signum() > 0) {
                    creditors.offer(entry.getKey());
                } else if (entry.getValue().signum() < 0) {
                    debtors.offer(entry.getKey());
                }
            }

            // Step 3: match the biggest debtor with the biggest creditor until everyone is settled
            List<Transaction> transactions = new ArrayList<>();
            while (!creditors.isEmpty() && !debtors.isEmpty()) {
                String creditor = creditors.poll();
                String debtor = debtors.poll();

                BigDecimal settled = net.get(creditor).min(net.get(debtor).negate());
                transactions.add(new Transaction(debtor, creditor, settled));

                net.put(creditor, net.get(creditor).subtract(settled));
                net.put(debtor, net.get(debtor).add(settled));

                if (net.get(creditor).signum() > 0) {
                    creditors.offer(creditor);
                }
                if (net.get(debtor).signum() < 0) {
                    debtors.offer(debtor);
                }
            }
            return transactions;
        } finally {
            group.getLock().unlock();
        }
    }
}
