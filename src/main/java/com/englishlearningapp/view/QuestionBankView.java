package com.englishlearningapp.view;

import com.englishlearningapp.dao.QuestionDAO;
import com.englishlearningapp.model.QuestionData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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

    private SplitPane splitPane;
    private BorderPane rightPane;

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
            performSearch();
        });
        searchBox = new HBox(returnButton,searchField, searchButton);
        searchBox.setSpacing(10);
        this.setTop(searchBox);
        resultBox=new VBox(new Label("这里是题目浏览界面"));
        resultPane = new ScrollPane(resultBox);

        detailBox=new VBox();
        rightPane=new BorderPane(detailBox);
        splitPane=new SplitPane(rightPane,resultPane);
        splitPane.setDividerPositions(0.6);
        this.setCenter(splitPane);


        performSearch();

    }

    private void performSearch() {
        List<QuestionData> list=questionDAO.selectAll();
        resultBox.getChildren().clear();
        for(QuestionData q:list){
            String info;
            info=q.getRelatedBook();
            Button button=new Button();
            button.setOnAction(event -> {
                showQuestionDetails(q);
            });
            if(q.getIsCollected()==1){
                button.setStyle("-fx-background-color: #dde56c");
                info="已收藏："+info;
            }
            button.setText(info);
            resultBox.getChildren().add(button);

        }
    }

    private void showQuestionDetails(QuestionData q) {

        Label typeLabel = new Label("题目类型："+q.getType());
        Label aiModuleLabel = new Label("ai模型："+q.getAiModule());
        Label relatedBookLabel = new Label("关联书籍："+q.getRelatedBook());
        HBox typeBox=new HBox(typeLabel,aiModuleLabel,relatedBookLabel);
        typeBox.setSpacing(30);
        typeBox.setPadding(new Insets(10,10,10,10));


        TextFlow answerLabel = new TextFlow(new Text("答案："+q.getAnswer()));

        answerLabel.setVisible(false);
        Button answerButton=new Button("显示答案");
        answerButton.setOnAction(event -> {
            answerLabel.setVisible(true);
            answerButton.setVisible(false);
        });
        answerButton.setAlignment(Pos.CENTER_LEFT);
        StackPane stackPane=new StackPane(answerLabel,answerButton);
        TextFlow questionLabel = new TextFlow(new Text(q.getQuestion()));
        detailBox = new VBox(typeBox,questionLabel,stackPane);

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

        detailBox.setSpacing(10);
        detailBox.setPadding(new Insets(10,10,10,10));

        rightPane.setCenter(detailBox);
    }

    private void setCollected(QuestionData question) throws SQLException {
        questionDAO.collectQuestion(question);
    }
}
