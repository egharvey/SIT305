package com.example.learningbyloosing;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "history")
public class History {

    @PrimaryKey (autoGenerate = true)
    private int id;

    private String q1,  a11,  a12,  a13,  q2,  a21,  a22,  a23,  q3,  a31,  a32,  a33,  c1,  c2, c3, s1, s2, s3;
    private int userID;

    public History(String q1, String a11, String a12, String a13, String q2, String a21, String a22, String a23, String q3, String a31, String a32, String a33, String c1, String c2, String c3, String s1, String s2, String s3, int userID){
        this.userID = userID;
        this.q1 = q1;
        this.a11 = a11;
        this.a12 = a12;
        this.a13 = a13;
        this.q2 = q2;
        this.a21 = a21;
        this.a22 = a22;
        this.a23 = a23;
        this.q3 = q3;
        this.a31 = a31;
        this.a32 = a32;
        this.a33 = a33;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
    }

    //Getters
    public int getId() { return id; }
    public int getUserID() { return userID; }
    public String getA11() { return a11; }
    public String getA12() { return a12; }
    public String getA13() { return a13; }
    public String getA21() { return a21; }
    public String getA22() { return a22; }
    public String getA23() { return a23; }
    public String getA31() { return a31; }
    public String getA32() { return a32; }
    public String getA33() { return a33; }
    public String getQ1() { return q1; }
    public String getQ2() { return q2; }
    public String getQ3() { return q3; }
    public String getC1() { return c1; }
    public String getC2() { return c2; }
    public String getC3() { return c3; }
    public String getS1() { return s1; }
    public String getS2() { return s2; }
    public String getS3() { return s3; }

    public QuizData getQuizData() {
        List<String> answers1 = new ArrayList<>();
        answers1.add(this.a11);
        answers1.add(this.a12);
        answers1.add(this.a13);
        List<String> answers2 = new ArrayList<>();
        answers2.add(this.a21);
        answers2.add(this.a22);
        answers2.add(this.a23);
        List<String> answers3 = new ArrayList<>();
        answers3.add(this.a31);
        answers3.add(this.a32);
        answers3.add(this.a33);
        return new QuizData(new QuestionData(c1, answers1, q1), new QuestionData(c2, answers2, q2), new QuestionData(c3, answers3, q3), s1, s2, s3);
    }

    //Setters (room needs set id)
    public void setId(int id) { this.id = id; }
    public void setUserID(int userID) { this.userID = userID; }
    public void setQ1(String string) { this.q1 = string; }
    public void setQ2(String string) { this.q2 = string; }
    public void setQ3(String string) { this.q3 = string; }
    public void setC1(String string) { this.c1 = string; }
    public void setC2(String string) { this.c2 = string; }
    public void setC3(String string) { this.c3 = string; }
    public void setS1(String string) { this.s1 = string; }
    public void setS2(String string) { this.s2 = string; }
    public void setS3(String string) { this.s3 = string; }
    public void setA11(String string) { this.a11 = string; }
    public void setA12(String string) { this.a12 = string; }
    public void setA13(String string) { this.a13 = string; }
    public void setA21(String string) { this.a21 = string; }
    public void setA22(String string) { this.a22 = string; }
    public void setA23(String string) { this.a23 = string; }
    public void setA31(String string) { this.a31 = string; }
    public void setA32(String string) { this.a32 = string; }
    public void setA33(String string) { this.a33 = string; }
}
