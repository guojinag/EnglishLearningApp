package com.englishlearningapp.dao;

import com.englishlearningapp.model.QuestionData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    private Connection connection = null;
    public QuestionDAO(){
//        try {
//            // 加载SQLite驱动程序
//            Class.forName("org.sqlite.JDBC");
//            // 创建数据库连接
//            String DBPath= String.valueOf(getClass().getResource("/questions/SmartReadAI.db"));
//            DBPath=DBPath.substring(5,DBPath.length());
//            //System.out.println("jdbc:sqlite:"+DBPath);
//            connection = DriverManager.getConnection("jdbc:sqlite:"+DBPath);
//            //connection = DriverManager.getConnection("jdbc:sqlite:"+getClass().getResource("SmartReadAI.db"));
//            //connection = DriverManager.getConnection("jdbc:sqlite:E:\\javaProject\\English-Helper\\src\\main\\resources\\SmartReadAI.db");
//            //System.out.println("成功连接到question表！");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        try {
            Class.forName("org.sqlite.JDBC");

            // 获取数据库文件的输入流
            String url = "jdbc:sqlite::resource:" + getClass().getResource("/SmartReadAI.db").toString();

            // 连接到数据库
            connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database connection", e);
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
                    list.add(new QuestionData(id,type,relatedBook,module,question,answer,collect));
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

    public List<QuestionData> selectQuestion(List<String> books,boolean needCollect){
        List<QuestionData> list = new ArrayList<>();
        String value=String.join(",", books.stream().map(String::valueOf).toArray(String[]::new));
        String sql = "SELECT * FROM questions WHERE related_book IN (";
        for (int i = 0; i < books.size(); i++) {
            sql += "?";
            if (i < books.size() - 1) {
                sql += ",";
            }
        }
        sql += ")";
        if(needCollect){
            sql=sql+" AND collect=1";
        }
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < books.size(); i++) {
                pstmt.setString(i + 1, books.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("question_id");
                    String type = rs.getString("type");
                    String module = rs.getString("AI_module");
                    String question = rs.getString("question");
                    String answer = rs.getString("answer");
                    String relatedBook = rs.getString("related_book");
                    int collect=rs.getInt("collect");
                    list.add(new QuestionData(id,type,relatedBook,module,question,answer,collect));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }
}
