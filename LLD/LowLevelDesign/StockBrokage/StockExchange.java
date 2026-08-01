import java.util.*;

public class StockExchange {
    
    String name;
    List<User> users;
    List<Stock> stocks;
    Map<String, PriorityQueue<Order>> buyOrders;
    Map<String, PriorityQueue<Order>> sellOrders;
    List<Trade> trades;

    public StockExchange(String name) {
        this.name = name;
        users = new ArrayList<>();
        stocks = new ArrayList<>();
        buyOrders = new HashMap<>();
        sellOrders = new HashMap<>();
        trades = new ArrayList<>();
    }

    public void addOrder(Order order) {
        String stockId = order.getStockId();
        if (order.getType() == OrderType.BUY) {
            buyOrders.computeIfAbsent(stockId, k -> createBuyQueue()).add(order);
        } else {
            sellOrders.computeIfAbsent(stockId, k -> createSellQueue()).add(order);
        }
        matchOrders(stockId);
    }

    public void removeOrder(Order order) {
        String stockId = order.getStockId();
        if (order.getType() == OrderType.BUY) {
            PriorityQueue<Order> pq = buyOrders.get(stockId);
            if (pq != null) pq.remove(order);
        } else {
            PriorityQueue<Order> pq = sellOrders.get(stockId);
            if (pq != null) pq.remove(order);
        }
    }

    public void getPastTrades(String userId) {
        for(Trade trade : trades) {
            if(trade.getUserId().equals(userId) || trade.getUserId().equals(userId)) {
                System.out.println(trade);
            }
        }
    }



    public void updateOrder(Order order) {
        String stockId = order.getStockId();
        Map<String, PriorityQueue<Order>> orderMap = order.getType() == OrderType.BUY ? buyOrders : sellOrders;
        PriorityQueue<Order> pq = orderMap.get(stockId);
        if (pq == null) return;
        
        for(Order o : pq) {
            if(o.getOrderId().equals(order.getOrderId())) {
                o.setPrice(order.getPrice());
                o.setQuantity(order.getQuantity());
                o.setType(order.getType());
                break;
            }
        }
    }

    public void editOrder(Order order) {
        updateOrder(order);
    }

    public void cancelOrder(Order order) {
        removeOrder(order);
    }

    private void matchOrders(String stockId) {
        PriorityQueue<Order> buys = buyOrders.get(stockId);
        PriorityQueue<Order> sells = sellOrders.get(stockId);
        if (buys == null || sells == null) return;

        while (buys.size() > 0 && sells.size() > 0) {
            Order buyOrder = buys.peek();
            Order sellOrder = sells.peek();
            
            if (buyOrder.getPrice() >= sellOrder.getPrice()) {
                buys.poll();
                sells.poll();

                int quantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
                double price = sellOrder.getPrice();
                
                Trade trade = new TradeBuilder()
                    .setTradeId(UUID.randomUUID().toString())
                    .setUserId(buyOrder.getUserId())
                    .setStockId(stockId)
                    .setQuantity(quantity)
                    .setPrice(price)
                    .setBuyOrderId(buyOrder.getOrderId())
                    .setSellOrderId(sellOrder.getOrderId())
                    .build();
                trades.add(trade);
                
                buyOrder.setQuantity(buyOrder.getQuantity() - quantity);
                sellOrder.setQuantity(sellOrder.getQuantity() - quantity);
                
                if (buyOrder.getQuantity() > 0) buys.add(buyOrder);
                if (sellOrder.getQuantity() > 0) sells.add(sellOrder);
            } else {
                break;
            }
        }
    }

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public void removeStock(Stock stock) {
        stocks.remove(stock);
    }

    public void addUser(User user) {
        users.add(user);
    }
    
    public void removeUser(User user) {
        users.remove(user);
    }
    
    public String getName() {
        return name;
    }

    public Map<String, PriorityQueue<Order>> getBuyOrders() {
        return buyOrders;
    }

    public Map<String, PriorityQueue<Order>> getSellOrders() {
        return sellOrders;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    // Buy queue: highest price first, then earliest timestamp (FIFO)
    private static PriorityQueue<Order> createBuyQueue() {
        return new PriorityQueue<>((a, b) -> {
            if (a.getPrice() != b.getPrice()) return Double.compare(b.getPrice(), a.getPrice());
            return a.getTimestamp().compareTo(b.getTimestamp());
        });
    }

    // Sell queue: lowest price first, then earliest timestamp (FIFO)
    private static PriorityQueue<Order> createSellQueue() {
        return new PriorityQueue<>((a, b) -> {
            if (a.getPrice() != b.getPrice()) return Double.compare(a.getPrice(), b.getPrice());
            return a.getTimestamp().compareTo(b.getTimestamp());
        });
    }
}
