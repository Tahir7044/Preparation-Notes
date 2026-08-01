package LowLevelDesign.ATM;
public class Main{

    public static void main(String args[]){

    }

}



/*


// low level design of ATM

fuctional requirement:

 - user can insert the ATM card
 - user can enter the PIN amount and other avaiable option
 - user can withdraw the money
 - user can deposite the money
 - user can enquiry the balance

non function:

    - highly consistency (if user deposite/withdraw it must reflect the remaining balance correctly)
    - machine should contains different dinomination (100, 200, 500, 2000)
    - machine should have sufficient balance for withdrawals
    
    


entities:
 
 - ATM machine
 - denomination
 - User


 design patern will use - state design pattern


*/