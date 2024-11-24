package com.englishlearningapp.view;

import com.englishlearningapp.dao.QuestionDAO;
import com.englishlearningapp.model.QuestionData;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuestionTestView extends BorderPane {

    private TestView testView;
    private Button returnButton;
    private List<String> bookList;
    private int questionNum;
    private boolean isCollected;

    private QuestionDAO questionDAO;
    private List<QuestionData> questionDataList;
    private ScrollPane resultPane;
    private VBox resultBox;
    private VBox detailBox;

    public QuestionTestView(TestView testView, Set<String> bookList, int questionNum, boolean isCollected) {
        this.testView = testView;
        this.questionNum = questionNum;
        this.isCollected = isCollected;
        List<String> tmpList = new ArrayList<>();
        for(String book : bookList) {
            tmpList.add(book);
        }
        this.bookList = tmpList;
        returnButton = new Button("返回");
        returnButton.setOnAction(event -> {
            testView.showTestView();
        });
        this.setTop(returnButton);
        questionDAO = new QuestionDAO();



        questionDataList = questionDAO.selectQuestion(this.bookList,isCollected);

        resultBox=new VBox(new Label("这里是题目浏览界面"));
        resultPane = new ScrollPane(resultBox);
        this.setCenter(resultPane);
        this.questionNum=Math.min(questionNum,questionDataList.size());
        Random rand = new Random();
        List<Integer> randomNumbers = new ArrayList<>();
        while (randomNumbers.size() < this.questionNum) {
            int randomNumber = rand.nextInt(questionDataList.size() + 1);
            System.out.println(randomNumber);
            if (!randomNumbers.contains(randomNumber)) {
                randomNumbers.add(randomNumber);
            }
        }
        for(Integer i:randomNumbers){
            QuestionData q=questionDataList.get(i);
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
        detailBox=new VBox();
        this.setRight(detailBox);

    }
    private void showQuestionDetails(QuestionData q) {
        detailBox = new VBox(10);
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
