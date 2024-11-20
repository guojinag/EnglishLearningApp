package com.englishlearningapp.view;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class VocabularySearchView extends VBox {

    public VocabularySearchView() {
        // 创建一个标签，表示词汇查找界面
        Label label = new Label("词汇查找界面");
        this.getChildren().add(label);
    }
}
