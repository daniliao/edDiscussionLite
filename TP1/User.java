package application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {
   private final StringProperty userName;
   private final StringProperty role;
   private final StringProperty password;

    // Constructor to initialize a new User object with userName, password, and role.
    public User( String userName, String password, String role) {
    	this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
        
    }
    
   public StringProperty userNameProperty() {
	   return userName;
   }

   public StringProperty roleProperty() {
       return role;
   }
   
   public StringProperty passwordProperty() {
	   return password;
   }

   // Getters for actual String values (optional, if needed)
   public String getUserName() {
       return userName.get();
   }


   public String getPassword() {
	   return password.get();
   }

   public String getRole() {
       return role.get();
   }
   
   public void setRole(String newRole) {
	   this.role.set(newRole);
   }
}
