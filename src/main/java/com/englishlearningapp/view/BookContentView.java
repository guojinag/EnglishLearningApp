package com.englishlearningapp.view;

import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookContentView extends BorderPane {

    private BookReaderView bookReaderView;
    private TextArea bookContent;
    private List<String> pages;
    private Pagination pagination;
    private TextField pageNumberField;

    public BookContentView(BookReaderView bookReaderView, String bookFile) {
        this.bookReaderView = bookReaderView;

        // 初始化书籍内容显示区域
        bookContent = new TextArea();
        bookContent.setEditable(false);
        bookContent.setWrapText(true);
        bookContent.setFont(javafx.scene.text.Font.font("Arial", 14));

        // 加载并分页书籍内容
        loadBookContent(bookFile);

        // 创建返回按钮
        Button backButton = new Button("返回");
        backButton.setOnAction(event -> bookReaderView.showBookReaderView());

        // 创建分页控件
        pagination = new Pagination(pages.size());
        pagination.setPageFactory(this::createPage);

        // 创建快速跳转页码的输入框和按钮
        pageNumberField = new TextField();
        pageNumberField.setPromptText("输入页码");
        Button goToPageButton = new Button("跳转");
        goToPageButton.setOnAction(event -> {
            try {
                int targetPage = Integer.parseInt(pageNumberField.getText()) - 1;
                if (targetPage >= 0 && targetPage < pages.size()) {
                    pagination.setCurrentPageIndex(targetPage);
                    showPage(targetPage);
                } else {
                    bookContent.setText("页码超出范围");
                }
            } catch (NumberFormatException e) {
                bookContent.setText("请输入有效的页码");
            }
        });

        // 将书籍内容和分页控件添加到布局中
        ScrollPane scrollPane = new ScrollPane(bookContent);
        scrollPane.setFitToWidth(true); // 使内容适应宽度
        scrollPane.setFitToHeight(true); // 使内容适应高度

        // 设置书籍内容区域的大小
        scrollPane.setPrefWidth(Double.MAX_VALUE);
        scrollPane.setPrefHeight(Double.MAX_VALUE);

        // 修改布局，将书籍内容扩大到几乎整个窗口，只在下侧保留返回按钮、分页控件和快速跳转页码的输入框和按钮
        HBox bottomBox = new HBox(10, pagination, pageNumberField, goToPageButton, backButton);
        bottomBox.setAlignment(Pos.CENTER);
        this.setCenter(scrollPane);
        this.setBottom(bottomBox);
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(backButton, new Insets(10));

        // 显示第一页内容
        showPage(0);
    }

    private void loadBookContent(String bookFile) {
        // 加载并分页书籍内容
        InputStream inputStream = getClass().getResourceAsStream("/books/"+bookFile);
        if (inputStream == null) {
            bookContent.setText("无法加载书籍内容");
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String content = reader.lines().collect(Collectors.joining("\n"));

        // 将内容分页
        pages = new ArrayList<>();
        int pageSize = 1000; // 每页字符数
        for (int i = 0; i < content.length(); i += pageSize) {
            int end = Math.min(i + pageSize, content.length());
            pages.add(content.substring(i, end));
        }
    }

    private void showPage(int pageIndex) {
        if (pageIndex >= 0 && pageIndex < pages.size()) {
            bookContent.setText(pages.get(pageIndex));
        }
    }

    private VBox createPage(int pageIndex) {
        // 创建一个VBox来容纳页面内容
        VBox pageBox = new VBox(10);
        pageBox.setAlignment(Pos.CENTER);

        // 显示页面内容
        showPage(pageIndex);

        // 返回页面容器
        return pageBox;
    }
}


