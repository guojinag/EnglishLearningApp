package com.englishlearningapp.view;

import com.englishlearningapp.dao.WordDAO;
import com.englishlearningapp.model.WordData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookContentView extends BorderPane {

    private BookReaderView bookReaderView;
    private TextArea bookContent;
    private List<String> pages;
    private Pagination pagination;
    private TextField pageNumberField;
    private WordDAO wordDAO;
    //private TextFlow textFlow;
    private WebView contentView;
    private List<String> collectedWords;

    @SneakyThrows
    public BookContentView(BookReaderView bookReaderView, String bookFile) throws SQLException {
        this.bookReaderView = bookReaderView;

        wordDAO = new WordDAO();
        List<WordData> list = wordDAO.selectWordCollect();
        collectedWords = list.stream().map(WordData::getWord).collect(Collectors.toList());
        // 初始化书籍内容显示区域
        contentView = new WebView();

        //textFlow = new TextFlow(new Text("初始化"));

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
        Button goToPageButton = getGoToPageButton();

        // 将书籍内容和分页控件添加到布局中
        ScrollPane scrollPane = new ScrollPane(contentView);
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

    private Button getGoToPageButton() {
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
        return goToPageButton;
    }

    private void loadBookContent(String bookFile) {
        // 加载并分页书籍内容
        InputStream inputStream = getClass().getResourceAsStream("/books/"+bookFile);
        if (inputStream == null) {
            //textFlow=new TextFlow(new Text("无法加载书籍内容"));
            bookContent.setText("无法加载书籍内容");
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String content = reader.lines().collect(Collectors.joining("\n"));

        // 将内容分页
        pages = new ArrayList<>();
        int pageSize = 1000; // 每页字符数
        int start = 0;
        while (start < content.length()) {
            int end = Math.min(start + pageSize, content.length());
            // 如果当前页的最后一个字符不是空格，向后查找单词边界
            if (end < content.length() && content.charAt(end) != ' ') {
                int wordEnd = content.indexOf(' ', end);
                if (wordEnd != -1) {
                    end = wordEnd;
                }
            }
            pages.add(content.substring(start, end));
            start = end;
        }
    }

    private void showPage(int pageIndex){
        if (pageIndex >= 0 && pageIndex < pages.size()) {

            String highlightedPage = hilightWoirds(pages.get(pageIndex), collectedWords);
            //bookContent.setText(highlightedPage);
            contentView.getEngine().loadContent(highlightedPage);
        }
    }

    private VBox createPage(int pageIndex){
        // 创建一个VBox来容纳页面内容
        VBox pageBox = new VBox(10);
        pageBox.setAlignment(Pos.CENTER);

        // 显示页面内容
        showPage(pageIndex);

        // 返回页面容器
        return pageBox;
    }

    private String hilightWoirds(String content, List<String> wordsToHighlight) {
        StringBuilder textBuilder = new StringBuilder();
        String[] words = content.split("\\s+");

        for (String word : words) {
            if (wordsToHighlight.contains(word)) {
                textBuilder.append("<span style='color:red; text-decoration:underline;'>")
                        .append(word)
                        .append("</span> ");
            } else {
                textBuilder.append(word).append(" ");
            }
        }

        return textBuilder.toString();
    }
}


