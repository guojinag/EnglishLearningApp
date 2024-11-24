package com.englishlearningapp.view;

import com.englishlearningapp.dao.QuestionDAO;
import com.englishlearningapp.model.QuestionData;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class QuestionBankView extends BorderPane {

    private TestView testView;
    private Button returnButton;
    private QuestionDAO questionDAO;
    private HBox searchBox;
    private TextField searchField;
    private Button searchButton;
    private ScrollPane resultPane;
    private VBox resultBox;
    private VBox detailBox;


    public QuestionBankView(TestView testView) {
        this.testView = testView;
        returnButton = new Button("返回");
        returnButton.setOnAction(event -> {
            testView.showTestView();
        });
        questionDAO = new QuestionDAO();


        searchField = new TextField();
        searchField.setPromptText("输入题目信息");
        searchButton=new Button("搜索");
        searchButton.setOnAction(event -> {
            try {
                performSearch();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        searchBox = new HBox(searchField, searchButton,returnButton);
        this.setTop(searchBox);
        resultBox=new VBox(new Label("这里是答案界面"));
        resultPane = new ScrollPane(resultBox);
        this.setCenter(resultPane);

        detailBox=new VBox();
        this.setRight(detailBox);

        try {
            performSearch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void performSearch() throws SQLException{
        List<QuestionData> list=questionDAO.selectAll();
        resultBox.getChildren().clear();
        for(QuestionData q:list){
            String info;
            if(q.getQuestion().length()>50){
                info = q.getQuestion().substring(0, 49) + "...";
            }else{
                info = q.getQuestion();
            }
            Button button=new Button(info);
            button.setOnAction(event -> {
                showQuestionDetails(q);
            });
            resultBox.getChildren().add(button);

        }
    }

    private void showQuestionDetails(QuestionData q) {
        detailBox = new VBox(10);

        // 添加单词信息
        detailBox.getChildren().addAll(
                new Label(q.getType()),
                new Label(q.getAnswer()),
                new Label(q.getQuestion()),
                new Label(q.getAiModule()),
                new Label(q.getRelatedBook())
        );
        Button collectButton=new Button(q.getIsCollected()==0?"收藏":"取消收藏");
        collectButton.setOnAction(event -> {
            collectButton.setText(q.getIsCollected()==1?"收藏":"取消收藏");
            try {
                setCollected(q);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            q.setIsCollected(q.getIsCollected()==1?0:1);
        });
        detailBox.getChildren().add(collectButton);

        setRight(detailBox);
    }

    private void setCollected(QuestionData question) throws SQLException {
        questionDAO.collectQuestion(question);
    }
}
