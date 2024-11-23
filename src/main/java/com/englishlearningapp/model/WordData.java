package com.englishlearningapp.model;

import lombok.Data;

@Data
public class WordData {

    //private int id;

    private String word;

    private String phonetic;

    private String explanation;

    private String COBUILD_star;

    private int isCollected;

    public WordData(String wordResult, String phonetic, String explanation, String cobuildStar, int isCollected) {
        this.word = wordResult;
        this.phonetic = phonetic;
        this.explanation = explanation;
        this.COBUILD_star = cobuildStar;
        this.isCollected = isCollected;
    }
}
