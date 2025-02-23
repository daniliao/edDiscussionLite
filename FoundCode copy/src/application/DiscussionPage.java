package application;

import javafx.scene.Scene;
import javafx.collections.transformation.FilteredList;
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
        // Leahy, P. (2019, July 3). JavaFX Textfield Overview. ThoughtCo. https://www.thoughtco.com/textfield-overview-2033936 
        questionTextBox.setPromptText("Your question...");
        
        TextField answerTextBox = new TextField();
        // Release: Javafx 2.2, Using JavaFX UI Controls: List View | JavaFX 2 Tutorials and Documentation. (2013). https://docs.oracle.com/javafx/2/ui_controls/list-view.htm (accessed February 10, 2025).
        answerTreeView = new TreeView<>();
        answerTreeView.setPrefHeight(160);
        // Leahy, P. (2019, July 3). JavaFX Textfield Overview. ThoughtCo. https://www.thoughtco.com/textfield-overview-2033936 
        answerTextBox.setPromptText("Your answer...");
        
        TextField globalSearchTextBox = new TextField();
        globalSearchTextBox.setPromptText("Global search...");
        
        Button addQuestionButton = new Button("Add");
        Button updateQuestionButton = new Button("Update");
        Button deleteQuestionButton = new Button("Delete");
        Button addAnswerButton = new Button("Add");
        Button updateAnswerButton = new Button("Update");
        Button deleteAnswerButton = new Button("Delete");
        Button addReplyButton = new Button("Add Reply");
        Button quesionSolvedButton = new Button("Mark as Solved");
        
        
        // Label to display error messages for invalid input
        Label errorLabelQuestion = new Label();
        errorLabelQuestion.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        Label errorLabelAnswer = new Label();
        errorLabelAnswer.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        addQuestionButton.setOnAction(e -> {
        	if (questionTextBox.getText().isEmpty()) {
        		errorLabelQuestion.setText("Quesion field is empty!");
        	} else {
        		createOrUpdateQuestion(questionTextBox.getText(), true);
        		errorLabelQuestion.setText("");
        	}
        	
        });
        	
        updateQuestionButton.setOnAction(e -> {
        	Question selected = getQuestion();
        	if (selected != null) {
        		createOrUpdateQuestion(questionTextBox.getText(), false);
        		errorLabelQuestion.setText("");
        	} else {
        		errorLabelQuestion.setText("Please select a quesion!");
        	}
        	
        });
        
        deleteQuestionButton.setOnAction(e -> {
        	Question selected = getQuestion();
        	if (selected != null) {
        		deleteQuestion();
        		errorLabelQuestion.setText("");
        	} else {
        		errorLabelQuestion.setText("Please select an question to delete!");
        	}
        });
        
        addAnswerButton.setOnAction(e -> {
        	if (questionTextBox.getText().isEmpty()) {
        		errorLabelAnswer.setText("Please select a quesion and type your answer in the answer field!");
        	} else {
        		creatOrUpdateAnswer(answerTextBox.getText(), true);
        		errorLabelQuestion.setText("");
        		updateTreeView(getQuestion());
        	}
        	
        });
        
        updateAnswerButton.setOnAction(e -> {
        	Answer selected = getAnswer();
        	if (selected != null) {
        		creatOrUpdateAnswer(answerTextBox.getText(), false);
        		errorLabelAnswer.setText("");
        	} else {
        		errorLabelAnswer.setText("Please select an answer!");
        	}
        });
        
        deleteAnswerButton.setOnAction(e -> {
        	Answer selected = getAnswer();
        	if (selected != null) {
        		deleteAnswer();
        		errorLabelAnswer.setText("");
        	} else {
        		errorLabelAnswer.setText("Please select an answer to delete!");
        	}
        });
        
        quesionSolvedButton.setOnAction(e -> {
        	Question selected = getQuestion();
        	if (selected != null) {
        		solvedQuesion();
        		errorLabelQuestion.setText("");
        	} else {
        		errorLabelQuestion.setText("Please select an quesion to mark solved!");
        	}
        });
        
        
        addReplyButton.setOnAction(e -> {
        	if (answerTextBox.getText().isEmpty()) {
        		errorLabelAnswer.setText("Please type your reply in the answer field!");
        	} else {
        		createOrUpdateReply(answerTextBox.getText(), true);
        		errorLabelAnswer.setText("");
                updateTreeView(getQuestion());
        	}
        });
        
        globalSearchTextBox.textProperty().addListener((obs, oldVal, newVal) -> {
        	initiatedGlobalSearch(newVal);
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

        // Jenkov, J. (n.d.-a). JavaFX Splitpane. Jenkov.com Tech & Media Labs - Resources for Developers, IT Architects and Technopreneurs. https://jenkov.com/tutorials/javafx/splitpane.html 
        SplitPane splitPane = new SplitPane();

        VBox questionBox = new VBox(new Label("Questions"), globalSearchTextBox, questionTextBox, errorLabelQuestion, addQuestionButton, updateQuestionButton, deleteQuestionButton, quesionSolvedButton, questionInListView);
        VBox answerBox = new VBox(new Label("Answers"), answerTextBox, errorLabelAnswer, addAnswerButton, updateAnswerButton, deleteAnswerButton, addReplyButton, answerTreeView);

        // Jenkov, J. (n.d.-a). JavaFX Splitpane. Jenkov.com Tech & Media Labs - Resources for Developers, IT Architects and Technopreneurs. https://jenkov.com/tutorials/javafx/splitpane.html 
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
    
    // User story 1 and 5: Students to ask or update question
    private void createOrUpdateQuestion(String text, boolean newText) {
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

    // User story 2 and 5: Students to propose or update answer
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
    
    // User story 6: Students that posted the question to announce that a specific answer addressed the issue that prompted the initial question.
    private void solvedQuesion() {
    	Question selected = getQuestion();
    	if (selected != null) {
    		selected.markAsSolved();
    		questionInList.set(questionInList.indexOf(selected), selected); 
    	}
    }
    
    	
    // User story 4: Students to ask for or suggest clarifications. 
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
    
    // User story 3: Students to read proposed answers.
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
    
    private TreeItem<Answer> filterAnswer(Question question, String searchQuery) {
        List<Answer> answers = questionAndAnswer.get(question);
        TreeItem<Answer> root = new TreeItem<>();

        // Drew B, CourageCourage, & garawaa. (1962, April 1). How to search javafx TreeView for next treeitem?. Stack Overflow. https://stackoverflow.com/questions/44088649/how-to-search-javafx-treeview-for-next-treeitem 
        // HemantSHemantS, rlirli, & Matt Jennings (1959, August 1). How to implement filtering for treetableview. Stack Overflow. https://stackoverflow.com/questions/26072510/how-to-implement-filtering-for-treetableview 
        if (answers != null) {
            for (Answer answer : answers) {
                if (matchesSearchQuery(answer, searchQuery)) {
                    TreeItem<Answer> answerItem = new TreeItem<>(answer);
                    root.getChildren().add(answerItem);

                    for (Answer reply : answer.getReply()) {
                        if (matchesSearchQuery(reply, searchQuery)) {
                            TreeItem<Answer> replyItem = new TreeItem<>(reply);
                            answerItem.getChildren().add(replyItem);
                        }
                    }
                }
            }
        }
        return root;
    }

    private boolean matchesSearchQuery(Answer answer, String searchQuery) {
        return answer.getAnswerFromUser().toLowerCase().contains(searchQuery.toLowerCase());
    }
    
    private void initiatedGlobalSearch(String searchQuery) {
    	// HemantSHemantS, rlirli, & Matt Jennings (1959, August 1). How to implement filtering for treetableview. Stack Overflow. https://stackoverflow.com/questions/26072510/how-to-implement-filtering-for-treetableview 
    	
        // Filter questions based on the search query
        FilteredList<Question> filteredQuestions = questionInList.filtered(question ->
            question.getQuestionFromUser().toLowerCase().contains(searchQuery) ||
            questionAndAnswer.get(question).stream().anyMatch(answer ->
                answer.getAnswerFromUser().toLowerCase().contains(searchQuery) ||
                answer.getReply().stream().anyMatch(reply ->
                    reply.getAnswerFromUser().toLowerCase().contains(searchQuery)
                )
            )
        );
        questionInListView.setItems(filteredQuestions);

        // Update the TreeView for the first matching question
        if (!filteredQuestions.isEmpty()) {
            Question firstMatchingQuestion = filteredQuestions.get(0);
            TreeItem<Answer> filteredTree = filterAnswer(firstMatchingQuestion, searchQuery);
            answerTreeView.setRoot(filteredTree);
            answerTreeView.setShowRoot(false);
        } else {
            answerTreeView.setRoot(null);
        }
    }
    


    
}
