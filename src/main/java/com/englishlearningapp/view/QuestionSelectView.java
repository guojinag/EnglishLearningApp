package com.englishlearningapp.view;

import com.englishlearningapp.dao.QuestionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionSelectView extends BorderPane {

    private TestView testView;
    private Button returnButton;
    private QuestionDAO questionDAO;
    private List<String> bookList;
    private VBox vBox;

    private Set<String> selectedBooks = new HashSet<>();
    private ObservableList<String> results = FXCollections.observableArrayList();
    private Button selectButton;
    private HBox selectBox;
    private TextField questionNumField;
    private CheckBox needCollect;
    private boolean isCollect;
    private int questionNum;

    public QuestionSelectView(TestView testView) {
        this.testView = testView;
        questionDAO = new QuestionDAO();
        bookList=questionDAO.getBooks();
        returnButton = new Button("返回");
        returnButton.setOnAction(event -> {
            testView.showTestView();
        });
        this.setTop(returnButton);


        questionNumField = new TextField();
        questionNumField.setPromptText("输入题目数量");
        needCollect = new CheckBox("在收藏题目中选择");
        needCollect.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                isCollect = true;
            } else {
                isCollect = false;
            }
        });
        selectBox = new HBox(questionNumField, needCollect);


        vBox = new VBox(new Label("选择你的题库"));
        for(String book:bookList){
            CheckBox checkBox = new CheckBox(book);
            checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedBooks.add(book);
                } else {
                    selectedBooks.remove(book);
                }
            });
            vBox.getChildren().add(checkBox);
        }
        vBox.getChildren().add(selectBox);
        this.setLeft(vBox);
        selectButton=new Button("确认选择");
        selectButton.setOnAction(event -> {
            if(isInteger(questionNumField.getText())){
                questionNum = Integer.parseInt(questionNumField.getText());
                if(questionNum>0){
                    testView.setCenter(new QuestionTestView(testView,selectedBooks,questionNum,isCollect));
                    return;
                }
            }
            Stage stage=new Stage();
            Scene scene=new Scene(new Label("请输入非负整数！"));
            stage.setScene(scene);
            stage.show();
        });
        this.setBottom(selectButton);
    }

    private boolean isInteger(String input) {
        try {
            int test=Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }
}
