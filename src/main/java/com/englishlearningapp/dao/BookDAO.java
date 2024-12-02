package com.englishlearningapp.dao;

import com.englishlearningapp.model.BookData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection connection;
    public BookDAO() {
//        try {
//            // 加载SQLite驱动程序
//            Class.forName("org.sqlite.JDBC");
//            // 创建数据库连接
//            String DBPath= String.valueOf(getClass().getResource("/questions/SmartReadAI.db"));
//            DBPath=DBPath.substring(5,DBPath.length());
//            //System.out.println("jdbc:sqlite:"+DBPath);
//            connection = DriverManager.getConnection("jdbc:sqlite:"+DBPath);
//            //connection = DriverManager.getConnection("jdbc:sqlite:E:\\javaProject\\English-Helper\\src\\main\\resources\\SmartReadAI.db");
//            //System.out.println("成功连接到book数据库！");
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
    public int selectBookByTitle(String title) {
        String sql = "select * from books where book = ? ";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, title);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) {
                    return rs.getInt("book_index");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10;
    }
    public void updateBookIndex(String title,int bookIndex) {
        String sql = "update books set book_index = ? where book = ?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setInt(1, bookIndex);
            ps.setString(2, title);
            ps.executeUpdate();
            //System.out.println(title+bookIndex);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<BookData> selectAllBooks() {
        List<BookData> books = new ArrayList<>();
        String sql = "select * from books";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()) {
                    String title = rs.getString("book");
                    int bookIndex = rs.getInt("book_index");
                    BookData bookData=new BookData(title,bookIndex);
                    books.add(bookData);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return books;
    }
}
