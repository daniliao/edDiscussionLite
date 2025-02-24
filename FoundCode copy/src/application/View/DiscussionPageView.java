package application.View;

import application.viewModel.DiscussionPageViewModel;
import application.model.Question;

import java.util.List;

import application.model.Answer;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.transformation.FilteredList;

public class DiscussionPageView {

    private final DiscussionPageViewModel viewModel = new DiscussionPageViewModel();
    private ListView<Question> questionInListView;
    private TreeView<Answer> answerTreeView;


    public void show(Stage primaryStage) {
        TextField questionTextBox = new TextField();
        // Leahy, P. (2019, July 3). JavaFX Textfield Overview. ThoughtCo. https://www.thoughtco.com/textfield-overview-2033936 
        questionTextBox.setPromptText("Your question...");

        TextField answerTextBox = new TextField();
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
        Button questionSolvedButton = new Button("Mark as Solved");

        Label errorLabelQuestion = new Label();
        errorLabelQuestion.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Label errorLabelAnswer = new Label();
        errorLabelAnswer.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        questionInListView = new ListView<>(viewModel.getQuestionInList());
        questionInListView.setPrefHeight(160);

        // Release: Javafx 2.2, Using JavaFX UI Controls: List View | JavaFX 2 Tutorials and Documentation. (2013). https://docs.oracle.com/javafx/2/ui_controls/list-view.htm (accessed February 10, 2025).
     
        answerTreeView = new TreeView<>();
        answerTreeView.setPrefHeight(160);

        addQuestionButton.setOnAction(e -> {
            if (questionTextBox.getText().isEmpty()) {
                errorLabelQuestion.setText("Question field is empty!");
            } else {
                viewModel.createOrUpdateQuestion(questionTextBox.getText(), true, null);
                errorLabelQuestion.setText("");
                questionTextBox.clear();
            }
        });

        updateQuestionButton.setOnAction(e -> {
            Question selected = questionInListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                viewModel.createOrUpdateQuestion(questionTextBox.getText(), false, selected);
                errorLabelQuestion.setText("");
            } else {
                errorLabelQuestion.setText("Please select a question!");
            }
        });

        deleteQuestionButton.setOnAction(e -> {
            Question selected = questionInListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                viewModel.deleteQuestion(selected);
                errorLabelQuestion.setText("");
                answerTreeView.setRoot(null);
            } else {
                errorLabelQuestion.setText("Please select a question to delete!");
            }
        });

        addAnswerButton.setOnAction(e -> {
            Question selectedQuestion = questionInListView.getSelectionModel().getSelectedItem();
            if (selectedQuestion == null || answerTextBox.getText().isEmpty()) {
                errorLabelAnswer.setText("Please select a question and type your answer!");
            } else {
                viewModel.createOrUpdateAnswer(answerTextBox.getText(), true, selectedQuestion, null);
                errorLabelAnswer.setText("");
                answerTextBox.clear();
                updateTreeView(selectedQuestion);
            }
        });

        updateAnswerButton.setOnAction(e -> {
            Answer selectedAnswer = getSelectedAnswer();
            Question selectedQuestion = questionInListView.getSelectionModel().getSelectedItem();
            if (selectedAnswer != null && !answerTextBox.getText().isEmpty()) {
                viewModel.createOrUpdateAnswer(answerTextBox.getText(), false, selectedQuestion, selectedAnswer);
                errorLabelAnswer.setText("");
                updateTreeView(selectedQuestion);
            } else {
                errorLabelAnswer.setText("Please select an answer and type your answer!");
            }
        });

        deleteAnswerButton.setOnAction(e -> {
            Answer selectedAnswer = getSelectedAnswer();
            Question selectedQuestion = questionInListView.getSelectionModel().getSelectedItem();
            if (selectedAnswer != null && selectedQuestion != null) {
                viewModel.deleteAnswer(selectedQuestion, selectedAnswer);
                errorLabelAnswer.setText("");
                updateTreeView(selectedQuestion);
            } else {
                errorLabelAnswer.setText("Please select an answer to delete!");
            }
        });

        addReplyButton.setOnAction(e -> {
            Answer selectedAnswer = getSelectedAnswer();
            if (selectedAnswer == null || answerTextBox.getText().isEmpty()) {
                errorLabelAnswer.setText("Please select an answer and type your reply!");
            } else {
                viewModel.createOrUpdateReply(answerTextBox.getText(), true, selectedAnswer, null);
                errorLabelAnswer.setText("");
                answerTextBox.clear();
                updateTreeView(questionInListView.getSelectionModel().getSelectedItem());
            }
        });

        questionSolvedButton.setOnAction(e -> {
            Question selected = questionInListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                viewModel.solvedQuestion(selected);
                errorLabelQuestion.setText("");
            } else {
                errorLabelQuestion.setText("Please select a question to mark as solved!");
            }
        });

        globalSearchTextBox.textProperty().addListener((obs, oldVal, newVal) -> {
            FilteredList<Question> filteredQuestions = viewModel.initiatedGlobalSearch(newVal);
            questionInListView.setItems(filteredQuestions);

            if (!filteredQuestions.isEmpty()) {
                Question firstMatchingQuestion = filteredQuestions.get(0);
                TreeItem<Answer> filteredTree = filterAnswer(firstMatchingQuestion, newVal);
                answerTreeView.setRoot(filteredTree);
                answerTreeView.setShowRoot(false);
            } else {
                answerTreeView.setRoot(null);
            }
        });

        // Update answer view when quesion is selected
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
        
        VBox questionBox = new VBox(new Label("Questions"), globalSearchTextBox, questionTextBox, errorLabelQuestion, addQuestionButton, updateQuestionButton, deleteQuestionButton, questionSolvedButton, questionInListView);
        VBox answerBox = new VBox(new Label("Answers"), answerTextBox, errorLabelAnswer, addAnswerButton, updateAnswerButton, deleteAnswerButton, addReplyButton, answerTreeView);
        splitPane.getItems().addAll(questionBox, answerBox);

        primaryStage.setScene(new Scene(splitPane, 1000, 700));
        primaryStage.setTitle("Ed Discussion Lite");
        primaryStage.show();
    }

    private Answer getSelectedAnswer() {
        TreeItem<Answer> selectedItem = answerTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            return selectedItem.getValue();
        } else {
            return null;
        }
    }

    private void updateTreeView(Question question) {
    	 // D, J. (1962, January 1). How to update a TreeView in javafx with data from users. Stack Overflow. https://stackoverflow.com/questions/42284060/how-to-update-a-treeview-in-javafx-with-data-from-users 
        TreeItem<Answer> root = new TreeItem<>();
        for (Answer answer : viewModel.getQuestionAndAnswer().get(question)) {
            TreeItem<Answer> answerItem = new TreeItem<>(answer);
            root.getChildren().add(answerItem);
            for (Answer reply : answer.getReply()) {
                TreeItem<Answer> replyItem = new TreeItem<>(reply);
                answerItem.getChildren().add(replyItem);
            }
        }
        //Class ListView, Listview (Javafx 8). (2015). https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/ListView.html (accessed February 10, 2025).
        answerTreeView.setRoot(root);
        answerTreeView.setShowRoot(false);
    }

    // Sshahine. (n.d.). JFoenix/demo/src/main/java/demos/components/treeviewdemo.java at master · SSHAHINE/JFOENIX. GitHub. https://github.com/sshahine/JFoenix/blob/master/demo/src/main/java/demos/components/TreeViewDemo.java 
    private TreeItem<Answer> filterAnswer(Question question, String searchQuery) {
        TreeItem<Answer> root = new TreeItem<>();
        List<Answer> answers = viewModel.getQuestionAndAnswer().getOrDefault(question, List.of());

        for (Answer answer : answers) {
            if (!answer.getAnswerFromUser().contains(searchQuery)) {
                continue; 
            }
            TreeItem<Answer> answerItem = new TreeItem<>(answer);
            root.getChildren().add(answerItem);

            for (Answer reply : answer.getReply()) {
                if (reply.getAnswerFromUser().contains(searchQuery)) {
                    answerItem.getChildren().add(new TreeItem<>(reply));
                }
            }
        }
        return root;
    }

}