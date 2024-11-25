package com.englishlearningapp.view;

import com.englishlearningapp.dao.BookDAO;
import com.englishlearningapp.dao.WordDAO;
import com.englishlearningapp.model.WordData;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookContentView extends BorderPane {

    private BookReaderView bookReaderView;
    private String bookFile;
    private WordDAO wordDAO;
    private BookDAO bookDAO;
    private List<String> collectedWords;
    private int index;

    private List<String> pages;
    private Pagination pagination;
    private TextField pageNumberField;
    private WebView contentView;


    @SneakyThrows
    public BookContentView(BookReaderView bookReaderView, String bookFile)  {
        this.bookReaderView = bookReaderView;
        this.bookFile = bookFile.replace(".txt","");
        //载入收藏单词
        wordDAO = new WordDAO();
        List<WordData> list = wordDAO.selectWordCollect();
        collectedWords = list.stream().map(WordData::getWord).collect(Collectors.toList());
        //载入书签
        bookDAO = new BookDAO();
        this.index=bookDAO.selectBookByTitle(this.bookFile);

        //顶部界面：返回菜单
        Button backButton = new Button("返回");
        backButton.setOnAction(event -> this.bookReaderView.showBookReaderView());
        Button preserveIndexButton =new Button("保存书签");
        preserveIndexButton.setOnAction(event -> {
            bookDAO.updateBookIndex(this.bookFile,this.index);
        });
        HBox topBox = new HBox(backButton, preserveIndexButton);
        this.setTop(topBox);


        // 主界面：书籍内容
        contentView = new WebView();
        // 加载并分页书籍内容
        loadBookContent(bookFile);
        pagination = new Pagination(pages.size());
        pagination.setPageFactory(this::createPage);
        pagination.setCurrentPageIndex(index-1);
        pageNumberField = new TextField();
        pageNumberField.setPromptText("输入页码");
        Button goToPageButton = getGoToPageButton();
        ScrollPane scrollPane = new ScrollPane(contentView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        this.setCenter(scrollPane);

        // 底部界面：分页控制
        HBox bottomBox = new HBox(10, pagination, pageNumberField, goToPageButton);
        bottomBox.setAlignment(Pos.CENTER);
        this.setBottom(bottomBox);


        //showPage(this.index);
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
                    contentView.getEngine().loadContent("页码超出范围");
                }
            } catch (NumberFormatException e) {
                contentView.getEngine().loadContent("请输入有效页码");
            }
        });
        return goToPageButton;
    }

    private void loadBookContent(String bookFile) {
        InputStream inputStream = getClass().getResourceAsStream("/books/"+bookFile);
        if (inputStream == null) {
            contentView.getEngine().loadContent("无法加载书籍内容");
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String content = reader.lines().collect(Collectors.joining("\n"));

        pages = new ArrayList<>();
        int pageSize = 1000; // 每页字符数
        int start = 0;
        while (start < content.length()) {
            int end = Math.min(start + pageSize, content.length());
            if (end < content.length() && content.charAt(end) != ' ') {
                int wordEnd = content.indexOf(' ', end);
                if (wordEnd != -1) {
                    end = wordEnd;
                }
            }
            pages.add(content.substring(start, end));
            start = end;
        }
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//        String content = reader.lines().collect(Collectors.joining("\n"));
//        pages = new ArrayList<>();
//        int pageSize = 1000; // 每页字符数
//        int start = 0;
//        while (start < content.length()) {
//            int end = Math.min(start + pageSize, content.length());
//            // 查找最近的段落边界
//            if (end < content.length() && content.charAt(end) != '\n') {
//                int paragraphEnd = content.indexOf('\n', end);
//                if (paragraphEnd != -1) {
//                    end = paragraphEnd;
//                }
//            }
//            // 如果段落边界在当前页内，则使用段落边界
//            if (end < content.length() && content.charAt(end) == '\n') {
//                end++; // 包含换行符
//            } else {
//                // 否则，查找最近的单词边界
//                if (end < content.length() && content.charAt(end) != ' ') {
//                    int wordEnd = content.indexOf(' ', end);
//                    if (wordEnd != -1) {
//                        end = wordEnd;
//                    }
//                }
//            }
//            pages.add(content.substring(start, end));
//            start = end;
//        }

    }

    private void showPage(int pageIndex){
        if (pageIndex >= 0 && pageIndex < pages.size()) {
            this.index=pageIndex;
            String highlightedPage = highlightWords(pages.get(pageIndex), collectedWords);
            contentView.getEngine().loadContent(highlightedPage);
        }
    }

    private VBox createPage(int pageIndex){
        VBox pageBox = new VBox(10);
        pageBox.setAlignment(Pos.CENTER);
        showPage(pageIndex);
        return pageBox;
    }

    private String highlightWords(String content, List<String> wordsToHighlight) {
        StringBuilder textBuilder = new StringBuilder();
        String[] paragraphs = content.split("\\R"); // 使用正则表达式分割段落

        for (String paragraph : paragraphs) {
            String[] words = paragraph.split("\\s+"); // 分割段落中的单词

            for (String word : words) {
                if (wordsToHighlight.contains(word)) {
                    textBuilder.append("<span style='color: green;text-decoration:underline;'>")
                            .append(word)
                            .append("</span> ");
                } else {
                    textBuilder.append(word).append(" ");
                }
            }

            textBuilder.append("<br>"); // 在每个段落之间添加换行符
        }

        return textBuilder.toString().trim(); // 去除最后的换行符
    }

}


