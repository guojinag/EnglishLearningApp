package com.englishlearningapp.view;

import com.englishlearningapp.dao.QuestionDAO;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class QuestionSelectView extends BorderPane {

    private TestView testView;
    private Button returnButton;
    private QuestionDAO questionDAO;
    private List<String> bookList;
    private VBox vBox;
    public QuestionSelectView(TestView testView) {
        this.testView = testView;
        questionDAO = new QuestionDAO();
        bookList=questionDAO.getBooks();
        returnButton = new Button("返回");
        returnButton.setOnAction(event -> {
            testView.showTestView();
        });
        this.setTop(returnButton);

        Label label = new Label("选择你的题库");
        label.setAlignment(Pos.CENTER);

        vBox = new VBox(label);
        for(String book:bookList){
            Label l = new Label(book);
            CheckBox checkBox = new CheckBox(book);
            vBox.getChildren().add(checkBox);
        }
        this.setLeft(vBox);
    }


}
