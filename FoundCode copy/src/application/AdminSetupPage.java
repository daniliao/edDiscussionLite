package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import databasePart1.*;

/**
 * The SetupAdmin class handles the setup process for creating an administrator account.
 * This is intended to be used by the first user to initialize the system with admin credentials.
 */
public class AdminSetupPage {
	
    private final DatabaseHelper databaseHelper;
    
	private TextInputControl errorLabel;
    
    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
 


	public void show(Stage primaryStage) {
    	// Input fields for userName and password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        Button setupButton = new Button("Setup");

        setupButton.setOnAction(a -> {
            // Retrieve user input
            String userName = userNameField.getText();
            String password = passwordField.getText();
            
            try {
                // Check if the user already exists
                if (!databaseHelper.doesUserExist(userName)) {
                	boolean isFirstUser = databaseHelper.isDatabaseEmpty();
                    ArrayList<String> adminRole = new ArrayList<>(Collections.singletonList("admin"));
                    User user = new User(userName, password, adminRole); 
                    
                    // Register the new user in the database
                    databaseHelper.register(user);
                    System.out.println("Administrator setup completed.");
                    
                     
                    
                    if (isFirstUser) {
                        new UserLoginPage(databaseHelper).show(primaryStage);
                    } else {
                        // Navigate to the Welcome Login Page for other users
                        new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
                    }
                    
                } else {
                    errorLabel.setText("This userName is taken! Please use another to set up an account.");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });



        VBox layout = new VBox(10, userNameField, passwordField, setupButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}