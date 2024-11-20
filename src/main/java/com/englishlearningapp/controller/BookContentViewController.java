package com.englishlearningapp.controller;

import com.englishlearningapp.view.BookReaderView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookContentViewController {

    @FXML
    private TextArea bookContent;

    @FXML
    private Pagination pagination;

    @FXML
    private TextField pageNumberField;

    @FXML
    private Button goToPageButton;

    @FXML
    private Button backButton;

    private BookReaderView bookReaderView;
    private List<String> pages;

    public void initialize(BookReaderView bookReaderView, String bookFile) {
        this.bookReaderView = bookReaderView;

        // 初始化书籍内容显示区域
        bookContent.setFont(javafx.scene.text.Font.font("Arial", 14));

        // 加载并分页书籍内容
        loadBookContent(bookFile);

        // 设置分页控件
        pagination.setPageCount(pages.size());
        pagination.setPageFactory(this::createPage);

        // 设置返回按钮的点击事件
        backButton.setOnAction(event -> bookReaderView.showBookReaderView());

        // 设置跳转按钮的点击事件
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
        pageBox.setAlignment(javafx.geometry.Pos.CENTER);

        // 显示页面内容
        showPage(pageIndex);

        // 返回页面容器
        return pageBox;
    }
}
