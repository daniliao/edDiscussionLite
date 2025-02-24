package application.viewModel;

import application.model.Question;
import application.model.Answer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.*;

public class DiscussionPageViewModel {
    //Lists to store questions, which is observable for view updates
    private final ObservableList<Question> questionInList = FXCollections.observableArrayList();
    private final Map<Question, List<Answer>> questionAndAnswer = new HashMap<>();

    // Class ListView, Listview (Javafx 8). (2015). https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/ListView.html (accessed February 10, 2025).
    // Release: Javafx 2.2, Using JavaFX UI Controls: List View | JavaFX 2 Tutorials and Documentation. (2013). https://docs.oracle.com/javafx/2/ui_controls/list-view.htm (accessed February 10, 2025).
    public ObservableList<Question> getQuestionInList() {
        return questionInList;
    }

    public Map<Question, List<Answer>> getQuestionAndAnswer() {
        return questionAndAnswer;
    }

    // User story 1 and 5: Students to ask or update question
    public void createOrUpdateQuestion(String text, boolean newText, Question selected) {
        if (newText) {
            Question q = new Question(text);
            questionInList.add(q);
            questionAndAnswer.put(q, new ArrayList<>());
        } else if (selected != null) {
            selected.setQuestionFromUser(text);
            questionInList.set(questionInList.indexOf(selected), selected);
        }
    }

    // User story 2 and 5: Students to propose or update answer
    public void createOrUpdateAnswer(String text, boolean newText, Question selectedQuestion, Answer selectedAnswer) {
        if (text.isEmpty() || selectedQuestion == null) return;

        if (newText) {
            Answer a = new Answer(text);
            questionAndAnswer.get(selectedQuestion).add(a);
        } else if (selectedAnswer != null) {
            selectedAnswer.setanswerFromInput(text);
        }
    }

    public void deleteQuestion(Question selected) {
        if (selected != null) {
            questionInList.remove(selected);
            questionAndAnswer.remove(selected);
        }
    }

    public void deleteAnswer(Question selectedQuestion, Answer selectedAnswer) {
        if (selectedQuestion == null || selectedAnswer == null) return;

        // Check if the selected answer is a top-level answer
        List<Answer> answers = questionAndAnswer.get(selectedQuestion);
        if (answers != null && answers.contains(selectedAnswer)) {
            answers.remove(selectedAnswer);
            return;
        }

        // If not a top-level answer, search recursively in replies
        for (Answer answer : answers) {
            if (deleteNestedReply(answer, selectedAnswer)) {
                return;
            }
        }
    }

    private boolean deleteNestedReply(Answer parentAnswer, Answer replyToDelete) {
        if (parentAnswer.getReply().contains(replyToDelete)) {
            parentAnswer.getReply().remove(replyToDelete);
            return true;
        }
        // search in replies
        for (Answer reply : parentAnswer.getReply()) {
            if (deleteNestedReply(reply, replyToDelete)) {
                return true;
            }
        }

        return false;
    }

    // User story 6: Students that posted the question to announce that a specific answer addressed the issue that prompted the initial question.
    public void solvedQuestion(Question selected) {
        if (selected != null) {
            selected.markAsSolved();
            questionInList.set(questionInList.indexOf(selected), selected);
        }
    }

    // User story 4: Students to ask for or suggest clarifications.
    public void createOrUpdateReply(String text, boolean newText, Answer selectedAnswer, Answer selectedReply) {
        if (text.isEmpty() || selectedAnswer == null) return;

        if (newText) {
            Answer a = new Answer(text);
            selectedAnswer.createReply(a);
        } else if (selectedReply != null) {
            selectedReply.setanswerFromInput(text);
        }
    }

    // Class ListView, Listview (Javafx 8). (2015). https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/ListView.html (accessed February 10, 2025).
    // Release: Javafx 2.2, Using JavaFX UI Controls: List View | JavaFX 2 Tutorials and Documentation. (2013). https://docs.oracle.com/javafx/2/ui_controls/list-view.htm (accessed February 10, 2025).
 
    public FilteredList<Question> initiatedGlobalSearch(String searchQuery) {
        return questionInList.filtered(question ->
            question.getQuestionFromUser().toLowerCase().contains(searchQuery.toLowerCase()) || questionAndAnswer.get(question).stream().anyMatch(answer -> answer.getAnswerFromUser().toLowerCase().contains(searchQuery.toLowerCase()) || matchReply(answer, searchQuery))
        );
    }

    private boolean matchReply(Answer answer, String searchQuery) {
        return answer.getReply().stream().anyMatch(reply ->
            reply.getAnswerFromUser().toLowerCase().contains(searchQuery.toLowerCase()) || matchReply(reply, searchQuery)
        );
    }
}