package com.englishlearningapp.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class VocabularyTestView extends BorderPane {

    public VocabularyTestView(){
        Button button = new Button("点击开始词汇测试");
        button.setOnAction(e -> {
            try {
                openWebsite("https://preply.com/en/learn/english/test-your-vocab?msockid=2d4f837f09f76dfa2c2697fe08c16ce6");
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        });
        this.setCenter(button);
        button.setAlignment(Pos.CENTER);
    }

    private void openWebsite(String url) throws IOException, URISyntaxException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            System.out.println("Desktop browsing is not supported.");
        }
    }

}
