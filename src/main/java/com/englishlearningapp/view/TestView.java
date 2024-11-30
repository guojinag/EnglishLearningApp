package com.englishlearningapp.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class TestView extends BorderPane {

    private BorderPane subPane;
    private HBox mainBox;
    private Button questionSelectButton;
    private Button questionBankButton;

    public TestView() {
        //subPane = new BorderPane();
        questionSelectButton = new Button("题目测试");
        questionSelectButton.setId("testViewButton");
        questionSelectButton.setOnAction(e -> {
            this.setCenter(new QuestionSelectView(this));
        });
        questionBankButton= new Button("查看题库");
        questionBankButton.setId("testViewButton");
        questionBankButton.setOnAction(e -> {
            this.setCenter(new QuestionBankView(this));
        });
        mainBox = new HBox(questionSelectButton, questionBankButton);

        this.setCenter(mainBox);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(10);
    }

    public void showTestView(){
        this.setCenter(mainBox);
    }
}
