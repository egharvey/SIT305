package com.example.learningbyloosing;

public class QuizData {
    QuestionData question1, question2, question3;

    public QuizData(QuestionData question1, QuestionData question2, QuestionData question3){
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
    }

    public QuestionData getQuestion1() { return question1; }
    public QuestionData getQuestion2() { return question2; }
    public QuestionData getQuestion3() { return question3; }
    public void setQuestion1(QuestionData question) { this.question1 = question; }
    public void setQuestion2(QuestionData question) { this.question2 = question; }
    public void setQuestion3(QuestionData question) { this.question3 = question; }
}

