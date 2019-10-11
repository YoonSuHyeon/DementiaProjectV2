package com.example.last;

public class Problem {
    String example ;
    String answer;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    String url;
    String num;

     public Problem(String example, String answer, String url,String num) {
         this.example = example;
         this.answer = answer;
         this.url = url;
         this.num = num;
     }
    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }






}