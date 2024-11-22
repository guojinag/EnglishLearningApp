package com.englishlearningapp.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class QuestionView extends BorderPane {

    public QuestionView() {
        try {
            //String pa= String.valueOf(getClass().getResource("/fxml/QuestionView.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/QuestionView.fxml"));
            BorderPane root = (BorderPane) loader.load();
            //this = (QuestionView) root;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
