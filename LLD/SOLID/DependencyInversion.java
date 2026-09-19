package SOLID;

/*
 * 
 * High level module should not depend on low level module instead they both depends on abstractions like (interface) for decoupling.
 * 
 * client           ->   controller     ->   Service        ->  SQL Repository  -> SQL DB
 * (Http request)       (/api/user/id)     (repo.get(id))        (get())   
 * 
 *                                                             NoSQL Repository -> NoSQL DB
 *                                         (repo.find(id))       (find())
 */       

class SQLRepository {
    public void get() {
        System.out.println("get data from SQL");
    }
}

class NoSQLRepository {
    public void find() {
        System.out.println("get data from NoSQL");
    }
}

// here you can see that if we need NoQSL then we have to make change in UserServe.
// it means userService is depending on SQL/NoSQL repository.

class UserService {
    SQLRepository sqlRepository = new SQLRepository();

    public void get() {
        sqlRepository.get();
    }
}

// ------------------------------------------------------------------------ Solution ----------------------------------------------------

 interface IRepository {
    void get();
 }

 class SQLRepositoryDI implements IRepository {
    public void get() {
        System.out.println("get data from SQL");
    }
 }

 class NoSQLRepositoryDI implements IRepository {
    public void get() {
        System.out.println("get data from NoSQL");
    }
 }

 class UserServiceDI {
    private IRepository repository;
    
    public UserServiceDI(IRepository repository) {
        this.repository = repository;
    }
    
    public void get() {
        repository.get();
    }
}

public class DependencyInversion {
    public static void main(String[] args) {
        UserServiceDI user = new UserServiceDI(new NoSQLRepositoryDI());
        user.get();
    }
}
