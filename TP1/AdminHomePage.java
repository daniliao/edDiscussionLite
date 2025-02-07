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
        inviteUsersTab.setContent(createInvitePane());
        inviteUsersTab.setClosable(false);

        //Temp Password Logic 

        
        // Temp-Password Tab
        Tab createPasswordTab = new Tab("Temporary Password");
        createPasswordTab.setContent(createPasswordPane());
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

        /*deleteUserButton is an event handler that defines what happens when
        * deleteUserButton is clicked. It uses that 'e -> {...}' expression to
        * handle that event. Same concept applies to addUserButton
        */
        deleteUserButton.setOnAction(e -> {
            String selectedUser = userList.getSelectionModel().getSelectedItem();   //deletes the user that is currently selected
            if (selectedUser != null) {             //if a user is selected, it will go through the getItems
                userList.getItems().remove(selectedUser);   //.getItems returns a list, in this case the userlist, and selects the selectedUser
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



    private Pane createRolesPane() {
        VBox rolesPane = new VBox(10);
        rolesPane.setStyle("-fx-padding: 10;");


        //ADD FUNCTION HERE

        return rolesPane;
    }



    private Pane createInvitePane() {
        VBox invitePane = new VBox(10);
        invitePane.setStyle("-fx-padding: 10;");


        //ADD FUNCTION HERE

        return invitePane;
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

    // Then in createUserManagementPane:




    private Pane createPasswordPane() {
        VBox passwordPane = new VBox(10);
        passwordPane.setStyle("-fx-padding: 90;");


	     Label temp = new Label("");
	      temp.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
	    
	    Button genTempPass = new Button("Generate Temporary Password");
	    genTempPass.setOnAction(a -> {
	        SecureRandom random = new SecureRandom();
	        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/";
	        StringBuilder tempPass = new StringBuilder();
	        
	        tempPass.append("Bb0-"); // check all tests
	        
	        for (int i = 4; i < 8; i++) {
	            tempPass.append(chars.charAt(random.nextInt(chars.length())));
	        }
	        
	        temp.setText(tempPass.toString());

	        
	        	    });
	    passwordPane.getChildren().addAll(genTempPass,temp);
       

        return passwordPane;
    }

}