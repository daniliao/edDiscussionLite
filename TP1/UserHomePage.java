package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class UserHomePage {

    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Hello, User!");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");


        // Create a new Log Out button
        Button logoutButton = new Button("Log Out");

        // Action for the Log Out button
        logoutButton.setOnAction(a -> {
            // Redirect to the login selection page
            new SetupLoginSelectionPage(new databasePart1.DatabaseHelper()).show(primaryStage);
        });
        
	    ChoiceBox<String> choice = new ChoiceBox<>();
	    choice.getItems().add("Reviewer");
	    choice.getItems().addAll("Student", "Teacher");

        // Add the new Label and Button to the layout
        layout.getChildren().addAll(userLabel, logoutButton,choice);
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("User Page");
    	
    }
}
