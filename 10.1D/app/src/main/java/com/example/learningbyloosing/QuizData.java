package com.example.learningbyloosing;

import androidx.annotation.Nullable;

public class QuizData {
    QuestionData question1, question2, question3;
    String answer1, answer2, answer3;

    public QuizData(QuestionData question1, QuestionData question2, QuestionData question3, @Nullable String a1, @Nullable String a2, @Nullable String a3){
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
        this.answer1 = a1;
        this.answer2 = a2;
        this.answer3 = a3;
    }

    public QuestionData getQuestion1() { return question1; }
    public QuestionData getQuestion2() { return question2; }
    public QuestionData getQuestion3() { return question3; }
    public String getAnswer1() { return answer1; }
    public String getAnswer2() { return answer2; }
    public String getAnswer3() { return answer3; }
    public void setQuestion1(QuestionData question) { this.question1 = question; }
    public void setQuestion2(QuestionData question) { this.question2 = question; }
    public void setQuestion3(QuestionData question) { this.question3 = question; }
    public void setAnswer1(String answer) { this.answer1 = answer; }
    public void setAnswer2(String answer) { this.answer2 = answer; }
    public void setAnswer3(String answer) { this.answer3 = answer; }
}

