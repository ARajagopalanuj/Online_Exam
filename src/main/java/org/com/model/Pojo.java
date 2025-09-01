package org.com.model;

public class Pojo {        //Plain Old Java Object

    private int id;
    private String question;
    private String op1;
    private String op2;
    private String op3;
    private String op4;
    private String op5;
    private String ans;
    private String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Pojo(String topic) {
        this.topic = topic;
    }

    public Pojo(int id, String question, String op1, String op2, String op3, String op4, String ans, String topic) {
        this.id=id;
        this.question = question;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.op4 = op4;
        this.op5 = op5;
        this.ans = ans;
        this.topic = topic;
    }

    public String getOp4() {
        return op4;
    }

    public void setOp4(String op4) {
        this.op4 = op4;
    }

    public Pojo(int id,String question, String op1, String op2, String op3, String op4,String op5) {
        this.id = id;
        this.question = question;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.op4 = op4;
        this.op5 = op5;
    }

    //encapsualtion
    public Pojo(int id, String question) {
        this.id = id;
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getOp3() {
        return op3;
    }

    public void setOp3(String op3) {
        this.op3 = op3;
    }

    public String getAns() {
        return ans;
    }

    public String getOp5() {
        return op5;
    }

    public void setOp5(String op5) {
        this.op5 = op5;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}
