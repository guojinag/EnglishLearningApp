package com.englishlearningapp.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class VocabularySearchView extends BorderPane {

    public VocabularySearchView() {
        try {
            //String pa= String.valueOf(getClass());
            //String path= String.valueOf(getClass().getResource("/fxml/VocabularySearchView.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/VocabularySearchView.fxml"));
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

