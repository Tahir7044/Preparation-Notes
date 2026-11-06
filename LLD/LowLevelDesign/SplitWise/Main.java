package LowLevelDesign.SplitWise;

import LowLevelDesign.SplitWise.enums.SplitType;
import LowLevelDesign.SplitWise.model.BalanceSheet;
import LowLevelDesign.SplitWise.model.Transaction;
import LowLevelDesign.SplitWise.model.User;
import LowLevelDesign.SplitWise.services.GroupService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// Functional Requirements:
// 1. Create a group with members, add members later
// 2. Add an expense paid by one member, shared by a subset of the group (payer may or may not be a participant)
// 3. Split types: EQUAL, PERCENTAGE, EXACT
// 4. View the balance sheet per group (who owes whom)
// 5. Settle up: one member pays another
// 6. Simplify the group's balances into the minimum number of transactions

// Non-Functional Requirements:
// - Money is BigDecimal, scale 2, shares rounded DOWN
// - Thread-safe: one ReentrantLock per group guards members, expenses and balance sheets
// - Split type is pluggable via SplitStrategy (Strategy pattern)

// Out of scope:
// - Edit / delete expense
// - Multiple payers, multiple currencies
// - Giving the rounding remainder (e.g. 0.01 on 100 / 3) to the payer
// - Per-user balance across groups
// - Notifications, UI

/*

Classes

- User
    - userId, name

- Split
    - user, amount

- Expense                               ← immutable
    - expenseId, description, amount, paidBy: User, splitType
    - splits: List<Split>

- BalanceSheet                          ← one per member per group
    - balances: Map<otherUserId, BigDecimal>   (positive = they owe me, negative = I owe them)
    + addBalance(userId, amount), getBalance(userId)

- Group
    - groupId, name
    - members: Map<userId, User>
    - expenses: List<Expense>
    - balanceSheets: Map<userId, BalanceSheet>
    - lock: ReentrantLock
    + addMember(user)
    + recordExpense(expense)            ← payer is credited with every other share
    + recordPayment(from, to, amount)   ← settle up

- Transaction
    - fromUserId, toUserId, amount      ← "from pays to"

- ExpenseService
    + addExpense(group, ..., splitType, metadata)   ← validates, picks strategy, records the expense

- DebtSimplificationService
    + simplifyDebts(group): List<Transaction>       ← greedy: biggest debtor pays biggest creditor

- GroupService                          ← entry point
    + createGroup, addMember, addExpense, settleUp, getBalanceSheet, simplifyDebts

Strategy Pattern:
    SplitStrategy → split(amount, participants, metadata): List<Split>
        - EqualSplitStrategy
        - PercentageSplitStrategy       ← metadata: userId -> percent, must total 100
        - ExactSplitStrategy            ← metadata: userId -> amount, must total the expense

enum SplitType { EQUAL, PERCENTAGE, EXACT }

*/

public class Main {

    static int failures = 0;

    static void check(String name, boolean condition) {
        System.out.println((condition ? "PASS  " : "FAIL  ") + name);
        if (!condition) failures++;
    }

    static BigDecimal b(String value) {
        return new BigDecimal(value);
    }

    static boolean eq(BigDecimal actual, String expected) {
        return actual.compareTo(b(expected)) == 0;
    }

    static void print(GroupService service, String groupId, List<User> users) {
        for (User u : users) {
            BalanceSheet sheet = service.getBalanceSheet(groupId, u.getUserId());
            System.out.println("      " + u.getName() + " -> " + sheet.getBalances());
        }
    }

    public static void main(String[] args) throws Exception {
        testEqualSplitWithRemainder();
        testPercentageSplit();
        testExactSplit();
        testPayerNotInSplit();
        testSettleUp();
        testSimplify();
        testValidation();
        testConcurrentExpenses();

        System.out.println();
        System.out.println(failures == 0 ? "ALL TESTS PASSED" : failures + " TEST(S) FAILED");
        System.exit(failures == 0 ? 0 : 1);
    }

    // Scenario 1: 100.00 split 3 ways. Shares round down to 33.33, the extra 0.01 is not charged to anyone
    static void testEqualSplitWithRemainder() {
        System.out.println("-- equal split with rounding remainder");
        GroupService service = new GroupService();
        User a = new User("A", "Alice"), b = new User("B", "Bob"), c = new User("C", "Carol");
        String g = service.createGroup("trip", List.of(a, b, c));
        service.addExpense(g, "dinner", b("100.00"), a, List.of(a, b, c), SplitType.EQUAL, null);
        print(service, g, List.of(a, b, c));
        check("Bob owes Alice 33.33", eq(service.getBalanceSheet(g, "A").getBalance("B"), "33.33"));
        check("Carol owes Alice 33.33", eq(service.getBalanceSheet(g, "A").getBalance("C"), "33.33"));
        check("shares round down: Alice is owed 66.66 in total, not 66.67",
                eq(service.getBalanceSheet(g, "A").getBalance("B").add(service.getBalanceSheet(g, "A").getBalance("C")), "66.66"));
    }

    // Scenario 2: 50.00 taxi paid by Bob, Bob 30% / Carol 70%
    static void testPercentageSplit() {
        System.out.println("-- percentage split");
        GroupService service = new GroupService();
        User a = new User("A", "Alice"), b = new User("B", "Bob"), c = new User("C", "Carol");
        String g = service.createGroup("trip", List.of(a, b, c));
        service.addExpense(g, "taxi", b("50.00"), b, List.of(b, c), SplitType.PERCENTAGE,
                Map.of("B", b("30"), "C", b("70")));
        check("Carol owes Bob 35.00 (70% of 50)", eq(service.getBalanceSheet(g, "B").getBalance("C"), "35.00"));
        check("Carol's sheet is the mirror (-35.00)", eq(service.getBalanceSheet(g, "C").getBalance("B"), "-35.00"));
    }

    // Scenario 3: 90.00 hotel paid by Carol, exact amounts Alice 40 / Bob 50
    static void testExactSplit() {
        System.out.println("-- exact split");
        GroupService service = new GroupService();
        User a = new User("A", "Alice"), b = new User("B", "Bob"), c = new User("C", "Carol");
        String g = service.createGroup("trip", List.of(a, b, c));
        service.addExpense(g, "hotel", b("90.00"), c, List.of(a, b), SplitType.EXACT,
                Map.of("A", b("40"), "B", b("50")));
        check("Alice owes Carol 40.00", eq(service.getBalanceSheet(g, "C").getBalance("A"), "40.00"));
        check("Bob owes Carol 50.00", eq(service.getBalanceSheet(g, "C").getBalance("B"), "50.00"));
    }

    // Scenario 4: Alice pays 10.00 for Bob and Carol but is not part of the split
    static void testPayerNotInSplit() {
        System.out.println("-- payer is not a participant");
        GroupService service = new GroupService();
        User a = new User("A", "Alice"), b = new User("B", "Bob"), c = new User("C", "Carol");
        String g = service.createGroup("trip", List.of(a, b, c));
        service.addExpense(g, "gift", b("10.00"), a, List.of(b, c), SplitType.EQUAL, null);
        check("Bob owes Alice 5.00", eq(service.getBalanceSheet(g, "A").getBalance("B"), "5.00"));
        check("Carol owes Alice 5.00", eq(service.getBalanceSheet(g, "A").getBalance("C"), "5.00"));
    }

    // Scenario 5: Bob owes Alice 33.33, he pays 20.00, then the rest
    static void testSettleUp() {
        System.out.println("-- settle up");
        GroupService service = new GroupService();
        User a = new User("A", "Alice"), b = new User("B", "Bob");
        String g = service.createGroup("flat", List.of(a, b));
        service.addExpense(g, "groceries", b("66.66"), a, List.of(a, b), SplitType.EQUAL, null);
        check("Bob owes Alice 33.33", eq(service.getBalanceSheet(g, "A").getBalance("B"), "33.33"));
        service.settleUp(g, "B", "A", b("20.00"));
        check("after paying 20.00, Bob owes 13.33", eq(service.getBalanceSheet(g, "A").getBalance("B"), "13.33"));
        service.settleUp(g, "B", "A", b("13.33"));
        check("fully settled: Bob's sheet is zero", eq(service.getBalanceSheet(g, "B").getBalance("A"), "0"));
    }

    // Scenario 6: Alice is owed 10 by Bob, Bob is owed 10 by Carol  ->  one transaction: Carol pays Alice 10
    static void testSimplify() {
        System.out.println("-- simplify debts");
        GroupService service = new GroupService();
        User a = new User("A", "Alice"), b = new User("B", "Bob"), c = new User("C", "Carol");
        String g = service.createGroup("chain", List.of(a, b, c));
        service.addExpense(g, "e1", b("10.00"), a, List.of(b), SplitType.EXACT, Map.of("B", b("10")));
        service.addExpense(g, "e2", b("10.00"), b, List.of(c), SplitType.EXACT, Map.of("C", b("10")));
        List<Transaction> transactions = service.simplifyDebts(g);
        System.out.println("      " + transactions);
        check("2 debts collapse into 1 transaction", transactions.size() == 1);
        check("Carol pays Alice 10.00", transactions.get(0).getFromUserId().equals("C")
                && transactions.get(0).getToUserId().equals("A") && eq(transactions.get(0).getAmount(), "10.00"));
        check("stored balances are untouched (Bob still owes Alice 10.00)", eq(service.getBalanceSheet(g, "A").getBalance("B"), "10.00"));

        User d = new User("D", "Dan");
        String g2 = service.createGroup("four", List.of(a, b, c, d));
        service.addExpense(g2, "x", b("90.00"), a, List.of(a, b, c, d), SplitType.EXACT,
                Map.of("A", b("0"), "B", b("30"), "C", b("30"), "D", b("30")));
        service.addExpense(g2, "y", b("40.00"), b, List.of(c, d), SplitType.EXACT, Map.of("C", b("20"), "D", b("20")));
        List<Transaction> t2 = service.simplifyDebts(g2);
        System.out.println("      " + t2);
        check("net is settled with at most n-1 = 3 transactions", t2.size() <= 3);
        BigDecimal paid = t2.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        check("total moved = 100.00 (Carol and Dan each end up owing 50; Alice is owed 90, Bob 10)", eq(paid, "100.00"));
    }

    // Scenario 7: bad input is rejected
    static void testValidation() {
        System.out.println("-- validation");
        GroupService service = new GroupService();
        User a = new User("A", "Alice"), b = new User("B", "Bob");
        User outsider = new User("Z", "Zed");
        String g = service.createGroup("g", List.of(a, b));
        check("percentages not totalling 100 rejected", fails(() ->
                service.addExpense(g, "x", b("100"), a, List.of(a, b), SplitType.PERCENTAGE, Map.of("A", b("60"), "B", b("30")))));
        check("exact amounts not totalling the expense rejected", fails(() ->
                service.addExpense(g, "x", b("100"), a, List.of(a, b), SplitType.EXACT, Map.of("A", b("60"), "B", b("30")))));
        check("participant outside the group rejected", fails(() ->
                service.addExpense(g, "x", b("100"), a, List.of(a, outsider), SplitType.EQUAL, null)));
        check("payer outside the group rejected", fails(() ->
                service.addExpense(g, "x", b("100"), outsider, List.of(a, b), SplitType.EQUAL, null)));
        check("zero amount rejected", fails(() ->
                service.addExpense(g, "x", b("0"), a, List.of(a, b), SplitType.EQUAL, null)));
        check("unknown group rejected", fails(() -> service.simplifyDebts("nope")));
    }

    static boolean fails(Runnable action) {
        try {
            action.run();
            return false;
        } catch (RuntimeException e) {
            return true;
        }
    }

    // Scenario 8: 8 threads add 500 expenses each to one group. Nothing is lost and balances stay consistent
    static void testConcurrentExpenses() throws InterruptedException {
        System.out.println("-- concurrent expenses");
        GroupService service = new GroupService();
        List<User> users = List.of(new User("A", "A"), new User("B", "B"), new User("C", "C"), new User("D", "D"));
        String g = service.createGroup("busy", users);
        int threads = 8, perThread = 500;
        AtomicInteger errors = new AtomicInteger();
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int t = 0; t < threads; t++) {
            final int seed = t;
            pool.submit(() -> {
                try {
                    for (int i = 0; i < perThread; i++) {
                        User payer = users.get((seed + i) % users.size());
                        service.addExpense(g, "e", b("12.35"), payer, users, SplitType.EQUAL, null);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    errors.incrementAndGet();
                }
            });
        }
        pool.shutdown();
        check("workers finished", pool.awaitTermination(30, TimeUnit.SECONDS));
        check("no exceptions", errors.get() == 0);

        BigDecimal totalNet = BigDecimal.ZERO;
        boolean mirrored = true;
        for (User u : users) {
            for (User v : users) {
                BigDecimal ab = service.getBalanceSheet(g, u.getUserId()).getBalance(v.getUserId());
                BigDecimal ba = service.getBalanceSheet(g, v.getUserId()).getBalance(u.getUserId());
                if (ab.add(ba).signum() != 0) mirrored = false;
                totalNet = totalNet.add(ab);
            }
        }
        check("every pair is mirrored (A owes B == -(B owes A))", mirrored);
        check("all balances add up to zero (no money created or lost)", totalNet.signum() == 0);
    }
}
