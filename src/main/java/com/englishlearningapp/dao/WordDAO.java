package com.englishlearningapp.dao;

import com.englishlearningapp.model.WordData;

import java.sql.*;
import java.util.List;

public class WordDAO {

    private Connection connection;
    private Statement statement;


    public WordDAO(){
        try {
            // 加载SQLite驱动程序
            Class.forName("org.sqlite.JDBC");

            // 创建数据库连接
            connection = DriverManager.getConnection("jdbc:sqlite:E:\\javaProject\\English-Helper\\src\\main\\resources\\SmartReadAI.db");
            //statement = connection.createStatement();

            System.out.println("成功连接到SQLite数据库！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void save(List<WordData> list) throws SQLException {
        String insertSQL = "INSERT INTO words (word, phonetic, explanation, COBUILD_star) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            for (WordData word : list) {
                statement.setString(1, word.getWord());
                statement.setString(2, word.getPhonetic());
                statement.setString(3, word.getExplanation());
                statement.setString(4, "");
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void closeConnection() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
