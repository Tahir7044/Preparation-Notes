package DesignPattern.Behavioral;

/*
 * 
 * it is a Behavioral design pattern.
 * it gets use as an medium between 3rd party API and our code base.
 * it overrides methods behavior as per requirements.
 * 
 */

interface PaymentStrategy {
    void pay(int amount);
}

class Paypal implements PaymentStrategy {

    public void pay(int amount) {
        System.out.println("pay through Paypal" + " " + amount);
    }
}

class Stripe implements PaymentStrategy {

    public void pay(int amount) {
        System.out.println("pay through Stripe" + " " + amount);
    }
}

class GPay implements PaymentStrategy {

    public void pay(int amount) {
        System.out.println("pay through PGay" + " " + amount);
    }
}

class PaymentProcess {
    private PaymentStrategy paymentStrategy;
    PaymentProcess(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    public void pay(int amount){
        this.paymentStrategy.pay(amount);
    }
}

public class Strategy {
    public static void main(String[] args) {
        PaymentProcess payPal = new PaymentProcess(new Paypal());
        PaymentProcess gpay = new PaymentProcess(new GPay());
        PaymentProcess stripe = new PaymentProcess(new Stripe());
        payPal.pay(22);
        gpay.pay(29);
        stripe.pay(13);
    }
}
