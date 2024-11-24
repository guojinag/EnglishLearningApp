package com.englishlearningapp.dao;

import com.englishlearningapp.model.QuestionData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class QuestionDAO {
    private Connection connection = null;
    public QuestionDAO(){
        try {
            // 加载SQLite驱动程序
            Class.forName("org.sqlite.JDBC");

            // 创建数据库连接
            //connection = DriverManager.getConnection("jdbc:sqlite:"+getClass().getResource("SmartReadAI.db"));
            connection = DriverManager.getConnection("jdbc:sqlite:E:\\javaProject\\English-Helper\\src\\main\\resources\\SmartReadAI.db");

            //statement = connection.createStatement();

            System.out.println("成功连接到SQLite数据库！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(List<QuestionData> list){
        String sql = "INSERT INTO questions (type, AI_module, question, answer, related_book) VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            for (QuestionData questionData : list) {
                pstmt.setString(1, questionData.getType());
                pstmt.setString(2, questionData.getAiModule());
                pstmt.setString(3, questionData.getQuestion());
                pstmt.setString(4, questionData.getAnswer());
                pstmt.setString(5, questionData.getRelatedBook());
                pstmt.addBatch(); // 添加到批处理
            }

            pstmt.executeBatch(); // 执行批处理
            //System.out.println("Data saved successfully.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
