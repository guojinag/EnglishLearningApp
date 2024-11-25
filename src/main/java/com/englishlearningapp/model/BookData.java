package com.englishlearningapp.model;

import lombok.Data;

@Data
public class BookData {
    private String book;
    private int index;
    public BookData(String book, int index) {
        this.book = book;
        this.index = index;
    }
}
