package SOLID;
/*
 * Any software entities(class, modules, function) should be open for extension but close for modification
 */

 /*
  * this is violating the open close principle because in future if need to add other payment method(GPay) then we need to modify the function.
  */

 enum PaymentType {
    PAYPAL,
    STRIPE,
    GPAY
 } 

class PaymentProcessor {
    public void processPayment(PaymentType paymentType, double amount) {
        if(paymentType == PaymentType.PAYPAL) {
            System.out.println("pay though paypal");
        } else if(paymentType == PaymentType.STRIPE){
            System.out.println("pay though stripe");
        } else {
            throw new Error("Invalid payment");
        }
    }
}

/*
 * to solve this we will create payment interface and extend the payment method as ter requirement.
 */

interface IPaymentProcess {
    public void pay(double amount);
 }

class PaypalPayment implements IPaymentProcess {
    public void pay(double amount) {
        System.out.println(amount + " pay though paypal");

    }
}

class StripePayment implements IPaymentProcess {
    public void pay(double amount) {
        System.out.println(amount + " pay though stripe");
    }
}

class GPayPayment implements IPaymentProcess {
    public void pay(double amount) {
        System.out.println(amount + " pay though GPay");

    }
}

class Payment {
    private IPaymentProcess paymentProcess;

    public Payment(IPaymentProcess paymentProcess){
        this.paymentProcess = paymentProcess;
    }

    public void setPaymentMethod(IPaymentProcess paymentProcess) {
        this.paymentProcess = paymentProcess;
    }

    public void pay(double amount) {
        paymentProcess.pay(amount);
    }
}

//----------------------------------------------------------------------Main----------------------------------------------------//


public class OpenClose {
    public static void main(String args[]){
        IPaymentProcess GPayPayment = new GPayPayment();
        IPaymentProcess paypalPayment = new PaypalPayment();
        IPaymentProcess stripePayment = new StripePayment();
        Payment payment = new Payment(GPayPayment);
        payment.pay(12);
        payment.setPaymentMethod(paypalPayment);
        payment.pay(15);
        payment.setPaymentMethod(stripePayment);
        payment.pay(15);

    }
}
