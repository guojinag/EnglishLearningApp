package com.englishlearningapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.List;

public class VocabularySearchView extends BorderPane {

    private TextField searchField;
    private Button searchButton;
    private VBox resultBox;
    private Pagination pagination;

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
        searchButton = new Button("Search");
        searchButton.setOnAction(event -> performSearch());

        // 将搜索文本框和按钮添加到搜索栏
        searchBar.getChildren().addAll(searchField, searchButton);

        // 将搜索栏添加到 BorderPane 的顶部
        setTop(searchBar);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        // 创建结果展示区域
        resultBox = new VBox();
        resultBox.setSpacing(5);
        resultBox.setPadding(new Insets(10));

        // 将结果展示区域添加到 BorderPane 的中心
        setCenter(resultBox);

        // 创建分页控件
        pagination = new Pagination();
        pagination.setPageCount(1); // 初始页数为1
        pagination.setCurrentPageIndex(0);
        pagination.setMaxPageIndicatorCount(5);
        pagination.setPageFactory(this::createPage);

        // 将分页控件添加到 BorderPane 的底部
        setBottom(pagination);
    }

    private void performSearch() {
        // 获取搜索词
        String searchTerm = searchField.getText();

        // 这里可以调用数据库查询方法，获取搜索结果
        // List<String> searchResults = databaseQuery(searchTerm);

        // 假设我们有一个模拟的搜索结果列表
        List<String> searchResults = Arrays.asList("apple", "banana", "cherry", "date", "elderberry", "fig", "grape", "honeydew");

        // 清空结果展示区域
        resultBox.getChildren().clear();

        // 将搜索结果添加到结果展示区域
        for (String word : searchResults) {
            Button wordButton = new Button(word);
            wordButton.setFont(Font.font("Arial", 14));
            wordButton.setTextFill(Color.BLACK);
            wordButton.setPrefWidth(200);
            wordButton.setOnAction(event -> showWordDetails(word));
            resultBox.getChildren().add(wordButton);
        }

        // 更新分页控件
        int totalResults = searchResults.size();
        int itemsPerPage = 5;
        int pageCount = (int) Math.ceil((double) totalResults / itemsPerPage);
        pagination.setPageCount(pageCount);
    }

    private Node createPage(int pageIndex) {
        // 创建一个 VBox 用于展示当前页的单词
        VBox pageBox = new VBox();
        pageBox.setSpacing(5);
        pageBox.setPadding(new Insets(10));

        // 计算当前页的起始和结束索引
        int itemsPerPage = 5;
        int startIndex = pageIndex * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, resultBox.getChildren().size());

        // 将当前页的单词添加到 pageBox
        for (int i = startIndex; i < endIndex; i++) {
            pageBox.getChildren().add(resultBox.getChildren().get(i));
        }

        return pageBox;
    }

    private void showWordDetails(String word) {
        // 这里可以实现显示单词详细信息的逻辑
        System.out.println("Showing details for word: " + word);
    }
}
