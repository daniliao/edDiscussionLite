package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.util.Arrays;
import java.util.List; // For mock roles
import databasePart1.*;

/**
 * The WelcomeLoginPage class displays a welcome screen for authenticated users.
 * It allows users to navigate to their respective pages based on their role or quit the application.
 */
public class WelcomeLoginPage {
    
    private final DatabaseHelper databaseHelper;

    public WelcomeLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user) {
        
        VBox layout = new VBox(20); // Increased spacing
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        Label welcomeLabel = new Label("Welcome!!");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Real multiple roles
        //List<String> roles = databaseHelper.getUserRoles(user.getUserName());
        
        // Fake multiple roles for testing
        List<String> roles = Arrays.asList("admin", "user", "reviewer", "instructor"); // Fake multiple roles
        
        // Create ComboBox and add roles
        ComboBox<String> roleSelector = new ComboBox<>();
        roleSelector.getItems().addAll(roles);

        // Set default value for ComboBox to the first role
        if (roles.size() == 1) {
            roleSelector.setValue(roles.get(0)); 
        }

        roleSelector.setPrefWidth(200); // Set preferred width for ComboBox

        // Button to navigate to the user's respective page based on their role
        Button continueButton = new Button("Continue to your Page");
        continueButton.setOnAction(a -> {
            String selectedRole = roleSelector.getValue();
            if (selectedRole == null) {
                return;
            }

            System.out.println("Selected Role: " + selectedRole);

            switch (selectedRole) {
                case "admin":
                	new AdminHomePage().show(databaseHelper, primaryStage);
                    break;
                case "user":
                    new UserHomePage().show(primaryStage);
                    break;
                case "student":
                    //new StudentHomePage().show(primaryStage);
                    break;
                case "instructor":
                    //new InstructorHomePage().show(primaryStage);
                    break;
                case "staff":
                    //new StaffHomePage().show(primaryStage);
                    break;
                case "reviewer":
                    //new ReviewerHomePage().show(primaryStage);
                    break;
                default:
                    System.out.println("Unknown Role");
                    break;
            }
        });
        
        // Button to quit the application
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(a -> {
            databaseHelper.closeConnection();
            Platform.exit(); // Exit the JavaFX application
        });
        
        // "Invite" button for admin to generate invitation codes
        if ("admin".equals(user.getRole())) {
            Button inviteButton = new Button("Invite");
            inviteButton.setOnAction(a -> {
                new InvitationPage().show(databaseHelper, primaryStage);
            });
            layout.getChildren().add(inviteButton);
        }

        // Add components to layout
        layout.getChildren().addAll(welcomeLabel, roleSelector, continueButton, quitButton);

        // Create scene and set it to primary stage
        Scene welcomeScene = new Scene(layout, 800, 400);
        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Welcome Page");
        primaryStage.show(); // Ensure the primaryStage is visible
    }
}