package com.englishlearningapp.view;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class TestView extends BorderPane {

    private BorderPane subPane;
    private Button questionSelectButton;
    private Button questionBankButton;

    public TestView() {
        subPane = new BorderPane();
        questionSelectButton = new Button("题目测试");
        questionSelectButton.setOnAction(e -> {
            this.setCenter(new QuestionSelectView(this));
        });
        subPane.setLeft(questionSelectButton);

        questionBankButton= new Button("查看题库");
        questionBankButton.setOnAction(e -> {
            this.setCenter(new QuestionBankView(this));
        });
        subPane.setRight(questionBankButton);

        this.setCenter(subPane);
    }

    public void showTestView(){
        this.setCenter(subPane);
    }
}
