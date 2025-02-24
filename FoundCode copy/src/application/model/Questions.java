package application.model;

import java.util.List;
import java.util.ArrayList;


public class Questions {
    private List<Question> questions = new ArrayList<>();

    public void createQuestion(Question question) {
        questions.add(question);
    }

    public void deleteQuestion(Question question) {
        questions.remove(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return "Questions: " + questions;
    }
}