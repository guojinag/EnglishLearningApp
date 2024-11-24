package com.englishlearningapp.model;

import lombok.Data;

@Data
public class QuestionData {
    private int id;
    private String type;
    private String relatedBook;
    private String aiModule;
    private String question;
    private String answer;
    private int isCollected;

    public QuestionData(int id,String type, String relatedBook, String aiModule, String question, String answer,int isCollected) {
        this.id = id;
        this.type = type;
        this.relatedBook = relatedBook;
        this.aiModule = aiModule;
        this.question = question;
        this.answer = answer;
        this.isCollected = isCollected;
    }
}
