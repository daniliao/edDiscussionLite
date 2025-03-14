package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.model.Answer;
import application.model.Question;
import application.viewModel.DiscussionPageViewModel;
import javafx.scene.control.TextField;


import org.junit.Test;

public class DiscussionPageTests {

	@Test
	public void testCreateOrUpdateQuestion_create() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();
		
		vm.createOrUpdateQuestion("Life?", true, null);
		
	    assertFalse(vm.getQuestionInList().isEmpty());
		assertEquals("Life?", vm.getQuestionInList().get(0).getQuestionFromUser());
		
	}
	
	@Test
	public void testCreateOrUpdateQuestion_update() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();
		
		//create question first
		vm.createOrUpdateQuestion("old", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());
		
		//update, by getting question created
		vm.createOrUpdateQuestion("new", false, vm.getQuestionInList().get(0));
		
	    
		assertEquals("new", vm.getQuestionInList().get(0).getQuestionFromUser());
		
	}
	
//	public void createOrUpdateAnswer(String text, boolean newText, Question selectedQuestion, Answer selectedAnswer) {
//  if (text.isEmpty() || selectedQuestion == null) return;
//
//  if (newText) {
//      Answer a = new Answer(text);
//      questionAndAnswer.get(selectedQuestion).add(a);
//  } else if (selectedAnswer != null) {
//      selectedAnswer.setanswerFromInput(text);
//  }
//}
	
	@Test
	public void createOrUpdateAnswer_create() {
		
	}
	

	

}
