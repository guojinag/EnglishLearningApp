package com.englishlearningapp.model;

import lombok.Data;

@Data
public class QuestionData {
    private String type;
    private String relatedBook;
    private String aiModule;
    private String question;
    private String answer;

}
