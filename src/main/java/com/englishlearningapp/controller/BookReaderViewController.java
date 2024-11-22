package com.englishlearningapp.controller;

import com.englishlearningapp.view.BookContentView;
import com.englishlearningapp.view.BookReaderView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookReaderViewController{

    @FXML
    private BorderPane subPane;
    @FXML
    private GridPane bookGrid;

    @FXML
    public void initialize() {
        // 初始化书籍网格
//        bookGrid.setHgap(10); // 设置水平间距
//        bookGrid.setVgap(10); // 设置垂直间距

        // 读取books目录下的所有txt文件
        List<String> bookFiles = getBookFiles();

        // 为每个txt文件创建一个按钮和图片
        int row = 0;
        int col = 0;
        for (String bookFile : bookFiles) {
            String fileName = getFileName(bookFile);
            String imageFileName = fileName.replace(".txt", ".png"); // 假设图片格式为png

            // 创建图片视图
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/" + imageFileName)));
            imageView.setFitWidth(62); // 设置图片宽度
            imageView.setFitHeight(100); // 设置图片高度

            // 创建按钮
            Button bookButton = new Button(fileName);
            bookButton.setOnAction(event -> {
                try {
                    showBookContentView(bookFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // 创建VBox来容纳图片和按钮
            VBox bookItem = new VBox(10, imageView, bookButton);
            bookItem.setAlignment(Pos.CENTER); // 设置对齐方式

            // 将VBox添加到网格中
            bookGrid.add(bookItem, col, row);

            // 更新行列索引
            col++;
            if (col >= 4) {
                col = 0;
                row++;
            }
        }
    }

    private List<String> getBookFiles() {
        // 获取books目录下的所有txt文件
        InputStream inputStream = getClass().getResourceAsStream("/books/");
        if (inputStream == null) {
            System.err.println("无法找到books目录");
            return new ArrayList<>();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return reader.lines()
                .filter(line -> line.endsWith(".txt"))
                .collect(Collectors.toList());
    }

    private String getFileName(String filePath) {
        // 从文件路径中提取文件名
        int lastSlashIndex = filePath.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < filePath.length() - 1) {
            return filePath.substring(lastSlashIndex + 1);
        }
        return filePath;
    }

    private void showBookContentView(String bookFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BookReaderView.fxml"));
        BorderPane BookReaderView= loader.load();
        // 创建并显示书籍内容界面
        BookContentView bookContentView = new BookContentView(new BookReaderView(), bookFile);
//        bookGrid.getChildren().clear();
//        bookGrid.getChildren().add(bookContentView);
        subPane.setCenter(bookContentView);
    }


    public void onBackButtonClicked() {
        // 重新显示书籍阅读界面
        initialize();
    }
}
