package com.englishlearningapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainViewController {

    @FXML
    private BorderPane mainPane;


    @FXML
    public void showBookReaderView() throws IOException {
        System.out.println("阅读按钮被按下");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BookReaderView.fxml"));
        BorderPane BookReaderView= loader.load();
        mainPane.setCenter(BookReaderView);
    }

    @FXML
    public void showVocabularySearchView() throws IOException {
        System.out.println("词汇按钮被按下");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/VocabularySearchView.fxml"));
        BorderPane vocabularySearchView= loader.load();
        mainPane.setCenter(vocabularySearchView);
    }

    @FXML
    public void showQuestionView() throws IOException {
        System.out.println("题目按钮被按下");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/QuestionView.fxml"));
        BorderPane questionView= loader.load();
        mainPane.setCenter(questionView);
    }
}
