public class Stock {
    private String stockId;
    private String name;
    private double price;
    
    public Stock(String stockId, String name, double price) {
        this.stockId = stockId;
        this.name = name;
        this.price = price;
    }
    
    public String getStockId() {
        return stockId;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
}