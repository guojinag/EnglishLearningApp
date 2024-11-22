package com.englishlearningapp.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class TestView extends BorderPane {

    public TestView() {
        // 创建一个标签，表示题目测试界面
        Label label = new Label("题目测试界面");
        this.setCenter(label);
        label.setAlignment(Pos.CENTER);
    }
}
