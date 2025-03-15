# CSE360 Individual Homework 3

DANIEL YI-CHIAN LIAO

![Demo!](https://github.com/user-attachments/assets/acede184-16b8-4684-813c-9ccfb13b2e36)

### Video explains the JUnit Tests: 

https://youtu.be/O4hEGBCX0_8

### [Go to Tests](https://github.com/daniliao/edDiscussionLite/blob/2a11b36e5ad4ea1b86975c636dfe71c744e4e600/FoundCode%20copy/src/test/DiscussionPageTests.java)





### 1. Students to ask questions and 5. Students to update questions or answers.
```java
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
```

### 2. Students to propose answers and 5. Students to update questions or answers.
```java
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
```
### 3. Students to search for and read questions and proposed answers.

Listens for user searches on search bar
```java
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
```

initiatedGlobalSearch(String searchQuery) filters questionInList based on the user's search query. It checks:

1. If the question itself (from question.getQuestionFromUser()) contains the searchQuery.

2. If any of the answers associated with the question contain the searchQuery
```java
public FilteredList<Question> initiatedGlobalSearch(String searchQuery) {
        return questionInList.filtered(question ->
            question.getQuestionFromUser().toLowerCase().contains(searchQuery.toLowerCase()) || questionAndAnswer.get(question).stream().anyMatch(answer -> answer.getAnswerFromUser().toLowerCase().contains(searchQuery.toLowerCase()) || matchReply(answer, searchQuery))
        );
    }
```

filterAnswer(Question question, String searchQuery) 

1. Loops through all answers associated with the given question.

2. If an answer contains the searchQuery, it is added to the tree we created.

3. If a reply to an answer also contains the searchQuery, it is added as a child of the answer.

```java
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
```


### 4. Students to ask for or suggest clarifications. 
```java
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
```

### 6. Students that posted the question to announce that a specific answer addressed the issue that prompted the initial question.
```java
// User story 6: Students that posted the question to announce that a specific answer addressed the issue that prompted the initial question.
    public void solvedQuestion(Question selected) {
        if (selected != null) {
            selected.markAsSolved();
            questionInList.set(questionInList.indexOf(selected), selected);
        }
    }
```

### 7. As a student, I can see a list of all unresolved questions
```java
filterUnresolvedButton.setOnAction(e -> {
            FilteredList<Question> unresolvedQuestions = viewModel.getQuestionInList().filtered(question -> !question.isSolved());
            questionInListView.setItems(unresolvedQuestions);
        });
```

## New files from team project 1 to team project 2

```
View
└──DiscussionPageView.class
```

```
model
└──Answer.class
└──Answers.class
└──Question.class
└──Questions.class
```

```
viewModel
└── DiscussionPageViewModel.class
```

```
.
├── FoundCode copy
│   ├── bin
│   │   ├── application
│   │   │   ├── AdminHomePage.class
│   │   │   ├── AdminSetupPage.class
│   │   │   ├── FirstPage.class
│   │   │   ├── InvitationPage.class
│   │   │   ├── SetupAccountPage.class
│   │   │   ├── SetupLoginSelectionPage.class
│   │   │   ├── StartCSE360.class
│   │   │   ├── User.class
│   │   │   ├── UserHomePage.class
│   │   │   ├── UserLoginPage.class
│   │   │   ├── View
│   │   │   │   └── DiscussionPageView.class
│   │   │   ├── WelcomeLoginPage.class
│   │   │   ├── model
│   │   │   │   ├── Answer.class
│   │   │   │   ├── Answers.class
│   │   │   │   ├── Question.class
│   │   │   │   └── Questions.class
│   │   │   └── viewModel
│   │   │       └── DiscussionPageViewModel.class
│   │   ├── databasePart1
│   │   │   └── DatabaseHelper.class
│   │   └── module-info.class
│   └── src
│       ├── application
│       │   ├── AdminHomePage.java
│       │   ├── AdminSetupPage.java
│       │   ├── FirstPage.java
│       │   ├── InvitationPage.java
│       │   ├── SetupAccountPage.java
│       │   ├── SetupLoginSelectionPage.java
│       │   ├── StartCSE360.java
│       │   ├── User.java
│       │   ├── UserHomePage.java
│       │   ├── UserLoginPage.java
│       │   ├── View
│       │   │   └── DiscussionPageView.java
│       │   ├── WelcomeLoginPage.java
│       │   ├── model
│       │   │   ├── Answer.java
│       │   │   ├── Answers.java
│       │   │   ├── Question.java
│       │   │   └── Questions.java
│       │   └── viewModel
│       │       └── DiscussionPageViewModel.java
│       ├── databasePart1
│       │   └── DatabaseHelper.java
│       └── module-info.java
├── README.md
├── Vids
│   └── (Show All Questions , Show Unresolved Questions).mp4
├── old code
│   ├── AdminHomePage.java
│   ├── AdminSetupPage.java
│   ├── DatabaseHelper.java
│   ├── FirstPage.java
│   ├── InvitationPage.java
│   ├── SetupAccountPage.java
│   ├── SetupLoginSelectionPage.java
│   ├── StartCSE360.java
│   ├── User.java
│   ├── UserHomePage.java
│   ├── UserLoginPage.java
│   └── WelcomeLoginPage.java
└── old vids
    ├── GMT20250208-022512_Recording_1440x900.mp4
    ├── Logout Functionality Test Cases(demo).mp4
    ├── Logout Functionality code explanation.mp4
    └── Matthew Cruz- screencast test for user roles and temp password.mp4
```

