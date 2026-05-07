package com.example.learningbyloosing;

import java.util.List;

public class QuestionData {
    String correct_answer, question;
    List<String> options;

    public QuestionData(String correct_answer, List<String> options, String question){
        this.correct_answer = correct_answer;
        this.question = question;
        this.options = options;
    }

    public String getQuestion() { return question; }
    public String getCorrect_answer() { return correct_answer; }
    public List<String> getOptions() { return options; }
    public void setQuestion(String question) { this.question = question; }
    public void setCorrect_answer(String correct_answer) { this.correct_answer = correct_answer; }
    public void setOptions(List<String> options) { this.options = options; }
}
