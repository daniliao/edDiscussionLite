package application;

import java.util.ArrayList;
import java.util.List;

public class Answers {
    private List<Answer> answers = new ArrayList<>();

    public void createAnswer(Answer answer) {
        answers.add(answer);
    }

    public void deleteAnswer(Answer answer) {
        answers.remove(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return "Answers: " + answers;
    }
}