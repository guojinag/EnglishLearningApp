package com.englishlearningapp.dao;

import java.sql.*;

public class BookDAO {
    private Connection connection;
    public BookDAO() {
        try {
            // 加载SQLite驱动程序
            Class.forName("org.sqlite.JDBC");
            // 创建数据库连接
            String DBPath= String.valueOf(getClass().getResource("/SmartReadAI.db"));
            DBPath=DBPath.substring(5,DBPath.length());
            //System.out.println("jdbc:sqlite:"+DBPath);
            connection = DriverManager.getConnection("jdbc:sqlite:"+DBPath);
            //connection = DriverManager.getConnection("jdbc:sqlite:E:\\javaProject\\English-Helper\\src\\main\\resources\\SmartReadAI.db");
            System.out.println("成功连接到book数据库！");
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
            System.out.println(title+bookIndex);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
