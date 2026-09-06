public class Trade {
    String tradeId;
    String userId;
    String stockId;
    int quantity;
    double price;
    String buyOrderId;
    String sellOrderId;

    public Trade(TradeBuilder tradeBuilder) {
        this.tradeId = tradeBuilder.tradeId;
        this.userId = tradeBuilder.userId;
        this.stockId = tradeBuilder.stockId;
        this.quantity = tradeBuilder.quantity;
        this.price = tradeBuilder.price;
        this.buyOrderId = tradeBuilder.buyOrderId;
        this.sellOrderId = tradeBuilder.sellOrderId;
    }


    public String getTradeId() {
        return tradeId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getStockId() {
        return stockId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public double getPrice() {
        return price;
    }

    // format: <buy-order-id> <sell-price> <qty> <sell-order-id>
    @Override
    public String toString() {
        return buyOrderId + " " + price + " " + quantity + " " + sellOrderId;
    }
}


class TradeBuilder {
     String tradeId;
    String userId;
    String stockId;
    int quantity;
    double price;
    String buyOrderId;
    String sellOrderId;


    public TradeBuilder setBuyOrderId(String buyOrderId) {
        this.buyOrderId = buyOrderId;
        return this;
    }

    public TradeBuilder setSellOrderId(String sellOrderId) {
        this.sellOrderId = sellOrderId;
        return this;
    }

    public TradeBuilder setTradeId(String tradeId) {
        this.tradeId = tradeId;
        return this;
    }
    
    public TradeBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }
    
    public TradeBuilder setStockId(String stockId) {
        this.stockId = stockId;
        return this;
    }
    
    public TradeBuilder setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
    
    public TradeBuilder setPrice(double price) {
        this.price = price;
        return this;
    }
    
    public Trade build() {
        return new Trade(this);
    }
}
