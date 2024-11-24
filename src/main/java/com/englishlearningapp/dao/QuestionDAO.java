package com.englishlearningapp.dao;

import com.englishlearningapp.model.QuestionData;

import java.sql.*;
import java.util.ArrayList;
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
            System.out.println("成功连接到SQLite数据库的question表！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
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

    public List<String> getBooks(){
        List<String> list = new ArrayList<>();
        String sql = "select related_book from questions group by related_book";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String book = rs.getString("related_book");
                    list.add(book);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public List<QuestionData> selectAll(){
        List<QuestionData> list = new ArrayList<>();
        String sql = "SELECT * FROM questions";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("question_id");
                    String type = rs.getString("type");
                    String module = rs.getString("AI_module");
                    String question = rs.getString("question");
                    String answer = rs.getString("answer");
                    String relatedBook = rs.getString("related_book");
                    int collect=rs.getInt("collect");
                    list.add(new QuestionData(id,type,module,relatedBook,question,answer,collect));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public void collectQuestion(QuestionData questionData){
        String sql = "update questions set collect=? where question_id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1,questionData.getIsCollected()==0?1:0);
            pstmt.setInt(2,questionData.getId());
            pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
