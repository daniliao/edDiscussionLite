package test;

import static org.junit.Assert.*;

import application.model.Answer;
import application.model.Question;
import application.viewModel.DiscussionPageViewModel;
import javafx.collections.transformation.FilteredList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



import org.junit.Test;


/* JavaDoc in next section!
 * Useful doc for my team:
 * 
 * Get Answer & Question objects:
 * ------------------------------------
 * 
 * Get Question: vm.getQuestionInList().get(0);
 * 
 * Get Answer: vm.getQuestionAndAnswer().get(question).get(0);
 * 
 * Get Reply: vm.getQuestionAndAnswer().get(question).get(0).getReply().get(0);
 * 
 * ------------------------------------
 * ------------------------------------
 * 
 * Get Answer & Question in String:
 * 
 * Get question: vm.getQuestionInList().get(0).getQuestionFromUser();
 * 
 * Get answer: vm.getQuestionAndAnswer().get(question).get(0).getAnswerFromUser();
 * 
 * Get reply: vm.getQuestionAndAnswer().get(question).get(0).getReply().get(0).getAnswerFromUser();
 * 
 * 
 * Code walkthrough for get answer in String: 
 * 			   vm.getQuestionAndAnswer()         .get(question)    .get(0)          .getAnswerFromUser();
		       In the getQuestionAndAnswer map,  first question,   first answer,    retrieve answer
   
   To check FilteredList<Question> aList, you need to use and add question into this first: ObservableList<Question> questions = FXCollections.observableArrayList();
		       
		       
 */


/**
 * <p> Title: DiscussionPageTests </p>
 * 
 * <p> Description: Test class for verifying the behavior of the methods in the 
 * {@link DiscussionPageViewModel} class.
 * </p>
 * 
 * <p> Copyright: Daniel Liao © 2025 </p>
	 * 
	 * @author Daniel Liao
	 * 
	 * @version 0.00		2025-03-14	Initial baseline 
 */

public class DiscussionPageTests {

	/**********
	 * This method tests CreateOrUpdateQuestion in viewModel by creating a new question 
	 * in the discussion page view model.
     * 
     * It verifies that a question is added to the list by checking its content
     * is as expected.
     * 
	 */
	@Test
	public void testCreateOrUpdateQuestion_create() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		vm.createOrUpdateQuestion("Life?", true, null);

		assertFalse(vm.getQuestionInList().isEmpty());
		assertEquals("Life?", vm.getQuestionInList().get(0).getQuestionFromUser());

	}

	/**********
	 * This method tests the update functionality of an existing question.
     * 
     * It verifies that a question can be updated and that the list storing it is 
     * affected by the changes.
     * 
	 */
	@Test
	public void testCreateOrUpdateQuestion_update() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//create a question first
		vm.createOrUpdateQuestion("old", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		//update, by getting question created
		vm.createOrUpdateQuestion("new", false, vm.getQuestionInList().get(0));


		assertEquals("new", vm.getQuestionInList().get(0).getQuestionFromUser());

	}
	
	/**********
	 * This method tests condition when a question is not selected for update.
     * 
     * It verifies that the existing question remains unchanged when no
     * question is selected for update.
     * 
	 */
	@Test
	public void testCreateOrUpdateQuestion_null() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//create a question first
		vm.createOrUpdateQuestion("old", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		//did not select question to update
		vm.createOrUpdateQuestion("new", false, null);


		assertEquals("old", vm.getQuestionInList().get(0).getQuestionFromUser());

	}
	/**********
	 * This method tests the creation of an answer
     * 
     * It verifies that an answer is correctly liked with a question.
     * 
	 */
	@Test
	public void testCreateOrUpdateAnswer_create() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//create question first
		vm.createOrUpdateQuestion("question", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		Question question = vm.getQuestionInList().get(0);
		vm.createOrUpdateAnswer("is life", true, question, null);

		assertEquals("is life", vm.getQuestionAndAnswer().get(question).get(0).getAnswerFromUser());
	}

	/**********
	 * This method test the update functionality of an existing answer.
     * 
     * It verifies that an answer can be updated and the updated answer
     * is correctly changed in the list.
     * 
	 */
	@Test
	public void testCreateOrUpdateAnswer_update() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//create question first
		vm.createOrUpdateQuestion("question", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		// get the first question
		Question question = vm.getQuestionInList().get(0);
		vm.createOrUpdateAnswer("is life", true, question, null);
		
		vm.createOrUpdateAnswer("updated life", false, question, vm.getQuestionAndAnswer().get(question).get(0));

		// Code Translation:
		// 	vm.getQuestionAndAnswer()		 .get(question)	 .get(0)		  .getAnswerFromUser()
		// 	In the getQuestionAndAnswer map, first question, first answer,    retrieve answer
		assertEquals("updated life", vm.getQuestionAndAnswer().get(question).get(0).getAnswerFromUser());
	}
	
	/**********
	 * This method test the condition when an answer is not selected for update.
     * 
     * It verifies that the answer is unchanged when no answer is selected.
     * 
	 */
	@Test
	public void testCreateOrUpdateAnswer_null() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//create question first
		vm.createOrUpdateQuestion("question", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		// get the first question
		Question question = vm.getQuestionInList().get(0);
		vm.createOrUpdateAnswer("is life", true, question, null);
		
		vm.createOrUpdateAnswer("updated life", false, question, null);

		// Code Translation:
		// 	vm.getQuestionAndAnswer()		 .get(question)	 .get(0)		  .getAnswerFromUser()
		// 	In the getQuestionAndAnswer map, first question, first answer,    retrieve answer
		assertEquals("is life", vm.getQuestionAndAnswer().get(question).get(0).getAnswerFromUser());
	}

	/**********
	 * This method test the deletion of a question.
     * 
     * It verifies that a question is removed from the list after delete method is called.
     * 
	 */
	@Test
	public void testDeleteQuestion() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//create question first
		vm.createOrUpdateQuestion("neww", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		// get the first question
		Question question = vm.getQuestionInList().get(0);
		vm.deleteQuestion(question);

		assertFalse(vm.getQuestionInList().contains(question));
	}
	
	/**********
	 * This method test the deletion of a top-level answer.
     * 
     * It verifies that an answer is removed from the list after delete method is called.
     * 
	 */
	@Test
	public void testDeleteAnswer_topLevelAnswer() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//	create question first
		vm.createOrUpdateQuestion("question", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		Question question = vm.getQuestionInList().get(0);

		//	create answer first
		vm.createOrUpdateAnswer("is life", true, question, null);

		//get answer
		Answer ans = vm.getQuestionAndAnswer().get(question).get(0);

		vm.deleteAnswer(question, ans);

		assertFalse(vm.getQuestionAndAnswer().get(question).contains(ans));
	}

	/**********
	 * This method tests deleting a reply
     * 
	 */
	@Test
	public void testDeleteAnswer_replies() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//	create question first
		vm.createOrUpdateQuestion("question", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		Question question = vm.getQuestionInList().get(0);

		//	create answer first
		vm.createOrUpdateAnswer("is life", true, question, null);

		//get answer
		Answer ans = vm.getQuestionAndAnswer().get(question).get(0);

		//create reply
		vm.createOrUpdateReply("reeeeply", true, ans, null);

		Answer replllly = ans.getReply().get(0);

		//delete reply
		vm.deleteAnswer(question, replllly);

		assertFalse(ans.getReply().contains(replllly));
	}

	/**********
	 * This method tests deleting a nested reply from an answer
     * 
	 */
	@Test
	public void testDeleteNestedReply() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//	create question first
		vm.createOrUpdateQuestion("question", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		Question question = vm.getQuestionInList().get(0);

		//	create answer first
		vm.createOrUpdateAnswer("is life", true, question, null);

		//get answer
		Answer ans = vm.getQuestionAndAnswer().get(question).get(0);

		//create reply
		vm.createOrUpdateReply("reeeeply", true, ans, null);

		Answer replllly = ans.getReply().get(0);

		//delete reply
		vm.deleteAnswer(question, replllly);

		assertFalse(ans.getReply().contains(replllly));
	}

	/**********
	 * This method tests marking question as solved
     * 
	 */
	@Test
	public void testSolvedQuestion() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//create question first
		vm.createOrUpdateQuestion("quesss", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		Question question = vm.getQuestionInList().get(0);
		vm.solvedQuestion(question);

		assertEquals("Question (solved): quesss", question.toString());
		assertTrue("marked as solved", question.isSolved());
	}

	/**********
	 * This method tests the condition when user did not select question but 
	 * press the solved button
     * 
	 */
	@Test
	public void testSolvedQuestion_noSelected() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//create question first
		vm.createOrUpdateQuestion("quesss", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		Question question = vm.getQuestionInList().get(0);
		vm.solvedQuestion(null);

		assertEquals("quesss", question.getQuestionFromUser());
		assertFalse(question.isSolved());
	}
	
	/**********
	 * This method tests creating a reply
     * 
	 */
	@Test
	public void testCreateOrUpdateReply_create() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//	create question first
		vm.createOrUpdateQuestion("question", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		Question question = vm.getQuestionInList().get(0);
		//	create answer first
		vm.createOrUpdateAnswer("is life", true, question, null);

		//get answer
		Answer ans = vm.getQuestionAndAnswer().get(question).get(0);

		vm.createOrUpdateReply("reeeeply", true, ans, null);

		assertEquals("reeeeply", ans.getReply().get(0).getAnswerFromUser());

	}

	/**********
	 * This method tests updating a reply
     * 
	 */
	@Test
	public void testCreateOrUpdateReply_update() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//	create question first
		vm.createOrUpdateQuestion("question", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		Question question = vm.getQuestionInList().get(0);

		//	create answer first
		vm.createOrUpdateAnswer("is life", true, question, null);

		//get answer
		Answer ans = vm.getQuestionAndAnswer().get(question).get(0);

		//create reply
		vm.createOrUpdateReply("reeeeply", true, ans, null);

		Answer replllly = ans.getReply().get(0);

		vm.createOrUpdateReply("newply", false, ans, replllly);

		assertEquals("newply", ans.getReply().get(0).getAnswerFromUser());

	}

	/**********
	 * This method tests not selecting a reply to update
     * 
	 */
	@Test
	public void testCreateOrUpdateReply_noSelectedReply() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();

		//	create question first
		vm.createOrUpdateQuestion("question", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		Question question = vm.getQuestionInList().get(0);

		//	create answer first
		vm.createOrUpdateAnswer("is life", true, question, null);

		//get answer
		Answer ans = vm.getQuestionAndAnswer().get(question).get(0);

		//create reply
		vm.createOrUpdateReply("reeeeply", true, ans, null);

		// no selected reply
		vm.createOrUpdateReply("newply", false, ans, null);

		assertEquals("reeeeply", ans.getReply().get(0).getAnswerFromUser());

	}

	/**********
	 * This method tests searching functionality when the search query 
	 * exists in question, answer, and reply
     * 
	 */
	@Test
	public void testInitiatedGlobalSearch_foundInAllLevel() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();
		
		ObservableList<Question> questions = FXCollections.observableArrayList();
		
		// create question first
		vm.createOrUpdateQuestion("questioi7on", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		Question question = vm.getQuestionInList().get(0);

		//	create answer first
		vm.createOrUpdateAnswer("is oi7life", true, question, null);

		//get answer
		Answer ans = vm.getQuestionAndAnswer().get(question).get(0);

		//create reply
		vm.createOrUpdateReply("reeeoi7eply", true, ans, null);
		
		questions.add(question);
		
		FilteredList<Question> shouldInclude = vm.initiatedGlobalSearch("oi7");
		
		assertEquals("questioi7on", shouldInclude.get(0).getQuestionFromUser());
		assertTrue(vm.getQuestionAndAnswer().get(question)
                .stream().anyMatch(a -> a.getAnswerFromUser().contains("oi7")));

        assertTrue(ans.getReply()
                .stream().anyMatch(r -> r.getAnswerFromUser().contains("oi7")));
	}
	
	/**********
	 * This method tests the condition when the search query can not be found
     * 
	 */
	@Test
	public void testInitiatedGlobalSearch_notFound() {
		DiscussionPageViewModel vm = new DiscussionPageViewModel();
		
		ObservableList<Question> questions = FXCollections.observableArrayList();
		
		// create question first
		vm.createOrUpdateQuestion("questio7on", true, null);
		assertFalse(vm.getQuestionInList().isEmpty());

		Question question = vm.getQuestionInList().get(0);

		//	create answer first
		vm.createOrUpdateAnswer("is oilife", true, question, null);

		//get answer
		Answer ans = vm.getQuestionAndAnswer().get(question).get(0);

		//create reply
		vm.createOrUpdateReply("reeei7eply", true, ans, null);
		
		questions.add(question);
		
		FilteredList<Question> shouldInclude = vm.initiatedGlobalSearch("oi7");
		
		assertTrue(shouldInclude.isEmpty());
		assertFalse(vm.getQuestionAndAnswer().get(question)
                .stream().anyMatch(a -> a.getAnswerFromUser().contains("oi7")));

        assertFalse(ans.getReply()
                .stream().anyMatch(r -> r.getAnswerFromUser().contains("oi7")));
	}
}
















