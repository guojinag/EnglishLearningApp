package com.englishlearningapp.view;

import com.englishlearningapp.dao.WordDAO;
import com.englishlearningapp.model.WordData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.SQLException;
import java.util.List;

public class VocabularySearchView extends BorderPane {

    private TextField searchField;
    private Button searchButton;
    private VBox resultBox;
    //private Pagination pagination;
    private ScrollPane resultScrollPane;
    private VBox detailPane;

    private WordDAO wordDAO;

    public VocabularySearchView() {
        // 设置 BorderPane 的内边距
        setPadding(new Insets(10));

        // 创建顶部搜索栏
        HBox searchBar = new HBox();
        searchBar.setSpacing(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        // 创建搜索文本框
        searchField = new TextField();
        searchField.setPromptText("Enter a word to search");
        searchField.setPrefWidth(300);
        // 创建搜索按钮
        searchButton = new Button("搜索");
        searchButton.setOnAction(event -> {
            try {
                performSearch();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        // 将搜索文本框和按钮添加到搜索栏
        searchBar.getChildren().addAll(searchField, searchButton);
        // 将搜索栏添加到 BorderPane 的顶部
        setTop(searchBar);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        // 创建结果展示区域
        resultBox = new VBox();
        resultBox.setSpacing(5);
        resultBox.setPadding(new Insets(10));

        resultScrollPane = new ScrollPane();
        resultScrollPane.setContent(resultBox);
        // 将结果展示区域添加到 BorderPane 的中心
        setCenter(resultScrollPane);


    }

    private void performSearch() throws SQLException {
        // 获取搜索词
        String searchTerm = searchField.getText();

        wordDAO = new WordDAO();
        // 这里可以调用数据库查询方法，获取搜索结果
        List<WordData> searchResults = wordDAO.selectWordByWord(searchTerm);

        // 清空结果展示区域
        resultBox.getChildren().clear();

        // 将搜索结果添加到结果展示区域
        for (WordData word : searchResults) {
            Button wordButton = new Button(word.getWord());
            wordButton.setFont(Font.font("Arial", 14));
            wordButton.setTextFill(Color.BLACK);
            wordButton.setPrefWidth(200);
            wordButton.setOnAction(event -> showWordDetails(word));
            resultBox.getChildren().add(wordButton);
        }

        wordDAO.closeConnection();
    }



    private void showWordDetails(WordData word) {
        detailPane = new VBox(10);

        // 添加单词信息
        detailPane.getChildren().addAll(
                new Label(word.getWord()),
                new Label(word.getPhonetic()),
                new Label(word.getExplanation())
        );
        Button collectButton=new Button(word.getIsCollected()==0?"收藏":"取消收藏");
        collectButton.setOnAction(event -> {
            collectButton.setText(word.getIsCollected()==1?"收藏":"取消收藏");
            try {
                setCollected(word);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            word.setIsCollected(word.getIsCollected()==1?0:1);
        });
        detailPane.getChildren().add(collectButton);

        setRight(detailPane);
    }

    private void setCollected(WordData word) throws SQLException {
        WordDAO wordDAO = new WordDAO();
        wordDAO.collectWord(word.getWord(),word.getIsCollected()==0?1:0);
    }

}
