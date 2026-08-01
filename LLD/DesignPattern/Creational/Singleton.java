package DesignPattern.Creational;

/*
 * 
 * it is a design which is used to create singleton objects. objects can be shared across the application.
 * like InMemoryCache, DBConnection, ConfigManager etc
 * ways to creating singleton objects
 * 1. Eager initialization
 * 2. Lazy initialization
 * 3. synchronized method
 * 4. double check locking
 * 5. Bill Pugh Singleton
 */

class DBConnection {
    // volatile -> volatile insure that the change should be visible to other threads
    // it does not cache value L1, L2 cache, instead its directly update the value in main memory
    private static volatile DBConnection dbConnection;
    private static String databaseName = "SQL";

    private DBConnection(String name) {
        System.out.println(name + " is connected successfully");
    }

    static synchronized DBConnection getDbConnectionSynchronizedMethod(String name) {
        if (dbConnection == null) {
            dbConnection = new DBConnection(name);
        }
        return dbConnection;
    }

    static DBConnection getDbConnectionSynchronizedBlock(String name) {
        if (dbConnection == null) {
            synchronized (DBConnection.class) {
                if (dbConnection == null) {
                    dbConnection = new DBConnection(name);
                }
            }
        }
        return dbConnection;
    }

    private static class DBConnectionHelper {
        private static final DBConnection INSTANCE = new DBConnection(databaseName);
    }

    static DBConnection getDbConnectionBillPugh(String name){
        return DBConnectionHelper.INSTANCE;
    }

}

public class Singleton {
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> DBConnection.getDbConnectionBillPugh("SQL"));
        Thread t2 = new Thread(() -> DBConnection.getDbConnectionBillPugh("NoSQL"));
        Thread t3 = new Thread(() -> DBConnection.getDbConnectionBillPugh("DynamoDB"));
        Thread t4 = new Thread(() -> DBConnection.getDbConnectionBillPugh("Postgres"));

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
    }
}
