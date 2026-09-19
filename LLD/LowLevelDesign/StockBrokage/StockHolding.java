public class StockHolding {
    String stockId;
    int quantity;
    double price;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public int getQuantity() {
        return this.quantity;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public double getPrice() {
        return this.price;
    }
    
    public void setStockId(String stockId) {
        this.stockId = stockId;
    }
    
    public String getStockId() {
        return this.stockId;
    }
}
