package com.englishlearningapp.model;

import lombok.Data;

@Data
public class VocabularyData {
    private int vocabulary;
    private String date;

    public VocabularyData(int vocabulary, String date) {
        this.vocabulary = vocabulary;
        this.date = date;
    }
}
