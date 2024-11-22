package com.englishlearningapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        String pa= String.valueOf(getClass().getResource("/fxml/MainView.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        BorderPane root = loader.load();
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("英语学习助手");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        // 获取数据库文件路径
//        URL dbUrl = Main.class.getClassLoader().getResource("SmartReadAI.db");
//        if (dbUrl == null) {
//            System.out.println("数据库文件不存在！");
//            return;
//        }
//
//        // 创建 DatabaseManager 实例
//        DatabaseManager dbManager = new DatabaseManager(dbUrl.getFile());
//
//        // 插入所有 txt 文件到数据库
//        TxtUtil.insertAllTxt(dbManager);
//
//        // 关闭数据库连接
//        dbManager.closeConnection();
    }

}
