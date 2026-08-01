package DesignPattern.Creational;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/*
 * Factory Design Pattern - Improved Implementation
 * 
 * Improvements made:
 * 1. Type safety with enums
 * 2. Factory registration mechanism
 * 3. Better error handling
 * 4. Extensibility through registration
 * 5. Reduced string-based comparisons
 */

// --- Enums for Type Safety ---
enum BurgerType {
    BASIC, STANDARD, PREMIUM
}

enum GarlicBreadType {
    BASIC, CHEESE
}

enum RestaurantType {
    KFC, BURGER_KING
}

// --- Product 1 --> Burger ---
interface Burger {
    void prepare();
}

class BasicBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing Basic Burger with bun, patty, and ketchup!");
    }
}

class StandardBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing Standard Burger with bun, patty, cheese, and lettuce!");
    }
}

class PremiumBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing Premium Burger with gourmet bun, premium patty, cheese, lettuce, and secret sauce!");
    }
}

class BasicWheatBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing Basic Wheat Burger with whole wheat bun, patty, and ketchup!");
    }
}

class StandardWheatBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing Standard Wheat Burger with whole wheat bun, patty, cheese, and lettuce!");
    }
}

class PremiumWheatBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing Premium Wheat Burger with gourmet wheat bun, premium patty, cheese, lettuce, and secret sauce!");
    }
}

// --- Product 2 --> GarlicBread ---
interface GarlicBread {
    void prepare();
}

class BasicGarlicBread implements GarlicBread {
    public void prepare() {
        System.out.println("Preparing Basic Garlic Bread with butter and garlic!");
    }
}

class CheeseGarlicBread implements GarlicBread {
    public void prepare() {
        System.out.println("Preparing Cheese Garlic Bread with extra cheese and butter!");
    }
}

class BasicWheatGarlicBread implements GarlicBread {
    public void prepare() {
        System.out.println("Preparing Basic Wheat Garlic Bread with butter and garlic!");
    }
}

class CheeseWheatGarlicBread implements GarlicBread {
    public void prepare() {
        System.out.println("Preparing Cheese Wheat Garlic Bread with extra cheese and butter!");
    }
}

// --- Abstract Factory ---
interface MealFactory {
    Burger createBurger(BurgerType type);
    GarlicBread createGarlicBread(GarlicBreadType type);
    String getRestaurantName();
}

// --- Concrete Factory 1 ---
class KFC implements MealFactory {
    private final Map<BurgerType, Supplier<Burger>> burgerMap;
    private final Map<GarlicBreadType, Supplier<GarlicBread>> garlicBreadMap;
    
    public KFC() {
        burgerMap = new HashMap<>();
        burgerMap.put(BurgerType.BASIC, BasicBurger::new);
        burgerMap.put(BurgerType.STANDARD, StandardBurger::new);
        burgerMap.put(BurgerType.PREMIUM, PremiumBurger::new);
        
        garlicBreadMap = new HashMap<>();
        garlicBreadMap.put(GarlicBreadType.BASIC, BasicGarlicBread::new);
        garlicBreadMap.put(GarlicBreadType.CHEESE, CheeseGarlicBread::new);
    }
    
    public Burger createBurger(BurgerType type) {
        Supplier<Burger> burgerSupplier = burgerMap.get(type);
        if (burgerSupplier != null) {
            return burgerSupplier.get();
        }
        throw new IllegalArgumentException("Unsupported burger type: " + type);
    }

    public GarlicBread createGarlicBread(GarlicBreadType type) {
        Supplier<GarlicBread> garlicBreadSupplier = garlicBreadMap.get(type);
        if (garlicBreadSupplier != null) {
            return garlicBreadSupplier.get();
        }
        throw new IllegalArgumentException("Unsupported garlic bread type: " + type);
    }
    
    public String getRestaurantName() {
        return "KFC - Kentucky Fried Chicken";
    }
}

// --- Concrete Factory 2 ---
class BurgerKing implements MealFactory {
    private final Map<BurgerType, Supplier<Burger>> burgerMap;
    private final Map<GarlicBreadType, Supplier<GarlicBread>> garlicBreadMap;
    
    public BurgerKing() {
        burgerMap = new HashMap<>();
        burgerMap.put(BurgerType.BASIC, BasicWheatBurger::new);
        burgerMap.put(BurgerType.STANDARD, StandardWheatBurger::new);
        burgerMap.put(BurgerType.PREMIUM, PremiumWheatBurger::new);
        
        garlicBreadMap = new HashMap<>();
        garlicBreadMap.put(GarlicBreadType.BASIC, BasicWheatGarlicBread::new);
        garlicBreadMap.put(GarlicBreadType.CHEESE, CheeseWheatGarlicBread::new);
    }
    
    public Burger createBurger(BurgerType type) {
        Supplier<Burger> burgerSupplier = burgerMap.get(type);
        if (burgerSupplier != null) {
            return burgerSupplier.get();
        }
        throw new IllegalArgumentException("Unsupported burger type: " + type);
    }

    public GarlicBread createGarlicBread(GarlicBreadType type) {
        Supplier<GarlicBread> garlicBreadSupplier = garlicBreadMap.get(type);
        if (garlicBreadSupplier != null) {
            return garlicBreadSupplier.get();
        }
        throw new IllegalArgumentException("Unsupported garlic bread type: " + type);
    }
    
    public String getRestaurantName() {
        return "Burger King - Have It Your Way";
    }
}

public class Factory {
    public static void main(String[] args) {

        MealFactory mealFactory = new KFC();

        Burger burger = mealFactory.createBurger(BurgerType.BASIC);
        GarlicBread garlicBread = mealFactory.createGarlicBread(GarlicBreadType.CHEESE);

        if (burger != null) burger.prepare();
        if (garlicBread != null) garlicBread.prepare();
    }
}
