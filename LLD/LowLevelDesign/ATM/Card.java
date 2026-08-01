package LowLevelDesign.ATM;

public class Card {
    String name;
    String cardNumber;
    String cvv;
    String expiryDate;
    
    public Card(String name, String cardNumber, String cvv, String expiryDate) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public String getCvv() {
        return cvv;
    }
    
    public String getExpiryDate() {
        return expiryDate;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    
}
