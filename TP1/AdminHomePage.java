package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import databasePart1.DatabaseHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminHomePage {

    // DatabaseHelper instance for fetching users
    private final DatabaseHelper databaseHelper;

    // TableView to display user info
    private TableView<User> userTable;

    public AdminHomePage() {
        this.databaseHelper = new DatabaseHelper();
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        // Label to display the welcome message for the admin
        Label adminLabel = new Label("Hello, Admin!");
        adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Button to view users
        Button viewUsersButton = new Button("View All Users");
        viewUsersButton.setOnAction(e -> {
            // Fetch user data and update the TableView
            userTable.setItems(fetchUserList());
        });

        // Setup the TableView
        userTable = new TableView<>();
        
       
    	//Username Column
		TableColumn<User, String> userNameCol = new TableColumn<>("Username");
		userNameCol.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
		//Password Column
		TableColumn<User, String> passCol = new TableColumn<>("Password");
		passCol.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
		//Role Column
		TableColumn<User, String> roleCol = new TableColumn<>("Role");
		roleCol.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        
        // Add columns to the TableView
        userTable.getColumns().addAll(userNameCol, passCol, roleCol);
        
        // Add the button and table to the layout
        layout.getChildren().addAll(adminLabel, viewUsersButton, userTable);
        
        // Scene for the admin page
        Scene adminScene = new Scene(layout, 800, 400);
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Page");
    }

    // Fetch user data from the database and return it as an ObservableList
    private ObservableList<User> fetchUserList() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            ResultSet rs = databaseHelper.getAllUsers();
            while (rs.next()) {
                // Populate the ObservableList with User objects
                users.add(new User(rs.getString("userName"), rs.getString("password"), rs.getString("role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
