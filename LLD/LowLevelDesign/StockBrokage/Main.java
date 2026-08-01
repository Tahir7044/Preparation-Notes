public class Main{
    public static void main(String[] args) {

        // Input format: <order-id> <time> <stock> <buy/sell> <price> <qty>
        // #1 09:45 Meesho sell 240.12 100
        // #2 09:46 Meesho sell 237.45  90
        // #3 09:47 Meesho buy  238.10 110
        // #4 09:48 Meesho buy  237.80  10
        // #5 09:49 Meesho buy  237.80  40
        // #6 09:50 Meesho sell 236.00  50

        StockExchange exchange = new StockExchange("NSE");

        // Add stocks of different companies
        Stock meesho = new Stock("Meesho", "Meesho", 240.0);
        Stock tcs = new Stock("TCS", "Tata Consultancy Services", 3500.0);
        Stock infy = new Stock("INFY", "Infosys", 1500.0);
        Stock reliance = new Stock("RELIANCE", "Reliance Industries", 2400.0);
        exchange.addStock(meesho);
        exchange.addStock(tcs);
        exchange.addStock(infy);
        exchange.addStock(reliance);

        // ===== Show Stock List in Exchange =====
        System.out.println("===== Stocks Listed on " + exchange.getName() + " =====");
        for (Stock s : exchange.getStocks()) {
            System.out.println(s.getStockId() + " | " + s.getName() + " | price: " + s.getPrice());
        }

        // Meesho orders
        Order order1  = new Order("#1",  "user1", "Meesho", 100, 240.12, OrderType.SELL, OrderStatus.OPEN, "09:45");
        Order order2  = new Order("#2",  "user2", "Meesho",  90, 237.45, OrderType.SELL, OrderStatus.OPEN, "09:46");
        Order order3  = new Order("#3",  "user3", "Meesho", 110, 238.10, OrderType.BUY,  OrderStatus.OPEN, "09:47");
        Order order4  = new Order("#4",  "user4", "Meesho",  10, 237.80, OrderType.BUY,  OrderStatus.OPEN, "09:48");
        Order order5  = new Order("#5",  "user5", "Meesho",  40, 237.80, OrderType.BUY,  OrderStatus.OPEN, "09:49");
        Order order6  = new Order("#6",  "user6", "Meesho",  50, 236.00, OrderType.SELL, OrderStatus.OPEN, "09:50");

        // TCS orders
        Order order7  = new Order("#7",  "user1", "TCS",  50, 3520.0, OrderType.BUY,  OrderStatus.OPEN, "09:45");
        Order order8  = new Order("#8",  "user2", "TCS",  30, 3510.0, OrderType.SELL, OrderStatus.OPEN, "09:46");
        Order order9  = new Order("#9",  "user3", "TCS",  20, 3500.0, OrderType.SELL, OrderStatus.OPEN, "09:47");

        // INFY orders
        Order order10 = new Order("#10", "user4", "INFY", 60, 1520.0, OrderType.BUY,  OrderStatus.OPEN, "09:48");
        Order order11 = new Order("#11", "user5", "INFY", 40, 1510.0, OrderType.SELL, OrderStatus.OPEN, "09:49");
        Order order12 = new Order("#12", "user6", "INFY", 25, 1525.0, OrderType.BUY,  OrderStatus.OPEN, "09:50");
        Order order13 = new Order("#13", "user1", "INFY", 30, 1505.0, OrderType.SELL, OrderStatus.OPEN, "09:51");

        // RELIANCE orders
        Order order14 = new Order("#14", "user2", "RELIANCE", 80, 2420.0, OrderType.BUY,  OrderStatus.OPEN, "09:45");
        Order order15 = new Order("#15", "user3", "RELIANCE", 50, 2410.0, OrderType.SELL, OrderStatus.OPEN, "09:46");
        Order order16 = new Order("#16", "user4", "RELIANCE", 30, 2415.0, OrderType.BUY,  OrderStatus.OPEN, "09:47");
        Order order17 = new Order("#17", "user5", "RELIANCE", 60, 2400.0, OrderType.SELL, OrderStatus.OPEN, "09:48");
        Order order18 = new Order("#18", "user6", "RELIANCE", 20, 2425.0, OrderType.BUY,  OrderStatus.OPEN, "09:49");

        // More TCS orders
        Order order19 = new Order("#19", "user4", "TCS", 40, 3515.0, OrderType.BUY,  OrderStatus.OPEN, "09:48");
        Order order20 = new Order("#20", "user5", "TCS", 25, 3505.0, OrderType.SELL, OrderStatus.OPEN, "09:49");

        exchange.addOrder(order1);
        exchange.addOrder(order2);
        exchange.addOrder(order3);
        exchange.addOrder(order4);
        exchange.addOrder(order5);
        exchange.addOrder(order6);
        exchange.addOrder(order7);
        exchange.addOrder(order8);
        exchange.addOrder(order9);
        exchange.addOrder(order10);
        exchange.addOrder(order11);
        exchange.addOrder(order12);
        exchange.addOrder(order13);
        exchange.addOrder(order14);
        exchange.addOrder(order15);
        exchange.addOrder(order16);
        exchange.addOrder(order17);
        exchange.addOrder(order18);
        exchange.addOrder(order19);
        exchange.addOrder(order20);

        // ===== Verify Buy Order Book per stock (highest price first, then earliest time) =====
        System.out.println("\nBuy Order Book (priority order):");
        for (java.util.Map.Entry<String, java.util.PriorityQueue<Order>> entry : exchange.getBuyOrders().entrySet()) {
            System.out.println("  " + entry.getKey() + ":");
            java.util.PriorityQueue<Order> pq = new java.util.PriorityQueue<>(entry.getValue());
            while (!pq.isEmpty()) {
                Order o = pq.poll();
                System.out.println("    " + o.getOrderId() + " | " + o.getTimestamp() + " | BUY | " + o.getPrice() + " | " + o.getQuantity());
            }
        }

        // ===== Verify Sell Order Book per stock (lowest price first, then earliest time) =====
        System.out.println("\nSell Order Book (priority order):");
        for (java.util.Map.Entry<String, java.util.PriorityQueue<Order>> entry : exchange.getSellOrders().entrySet()) {
            System.out.println("  " + entry.getKey() + ":");
            java.util.PriorityQueue<Order> pq = new java.util.PriorityQueue<>(entry.getValue());
            while (!pq.isEmpty()) {
                Order o = pq.poll();
                System.out.println("    " + o.getOrderId() + " | " + o.getTimestamp() + " | SELL | " + o.getPrice() + " | " + o.getQuantity());
            }
        }

        // ===== Trades (format: <buy-order-id> <sell-price> <qty> <sell-order-id>) =====
        System.out.println("\nTrades executed:");
        for (Trade t : exchange.getTrades()) {
            System.out.println(t);
        }

        // ===== Test: Cancel Order =====
        System.out.println("\n===== Cancel Order Test =====");

        // Add a new buy order, then cancel it before it matches
        Order cancelTestOrder = new Order("#21", "user1", "RELIANCE", 100, 2430.0, OrderType.BUY, OrderStatus.OPEN, "10:00");
        exchange.addOrder(cancelTestOrder);
        System.out.println("Added order: " + cancelTestOrder);

        System.out.println("RELIANCE buy orders before cancel: " + exchange.getBuyOrders().get("RELIANCE").size());
        exchange.cancelOrder(cancelTestOrder);
        System.out.println("RELIANCE buy orders after cancel:  " + exchange.getBuyOrders().get("RELIANCE").size());

        // Cancel a sell order
        Order cancelSellOrder = new Order("#22", "user2", "RELIANCE", 70, 2390.0, OrderType.SELL, OrderStatus.OPEN, "10:01");
        exchange.addOrder(cancelSellOrder);
        System.out.println("\nAdded sell order: " + cancelSellOrder);

        System.out.println("RELIANCE sell orders before cancel: " + exchange.getSellOrders().get("RELIANCE").size());
        exchange.cancelOrder(cancelSellOrder);
        System.out.println("RELIANCE sell orders after cancel:  " + exchange.getSellOrders().get("RELIANCE").size());

        // ===== Test: Edit Order (change quantity) =====
        System.out.println("\n===== Edit Order Test (change qty) =====");

        Order editQtyOrder = new Order("#23", "user3", "TCS", 15, 3530.0, OrderType.BUY, OrderStatus.OPEN, "10:02");
        exchange.addOrder(editQtyOrder);
        System.out.println("Added order: " + editQtyOrder);

        // Edit: change quantity from 15 to 35
        Order updatedQtyOrder = new Order("#23", "user3", "TCS", 35, 3530.0, OrderType.BUY, OrderStatus.OPEN, "10:02");
        exchange.editOrder(updatedQtyOrder);
        System.out.println("Edited #23 qty -> 35");

        // Verify by printing TCS buy book
        System.out.println("TCS buy orders after edit:");
        java.util.PriorityQueue<Order> buyAfterEdit = new java.util.PriorityQueue<>(exchange.getBuyOrders().get("TCS"));
        while (!buyAfterEdit.isEmpty()) {
            Order o = buyAfterEdit.poll();
            System.out.println(o.getOrderId() + " | " + o.getStockId() + " | " + o.getPrice() + " | qty: " + o.getQuantity());
        }

        // ===== Test: Edit Order (change price) =====
        System.out.println("\n===== Edit Order Test (change price) =====");

        Order editPriceOrder = new Order("#24", "user4", "INFY", 50, 1530.0, OrderType.SELL, OrderStatus.OPEN, "10:03");
        exchange.addOrder(editPriceOrder);
        System.out.println("Added order: " + editPriceOrder);

        // Edit: change price from 1530 to 1500
        Order updatedPriceOrder = new Order("#24", "user4", "INFY", 50, 1500.0, OrderType.SELL, OrderStatus.OPEN, "10:03");
        exchange.editOrder(updatedPriceOrder);
        System.out.println("Edited #24 price -> 1500.0");

        // Verify by printing INFY sell book
        System.out.println("INFY sell orders after edit:");
        java.util.PriorityQueue<Order> sellAfterEdit = new java.util.PriorityQueue<>(exchange.getSellOrders().get("INFY"));
        while (!sellAfterEdit.isEmpty()) {
            Order o = sellAfterEdit.poll();
            System.out.println(o.getOrderId() + " | " + o.getStockId() + " | " + o.getPrice() + " | qty: " + o.getQuantity());
        }

        // ===== Test: Past Trades for a User =====
        System.out.println("\n===== Past Trades for user1 =====");
        exchange.getPastTrades("user1");

        System.out.println("\n===== Past Trades for user3 =====");
        exchange.getPastTrades("user3");

        System.out.println("\n===== Past Trades for user6 (no trades expected) =====");
        exchange.getPastTrades("user6");
    }
}





// Design a stock brokage system like zerodha or groww

/*

1 - functional rquirement

    - Add/Register User in stock exchange
    - Add stock to the stock exchange
    - Each user should have stock holding list (stock, quantity, price) and balance
    - User able to add balance to their account
    - User able to buy/sell stock
    - User should be able to check their past successful trades

  Add-on
   - user should be able to cancel their order
   - user can edit their orders (change buy/sell quamtiry or price)


    Nouns
     - User
     - Stock
     - Order
     - Trade
     - StockExchange


 User class:
    - userId
    - name
    - email
    - phone
    - balance
    - stockHoldingList

Stock class:
    - stockId
    - name
    - price

Order class:
    - orderId
    - userId
    - stockId
    - quantity
    - price
    - type (buy/sell)
    - status (open/filled/cancelled)

Trade class:
    - tradeId
    - userId
    - stockId
    - quantity
    - price
    - type (buy/sell)
    - timestamp

stock exchange class:
    - exchangeId
    - name
    - stocks
    - orders
    - trades
    - users



methods:
    - addUser
    - addStock
    - addOrder
    - addTrade
    - cancelOrder
    - editOrder
    - getStockHoldingList
    - getPastTrades
    - orderMatching
*/


