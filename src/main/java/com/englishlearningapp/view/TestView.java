package com.englishlearningapp.view;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TestView extends VBox {

    public TestView() {
        // 创建一个标签，表示题目测试界面
        Label label = new Label("题目测试界面");
        this.getChildren().add(label);
    }
}
