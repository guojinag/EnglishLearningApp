package com.englishlearningapp;

import com.englishlearningapp.view.BookReaderView;
import com.englishlearningapp.view.TestView;
import com.englishlearningapp.view.VocabularySearchView;
import com.englishlearningapp.view.VocabularyTestView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建一个BorderPane布局
        BorderPane root = new BorderPane();

        Label mainlabel = new Label("Welcome to English Helper");
        root.setCenter(mainlabel);
        mainlabel.setAlignment(Pos.CENTER);

        // 创建三个按钮
        Button bookReaderButton = new Button("书籍阅读");
        Button vocabularySearchButton = new Button("词汇查找");
        Button testButton = new Button("题目测试");
        Button vocabularyTestButton = new Button("词汇量测试");

        // 创建一个HBox布局，并将按钮添加到HBox中
        HBox buttonBox = new HBox(10, bookReaderButton, vocabularySearchButton, testButton, vocabularyTestButton);
        buttonBox.setPrefWidth(Double.MAX_VALUE); // 使HBox的宽度适应窗口大小
        // 将HBox添加到BorderPane的底部区域
        root.setBottom(buttonBox);
        buttonBox.setAlignment(Pos.CENTER);
        // 设置按钮的点击事件
        bookReaderButton.setOnAction(event -> showBookReaderView(root));
        vocabularySearchButton.setOnAction(event -> showVocabularySearchView(root));
        testButton.setOnAction(event -> showTestView(root));
        vocabularyTestButton.setOnAction(event -> showVocabularyTestView(root));
        root.setId("root");
        URL cssurl = getClass().getResource("/css/mainView.css");
        Scene scene = new Scene(root, 800, 600);

        scene.getStylesheets().add(cssurl.toExternalForm());
        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.setTitle("English Learning App");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/sky.png/")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showBookReaderView(BorderPane root) {
        root.setCenter(new BookReaderView());
    }

    private void showVocabularySearchView(BorderPane root) {
        root.setCenter(new VocabularySearchView());
    }

    private void showTestView(BorderPane root) {
        root.setCenter(new TestView());
    }

    private void showVocabularyTestView(BorderPane root) {
        root.setCenter(new VocabularyTestView());
    }

    public static void main(String[] args) {
        launch(args);
        //String fileName = "D:\\Users\\kevin\\Desktop\\资源统筹\\2024下\\java\\english\\数据\\题目+答案完整版.xlsx";
        //EasyExcel.read(fileName, QuestionData.class, new easyExcelUtil.DemoDataListener()).sheet().doRead();
    }
}

