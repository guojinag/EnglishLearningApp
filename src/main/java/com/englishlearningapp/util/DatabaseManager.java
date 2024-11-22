package com.englishlearningapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {

    private Connection connection;

    // 构造方法，创建与数据库的连接
    public DatabaseManager(String dbPath) {
        try {
            // 加载 SQLite JDBC 驱动
            Class.forName("org.sqlite.JDBC");
            // 创建数据库连接
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            System.out.println("数据库连接成功！");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // 插入书籍数据
    public void insertBook(String bookName, String bookContent, String bookCover) {
        String sql = "INSERT INTO books (book_name, book_content, book_cover) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, bookName);
            pstmt.setString(2, bookContent);
            pstmt.setString(3, bookCover);
            pstmt.executeUpdate();
            System.out.println("书籍 " + bookName + " 插入成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 关闭数据库连接
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("数据库连接已关闭！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
