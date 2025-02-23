package application;

public class Question {
    private String questionInput;
    private boolean solved;

    public Question(String questionInput) {
        this.questionInput = questionInput;
        this.solved = false;
    }

    public String getQuestionFromUser() {
        return questionInput;
    }

    public void setQuestionFromUser(String questionInput) {
        this.questionInput = questionInput;
    }

    @Override
    public String toString() {
    	return solved ? "Question (solved): " + questionInput: "Question: " + questionInput;
    }
    
    public boolean isSolved() {
        return solved;
    }

    public void markAsSolved() {
        this.solved = true;
    }
}