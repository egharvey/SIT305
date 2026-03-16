# QuizTime 3.1C
This is a multiple choice quiz app made for SIT305 task 3.1C. The app is made up of 3 activities. 
    
The first is a simple welcome page that takes the user's name. Once entered they can click the start button which takes them to the second activity.    

The second activity is the quiz itself. It is a series of 5 randomly selected multiple choice questions. The user clicks 1 of 3 options and then submit. Once submitted, they will see of they got the right answer and what the right answer is should they answer wrong. They can then procede by pressing the next button to get the next question (staying in this activity). Once all 5 questions have been answered, the next button will take the user to the final activity.    

The final activity is the results page. It displays a congratulations with the user's name as well as their score out of 5. These are passed to the activity using intent extras. From here, the user has 2 options. First they can press the start new quiz button, taking them to the first activity, this time with their name already entered, ready for them to start immidiately. Alternatively, they could press the finish button, closing the app with the finish() command.  
## Important Files  
- In app/src/main/java/com/example/quiztime, you will find the 3 java files that run each activity
- In app/src/main/res/layout, you will find the layout xml files for the 3 activities
- In app/src/main/res/values, you will find string.xml and colors.xml which are referenced throughout the aformentioned xml and java files