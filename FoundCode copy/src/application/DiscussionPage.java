package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class DiscussionPage {
	//Lists to store questions, which is observable for view updates
    private final ObservableList<Question> questionInList = FXCollections.observableArrayList();
    private final Map<Question, List<Answer>> questionAndAnswer = new HashMap<>();
    private ListView<Question> questionInListView;
    private TreeView<Answer> answerTreeView;

    public void show(Stage primaryStage) {
        TextField questionTextBox = new TextField();
        // Class ListView, Listview (Javafx 8). (2015). https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/ListView.html (accessed February 10, 2025).
        // Release: Javafx 2.2, Using JavaFX UI Controls: List View | JavaFX 2 Tutorials and Documentation. (2013). https://docs.oracle.com/javafx/2/ui_controls/list-view.htm (accessed February 10, 2025).
        questionInListView = new ListView<>(questionInList); 
        questionInListView.setPrefHeight(160);
        
        TextField answerTextBox = new TextField();
        // Release: Javafx 2.2, Using JavaFX UI Controls: List View | JavaFX 2 Tutorials and Documentation. (2013). https://docs.oracle.com/javafx/2/ui_controls/list-view.htm (accessed February 10, 2025).
        answerTreeView = new TreeView<>();
        answerTreeView.setPrefHeight(160);

        
        Button addQuestionButton = new Button("Add");
        Button updateQuestionButton = new Button("Update");
        Button deleteQuestionButton = new Button("Delete");
        Button addAnswerButton = new Button("Add");
        Button updateAnswerButton = new Button("Update");
        Button deleteAnswerButton = new Button("Delete");
        Button addReplyButton = new Button("Add Reply");
        Button quesionSolvedButton = new Button("Mark as Solved");
        
        // Label to display error messages for invalid input
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        addQuestionButton.setOnAction(e -> {
        	createOrUpdateQuestion(questionTextBox.getText(), true);
        	errorLabel.setText("Quesion field is empty!");
        });
        	
        updateQuestionButton.setOnAction(e -> {
        	createOrUpdateQuestion(questionTextBox.getText(), false);
        	errorLabel.setText("Please select a quesion!");
        });
        
        deleteQuestionButton.setOnAction(e -> {
        	deleteQuestion();
        	errorLabel.setText("Please select an question to delete!");
        });
        
        addAnswerButton.setOnAction(e -> {
        	creatOrUpdateAnswer(answerTextBox.getText(), true);
        	updateTreeView(getQuestion());
        	errorLabel.setText("Please select a quesion and type your answer in the answer field!");
        });
        
        updateAnswerButton.setOnAction(e -> {
        	creatOrUpdateAnswer(answerTextBox.getText(), false);
        	errorLabel.setText("Please select an answer!");
        });
        
        deleteAnswerButton.setOnAction(e -> {
        	deleteAnswer();
        	errorLabel.setText("Please select an answer to delete!");
        });
        
        quesionSolvedButton.setOnAction(e -> {
        	solvedQuesion();
        	errorLabel.setText("Please select an quesion to mark solved!");
        });
        
        addReplyButton.setOnAction(e -> {
            createOrUpdateReply(answerTextBox.getText(), true);
            updateTreeView(getQuestion());
            errorLabel.setText("Please select an answer to reply to!");
        });

        questionInListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                questionTextBox.setText(newVal.getQuestionFromUser());
                updateTreeView(newVal);
            } else {
                questionTextBox.setText("");
                answerTreeView.setRoot(null);
            }
        });

        // https://jenkov.com/tutorials/javafx/splitpane.html
        SplitPane splitPane = new SplitPane();

        VBox questionBox = new VBox(new Label("Questions"), questionTextBox, errorLabel, addQuestionButton, updateQuestionButton, deleteQuestionButton, quesionSolvedButton, questionInListView);
        VBox answerBox = new VBox(new Label("Answers"), answerTextBox, errorLabel, addAnswerButton, updateAnswerButton, deleteAnswerButton, addReplyButton, answerTreeView);

        splitPane.getItems().addAll(questionBox, answerBox);

        
        primaryStage.setScene(new Scene(splitPane, 1000, 700));
        primaryStage.setTitle("Ed Discussion Lite");
        primaryStage.show();
    }
    
    // Class ListView, Listview (Javafx 8). (2015). https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/ListView.html (accessed February 10, 2025).
    // Release: Javafx 2.2, Using JavaFX UI Controls: List View | JavaFX 2 Tutorials and Documentation. (2013). https://docs.oracle.com/javafx/2/ui_controls/list-view.htm (accessed February 10, 2025).
    private Question getQuestion() {
        return questionInListView.getSelectionModel().getSelectedItem();  
    }

    private Answer getAnswer() {
        TreeItem<Answer> selectedItem = answerTreeView.getSelectionModel().getSelectedItem();
        return selectedItem != null ? selectedItem.getValue() : null;
    }
    
    
    private void createOrUpdateQuestion(String text, boolean newText) {
        if (text.isEmpty()) {
        	return;
        }

        if (newText) {
            Question q = new Question(text);
            questionInList.add(q);
            questionAndAnswer.put(q, new ArrayList<>());
        } else {
            Question selected = getQuestion();
            if (selected != null) {
                selected.setQuestionFromUser(text); 
                questionInList.set(questionInList.indexOf(selected), selected); 
            }
        }
    }

    private void creatOrUpdateAnswer(String text, boolean newText) {
        if (text.isEmpty()) {
        	return;
        }

        Question selectedQuestion = getQuestion();
        if (selectedQuestion != null) {
            if (newText) {
                Answer a = new Answer(text);
                questionAndAnswer.get(selectedQuestion).add(a);
            } else {
                Answer selectedAnswer = getAnswer();
                if (selectedAnswer != null) {
                    selectedAnswer.setanswerFromInput(text); 
                }
            }
            updateTreeView(selectedQuestion);
        }
    }


    private void deleteQuestion() {
        Question selected = getQuestion();
        if (selected != null) {
            questionInList.remove(selected);
            questionAndAnswer.remove(selected);
            answerTreeView.setRoot(null);
        }
    }
    

    private void deleteAnswer() {
        TreeItem<Answer> selectedItem = answerTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        Answer selectedAnswer = selectedItem.getValue();
        Question selectedQuestion = getQuestion();

        if (selectedQuestion != null && selectedAnswer != null) {
            TreeItem<Answer> parentItem = selectedItem.getParent();
            
            // D, J. (1962, January 1). How to update a TreeView in javafx with data from users. Stack Overflow. https://stackoverflow.com/questions/42284060/how-to-update-a-treeview-in-javafx-with-data-from-users 
            // If selected item is a reply
            if (parentItem != null && parentItem != answerTreeView.getRoot()) {
                Answer parentAnswer = parentItem.getValue();
                parentAnswer.getReply().remove(selectedAnswer);
            } else {
            	// else selected item is an answer
                questionAndAnswer.get(selectedQuestion).remove(selectedAnswer);
            }
            updateTreeView(selectedQuestion);
        }
    }
    
    private void solvedQuesion() {
    	Question selected = getQuestion();
    	if (selected != null) {
    		selected.markAsSolved();
    		questionInList.set(questionInList.indexOf(selected), selected); 
    	}
    }
    
    

    private void createOrUpdateReply(String text, boolean newText) {
    	if (text.isEmpty()) {
        	return;
        }
    	Question selectedQuestion = getQuestion();
    	Answer selectedAnswer = getAnswer();
    	
        if (selectedAnswer != null) {
        	if (newText) {
        		Answer a = new Answer(text);
        		selectedAnswer.createReply(a);
        	} else {
        		Answer selectedReply = getAnswer();
        		if (selectedReply != null) {
        			selectedReply.setanswerFromInput(text);
        		}
            
        	}
        	updateTreeView(selectedQuestion);
        }
    }
    
    private void updateTreeView(Question question) {
        TreeItem<Answer> root = new TreeItem<>();
        List<Answer> answers = questionAndAnswer.get(question);

        if (answers != null) {
            for (Answer answer : answers) {
            	//Release: Javafx 2.2. Using JavaFX UI Controls: Tree View | JavaFX 2 Tutorials and Documentation. (2013, August 27). https://docs.oracle.com/javafx/2/ui_controls/tree-view.htm 
                TreeItem<Answer> answerItem = new TreeItem<>(answer);
                root.getChildren().add(answerItem);
                // Add reply to answer
                for (Answer reply : answer.getReply()) {
                	//Release: Javafx 2.2. Using JavaFX UI Controls: Tree View | JavaFX 2 Tutorials and Documentation. (2013, August 27). https://docs.oracle.com/javafx/2/ui_controls/tree-view.htm 
                    TreeItem<Answer> replyItem = new TreeItem<>(reply);
                    answerItem.getChildren().add(replyItem);
                }
            }
        }
        //Class ListView, Listview (Javafx 8). (2015). https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/ListView.html (accessed February 10, 2025).
        answerTreeView.setRoot(root);
        answerTreeView.setShowRoot(false); 
    }
    
}
