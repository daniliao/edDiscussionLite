package application;

import java.util.List;
import java.util.ArrayList;

public class Answer {
    private String answerFromInput;
    private List<Answer> reply;

    public Answer(String answerFromInput) {
        this.answerFromInput = answerFromInput;
        this.setReply(new ArrayList<>());
    }

    public String getAnswerFromUser() {
        return answerFromInput;
    }

    public void setanswerFromInput(String answerFromInput) {
        this.answerFromInput = answerFromInput;
    }

    @Override
    public String toString() {
        return "Answer: " + answerFromInput;
    }

	public List<Answer> getReply() {
		return reply;
	}

	public void setReply(List<Answer> reply) {
		this.reply = reply;
	}
    
	public void createReply(Answer reply) {
		this.reply.add(reply);
	}
    
}