public class Order {
    String OrderId;
    String UserId;
    String StockId;
    int Quantity;
    double Price;
    OrderType Type;
    OrderStatus Status;
    String Timestamp;

    public Order(String orderId, String userId, String stockId, int quantity, double price, OrderType type, OrderStatus status, String timestamp) {
        OrderId = orderId;
        UserId = userId;
        StockId = stockId;
        Quantity = quantity;
        Price = price;
        Type = type;
        Status = status;
        Timestamp = timestamp;                          // System.currentTimeMillis();
    }
    
    public OrderStatus getStatus() {
        return Status;
    }
    
    public String getOrderId() {
        return OrderId;
    }
    
    public String getUserId() {
        return UserId;
    }
    
    public String getStockId() {
        return StockId;
    }
    
    public int getQuantity() {
        return Quantity;
    }
    
    public double getPrice() {
        return Price;
    }
    
    public OrderType getType() {
        return Type;
    }
    
    public String getTimestamp() {
        return Timestamp;
    }
    
    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
    
    public void setUserId(String userId) {
        UserId = userId;
    }
    
    public void setStockId(String stockId) {
        StockId = stockId;
    }
    
    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
    
    public void setPrice(double price) {
        Price = price;
    }
    
    public void setType(OrderType type) {
        Type = type;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "OrderId='" + OrderId + '\'' +
                ", Timestamp=" + Timestamp + '\'' +
                ", StockId='" + StockId + '\'' +
                ", Quantity=" + Quantity +
                ", Price=" + Price +
                ", Type=" + Type +
                ", Status=" + Status +
                 ", UserId='" + UserId +
                '}';
    }
}
