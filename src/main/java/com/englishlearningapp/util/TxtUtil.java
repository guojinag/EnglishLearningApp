package com.englishlearningapp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class TxtUtil {

    // 插入所有 txt 文件到数据库
    public static void insertAllTxt(DatabaseManager dbManager) {
        // 获取 resources/books 目录下的所有文件
        URL booksDir = TxtUtil.class.getClassLoader().getResource("books");
        if (booksDir == null) {
            System.out.println("books 目录不存在！");
            return;
        }

        File[] files = new File(booksDir.getFile()).listFiles();
        if (files == null) {
            System.out.println("books 目录为空！");
            return;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                String bookName = file.getName().replace(".txt", "");
                String bookContent = readTxtFile(file);
                String bookCover = getBookCoverUrl(bookName);

                // 插入书籍数据到数据库
                dbManager.insertBook(bookName, bookContent, bookCover);
            }
        }
    }

    // 读取 txt 文件内容
    private static String readTxtFile(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    // 获取书籍封面 URL
    private static String getBookCoverUrl(String bookName) {
        // 假设封面图片与 txt 文件同名，且位于 resources/images 目录下
        URL coverUrl = TxtUtil.class.getClassLoader().getResource("images/" + bookName + ".jpg");
        return coverUrl != null ? coverUrl.toString() : null;
    }
}


