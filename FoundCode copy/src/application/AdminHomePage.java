package application;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import databasePart1.*;





/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class AdminHomePage {
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */

    private static final int TIME_EXPIRE = 20; // Code expires in 20 seconds
	
    

	public void show(DatabaseHelper database, Stage primaryStage) {
    	
    	// Main Layout
    	BorderPane mainLayout = new BorderPane();	
	    mainLayout.setStyle("-fx-padding: 20;");
	    
	    // Top pane for welcome message
        VBox topPane = new VBox();
        topPane.setStyle("-fx-alignment: center; -fx-padding: 10;");
        Label adminLabel = new Label("Hello, Admin!");
        adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        topPane.getChildren().add(adminLabel);
        mainLayout.setTop(topPane);

        /* Center pane for tabs (each tab represents a sub-pane with functionality)
        *  This is how our team can organize our individual parts of the project
        *  rather than trying to add all our parts into one UI
        */
        TabPane tabPane = new TabPane();


        /*Tabs are objects in which we can add to tabPane to organize different functions for
        * the user (in this case, admin). A new Tab object gets initialized with a name, and
        * must add a class to introduce extra functionality to it. User Management should also
        * not be closable, so as to not have the tab itself disappear.
        */

        // User Management Tab
        Tab userManagementTab = new Tab("User Management");
        userManagementTab.setContent(createUserManagementPane(database));
        userManagementTab.setClosable(false);


        // Roles Tab
        Tab rolesTab = new Tab("Edit Roles");
        rolesTab.setContent(createRolesPane());
        rolesTab.setClosable(false);

        // Inv-Users Tab
        Tab inviteUsersTab = new Tab("Invite Users");
        inviteUsersTab.setContent(createInvitePane(database));
        inviteUsersTab.setClosable(false);

        //Temp Password Logic 

        
        // Temp-Password Tab
        Tab createPasswordTab = new Tab("Temporary Password");
        createPasswordTab.setContent(createPasswordPane(database));
        createPasswordTab.setClosable(false);


        // Add tabs to the tab pane
        
        tabPane.getTabs().addAll(userManagementTab, rolesTab, inviteUsersTab, createPasswordTab);
        mainLayout.setCenter(tabPane);


		// Set the scene to primary stage
        //this creates new window to transition to, and the numbers just means the size of it
	    Scene adminScene = new Scene(mainLayout, 800, 400);
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Page");
	    primaryStage.show();
	    
    }


    //USERS TAB: Gives admin access to view established users, along with functions such as
    //seeing basic info (username, roles), adding new users and deleting users.
    private Pane createUserManagementPane(DatabaseHelper database) {
        VBox userManagementPane = new VBox(10);
        userManagementPane.setStyle("-fx-padding: 10;");



        //this is how buttons are made, and how you can add labels to those buttons
        Label userLabel = new Label("Users");
        Button addUserButton = new Button("Add User");
        Button deleteUserButton = new Button("Delete User");
        ListView<String> userList = new ListView<>();           //really nice way to list the users in a list of variable '<>' size
        userList.getItems().addAll("User 1", "User 2", "User 3");   //testing purposes: .getItems() returns a list and adds 3 users.
        ListView<String> userList1 = fetchUserList(database);


        // Add functionality to buttons
        addUserButton.setOnAction(e -> {
            userList.getItems().add("New User");    //this code should change to invite new user rather than add
        });
        
        //add delete user functionality
        

        /*deleteUserButton is an event handler that defines what happens when
        * deleteUserButton is clicked. It uses that 'e -> {...}' expression to
        * handle that event. Same concept applies to addUserButton
        */
        deleteUserButton.setOnAction(e -> {
            String selectedItem = userList1.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String username = selectedItem.substring( //creates a substring of the username from the userlist ListView
                    selectedItem.indexOf(":") + 2, 
                    selectedItem.indexOf("|") - 1  
                ).trim(); //removes the spaces from the ends of the string
                
                try {
                    database.deleteUser(username);
                    userList1.getItems().remove(selectedItem);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        /*userManagementPane finally implements the getChildren function, which adds multiple UI
        * elements (userLabel, addUserButton, deleteUserButton, userList) into the Vbox
        * getChildren() essentially accesses these elements, and AddAll() put them together in a 
        * vertical stack (that's why its called Vbox, it organizes stuff vertically)
        */ 
        userManagementPane.getChildren().addAll(userLabel, addUserButton, deleteUserButton, userList,userList1);
        return userManagementPane;
    }


    //CREATE ROLES TAB: allows admin to set and delete roles from established users
    private Pane createRolesPane() {
        VBox rolesPane = new VBox(10);
        rolesPane.setStyle("-fx-padding: 10;");


        //ADD FUNCTION HERE

        return rolesPane;
    }


    //INVITE USERS TAB: allows admin to invite new users
    private Pane createInvitePane(DatabaseHelper database) {
        
        VBox invitePane = new VBox(10);
        invitePane.setStyle("-fx-padding: 10;");

        
        // Label to display the title of the page
        Label userLabel = new Label("Invite ");
        //userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Button to generate the invitation code
        Button showCodeButton = new Button("Generate Invitation Code");
        
        // Label to display the generated invitation code
        Label inviteCodeLabel = new Label(""); ;
        inviteCodeLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
        
        // Label to display the timer
        Label myTimer = new Label("");
        myTimer.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        showCodeButton.setOnAction(a -> {
            // Generate the invitation code using the databaseHelper and set it to the label
            String invitationCode = database.generateInvitationCode();
            inviteCodeLabel.setText(invitationCode);

            // Start the countdown timer
            startCountdown(myTimer);
        });
        

        invitePane.getChildren().addAll(userLabel, showCodeButton, inviteCodeLabel, myTimer);


        return invitePane;
    }

	
	//COUNTDOWN OBJECT, creates a countdown in which lets admin know when a code expires
	private void startCountdown(Label myTimer) {
        // Set the initial time remaining
        int[] timeRemaining = {TIME_EXPIRE};

        // Create a Timeline to update the timer every second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining[0]--;

            if (timeRemaining[0] >= 0) {
                // Update the timer label
                myTimer.setText("Code expires in: " + timeRemaining[0] + " seconds");
            } 
            else {
                // Stop the timer and indicate the code has expired
                myTimer.setText("Code expired!");
                ((Timeline) event.getSource()).stop();
            }
        }));

        // Set the timeline to run indefinitely until stopped
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }



    
    private ListView<String> fetchUserList(DatabaseHelper database) {
        ListView<String> userList = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        
        try {
            ResultSet rs = database.getAllUsers();
            while (rs.next()) {
                String userName = rs.getString("userName");
                String role = rs.getString("role");
                items.add(String.format("Username: %s | Role: %s", userName, role));
            }
            userList.setItems(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return userList;
    }
	



    private Pane createPasswordPane(DatabaseHelper database) {
        VBox passwordPane = new VBox(10);
        passwordPane.setStyle("-fx-padding: 90;");

        TextField userField = new TextField();
        userField.setPromptText("Enter username");
        

        TextField temp = new TextField("");
        temp.setEditable(false);

        temp.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
        
        Button genTempPass = new Button("Generate Temp Password");
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/";
        genTempPass.setOnAction(a -> {
            String username = userField.getText();
            if (!username.isEmpty()) {
                SecureRandom random = new SecureRandom();
                StringBuilder tempPass = new StringBuilder("Bb0-");
                for (int i = 4; i < 8; i++) {
                    tempPass.append(chars.charAt(random.nextInt(chars.length())));
                }
                database.updatePassword(username, tempPass.toString());
				temp.setText("Temp password: " + tempPass.toString());
				
		        temp.setText(tempPass.toString());

            }
        });
        
        passwordPane.getChildren().addAll(userField, genTempPass, temp);
        return passwordPane;
    }

}