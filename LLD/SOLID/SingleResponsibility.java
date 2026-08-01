package SOLID;
// single responsibility 
// A class should have only one reason to change.


// this class violating the SRP. 
// this class is tightly coupled
// unnecessary testing of all function

class UserWithoutSRP {
    private String name;
    private String email;
    private String password;

    public void hashPassword() {
        System.out.print("hash password");
    }

    public void saveToDatabase() {
        System.out.print("save to database");
    }

    public void sendWelcomeEmail() {
        System.out.print("send welcome email");
    }

    public void log() {
        System.out.print("logging");
    }
    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}

//----------------------------------------------------------------------Single Responsibility Principle----------------------------------------------------//


class User {
    private String name;
    private String email;
    private String password;
    // getter 
    //setter
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}

class UserRepository {
    public void save(User user) {
        System.out.println("save to database " + user.getEmail() + " "+ user.getName());
    }
}

class LoggerService {
    public void log(String msg) {
        System.out.println(msg);
    }
}

class EmailService {
    public void send(String email) {
        System.out.println("send email to "+ email);
    }
}

class UserService {
    private UserRepository userRepository = new UserRepository();
    private EmailService emailService = new EmailService();
    private  LoggerService loggerService = new LoggerService();

    public void registerUser(String name, String email, String password) {

        String hashedPassword = Integer.toHexString(password.hashCode());

        User user = new User(name, email, hashedPassword);
        userRepository.save(user);
        emailService.send(user.getEmail());
        loggerService.log("New user register "+user.getName());
    }

}


//----------------------------------------------------------------------Main----------------------------------------------------//



public class SingleResponsibility {
    public static void main(String args[]) {
        UserService userService = new UserService();
        userService.registerUser("Md Tahir", "tahirmd@gmail.com", "Tahir@1234");
    }
}