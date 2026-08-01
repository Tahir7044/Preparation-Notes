import java.util.ArrayList;
import java.util.List;

public class User{
    String name;
    String email;
    String phone;
    double balance;
    List<StockHolding> stockHoldingList;

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.balance = 0;
        this.stockHoldingList = new ArrayList<>();
    }

    public void addBalance(double amount) {
        this.balance += amount;
    }

    public void removeBalance(double amount) {
        this.balance -= amount;
    }

    public void addStockToHolding(StockHolding stockHolding) {
        for(StockHolding holding : this.stockHoldingList) {
            if(holding.getStockId().equals(stockHolding.getStockId())) {
                int totalQuantity  = holding.getQuantity() + stockHolding.getQuantity();
                double totalPrice = holding.getPrice() * holding.getQuantity() + stockHolding.getPrice() * stockHolding.getQuantity();
                holding.setQuantity(totalQuantity);
                holding.setPrice(totalPrice / totalQuantity);
                return;
            }
        }
        this.stockHoldingList.add(stockHolding);
    }
    
    public void removeStockToHolding(StockHolding stockHolding) {
        for(StockHolding holding : this.stockHoldingList) {
            if(holding.getStockId().equals(stockHolding.getStockId())) {
                holding.setQuantity(holding.getQuantity() - stockHolding.getQuantity());
                if(holding.getQuantity() < 0) {
                    throw new RuntimeException("Insufficient stock");
                }
                double totalPrice = holding.getPrice() * holding.getQuantity();
                holding.setPrice(totalPrice / holding.getQuantity());
                if(holding.getQuantity() == 0) {
                    this.stockHoldingList.remove(holding);
                }
                return;
            }
        }
    }
    
    public List<StockHolding> getStockHoldingList() {
        return this.stockHoldingList;
    }
    
    public double getBalance() {
        return this.balance;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getPhone() {
        return this.phone;
    }
}
