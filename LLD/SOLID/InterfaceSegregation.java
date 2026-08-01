package SOLID;

/*
 * this principle state that your class should not to implement a function which it does not use it. 
 * fix -> break large interface ito smaller one
 */


 // here we can see that it is violating interface segregation because Dishwasher class does not need to implement cook, serve but it is forced to implement.

interface IRestaurantWorker {
    void cook();
    void serve();
    void washDish();
}

class DishWasher implements IRestaurantWorker {
    public void washDish(){
        System.out.println("I can wash dishes");
    }

    public void cook() {
        throw new Error("do not cook");
    }

    public void serve() {
        throw new Error("do not serve");
    }
}

class Chef implements IRestaurantWorker {
    public void washDish(){
        throw new Error("do not wash dishes");
    }

    public void cook() {
        System.out.println("I can cook");
    }

    public void serve() {
        throw new Error("do not serve");
    }
}

class Waiter implements IRestaurantWorker {
    public void washDish(){
        throw new Error("do not wash dishes");

    }

    public void cook() {
        throw new Error("do not cook");

    }

    public void serve() {
        System.out.println("I can serve");
    }
}


// ---------------------------------------------------------------------------- Solution ----------------------------------------


interface IDishWasherDuties {
    void washDish();
} 

interface IChefDuties {
    void cook();
} 

interface IWaiterDuties {
    void serve();
} 


class ISDishWasher implements IDishWasherDuties {
    public void washDish(){
        System.out.println("I can wash dishes");
    }
}

class ISChef implements IChefDuties {
    public void cook(){
        System.out.println("I can cook");
    }
}

class ISWaiter implements IWaiterDuties {
    public void serve( ){
        System.out.println("I can serve");
    }
}




public class InterfaceSegregation {
    public static void main(String[] args) {        
        IRestaurantWorker chef = new Chef();
        chef.cook();
        // it will throw the error
        // chef.serve();

        IChefDuties chefDuties = new ISChef();
        chefDuties.cook();  
        // it will give compile time error
        // chefDuties.serve();

        IWaiterDuties waiter = new ISWaiter();
        waiter.serve();  
        // it will give compile time error
        // waiter.cook();


    }
}
