package com.englishlearningapp.dao;

import com.englishlearningapp.model.WordData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WordDAO {

    private Connection connection;
    private Statement statement;


    public WordDAO(){
        try {
            // 加载SQLite驱动程序
            Class.forName("org.sqlite.JDBC");

            // 创建数据库连接
            String DBPath= String.valueOf(getClass().getResource("/SmartReadAI.db"));
            DBPath=DBPath.substring(5,DBPath.length());
            //System.out.println("jdbc:sqlite:"+DBPath);
            connection = DriverManager.getConnection("jdbc:sqlite:"+DBPath);
            //connection = DriverManager.getConnection("jdbc:sqlite:"+getClass().getResource("SmartReadAI.db"));
            //connection = DriverManager.getConnection("jdbc:sqlite:E:\\javaProject\\English-Helper\\src\\main\\resources\\SmartReadAI.db");

            System.out.println("成功连接到word数据库！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        System.out.println("关闭数据库连接");
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

    public List<WordData> selectWordByWord(String word) throws SQLException {
        List<WordData> list = new ArrayList<>();
        String sql = "SELECT word, phonetic, explanation, COBUILD_star, collect FROM words WHERE word LIKE ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // 使用百分号进行模糊查询
            pstmt.setString(1, "%" + word + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String wordResult = rs.getString("word");
                    String phonetic = rs.getString("phonetic");
                    String explanation = rs.getString("explanation");
                    String cobuildStar = rs.getString("COBUILD_star");
                    int collect = rs.getInt("collect");
                    list.add(new WordData(wordResult, phonetic, explanation,cobuildStar,collect));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public void collectWord(String word,int isCollect) throws SQLException {
        String sql = "UPDATE words SET collect=? WHERE word=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, isCollect);
            pstmt.setString(2, word);
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("修改收藏成功");

    }

    public List<WordData> selectWordCollect() throws SQLException {
        List<WordData> list = new ArrayList<>();
        String sql = "SELECT word, phonetic, explanation, COBUILD_star, collect FROM words WHERE collect = 1;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String wordResult = rs.getString("word");
                    String phonetic = rs.getString("phonetic");
                    String explanation = rs.getString("explanation");
                    String cobuildStar = rs.getString("COBUILD_star");
                    int collect = rs.getInt("collect");
                    list.add(new WordData(wordResult, phonetic, explanation,cobuildStar,collect));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }
}
