package com.englishlearningapp.dao;

import com.englishlearningapp.model.VocabularyData;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VocabularyDAO {

    private Connection connection;


    public VocabularyDAO(){
//        try {
//            // 加载SQLite驱动程序
//            Class.forName("org.sqlite.JDBC");
//
//            // 创建数据库连接
//            String DBPath= String.valueOf(getClass().getResource("/SmartReadAI.db"));
//            DBPath=DBPath.substring(5,DBPath.length());
//            //System.out.println("jdbc:sqlite:"+DBPath);
//            connection = DriverManager.getConnection("jdbc:sqlite:"+DBPath);
//            //connection = DriverManager.getConnection("jdbc:sqlite:"+getClass().getResource("SmartReadAI.db"));
//            //connection = DriverManager.getConnection("jdbc:sqlite:E:\\javaProject\\English-Helper\\src\\main\\resources\\SmartReadAI.db");
//
//            //statement = connection.createStatement();
//
//            //System.out.println("成功连接到vocabulary数据库！");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        try {
            // 使用 ClassLoader 获取资源文件的路径
            URL dbUrl = getClass().getResource("/SmartReadAI.db");
            if (dbUrl == null) {
                throw new IllegalStateException("Database file not found");
            }
            String dbPath = Paths.get(dbUrl.toURI()).toString();
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            int i=0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("关闭数据库连接");
    }

    public void save(VocabularyData vocabulary) {
        String insertSQL = "INSERT INTO vocabulary (vocabulary, date) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setInt(1, vocabulary.getVocabulary());
            statement.setString(2, vocabulary.getDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println("插入词汇数据成功！"+vocabulary);
    }

    public List<VocabularyData> selectAll(int count){
        List<VocabularyData> vocabularyList = new ArrayList<>();
        String selectSQL = "SELECT * FROM vocabulary order by id desc";

        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()&&count>0) {
                    int vocabulary= rs.getInt("vocabulary");
                    String date = rs.getString("date");
                    vocabularyList.add(new VocabularyData(vocabulary,date));
                    count--;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vocabularyList;
    }
}
