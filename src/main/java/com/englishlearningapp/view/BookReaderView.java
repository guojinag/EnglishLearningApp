package com.englishlearningapp.view;

import com.englishlearningapp.dao.BookDAO;
import com.englishlearningapp.model.BookData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookReaderView extends BorderPane {


    private ScrollPane mainScrollPane;
    private GridPane bookGrid;
    private BookDAO bookDAO;

    public BookReaderView() {
        // 初始化书籍网格
        bookGrid = new GridPane();
        bookGrid.setPadding(new Insets(10));
        bookGrid.setHgap(10);
        bookGrid.setVgap(10);
        bookDAO=new BookDAO();

        for (int i = 0; i < 3; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / 3);
            bookGrid.getColumnConstraints().add(columnConstraints);
        }
        // 读取book,创建网格内容
        //List<String> bookFiles = getBookFiles();
        List<BookData> list=bookDAO.selectAllBooks();
        int row = 0;
        int col = 0;
        for (BookData bookFile : list) {
            String fileName = bookFile.getBook();
            //String imageFileName = fileName.replace(".txt", ".png");

            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/book/" + fileName+".png")));
            imageView.setFitWidth(62);
            imageView.setFitHeight(100);

            Button bookButton = new Button(fileName);
            bookButton.setOnAction(event -> {
                showBookContentView(bookFile.getBook());
            });

            VBox bookItem = new VBox(10, imageView, bookButton);
            bookItem.setAlignment(Pos.CENTER);
            bookGrid.add(bookItem, col, row);

            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }

        mainScrollPane = new ScrollPane(bookGrid);
        mainScrollPane.setFitToWidth(true);


        this.setCenter(bookGrid);
    }

    private List<String> getBookFiles() {
        // 获取books目录下的所有txt文件
        InputStream inputStream = getClass().getResourceAsStream("/books/");
        if (inputStream == null) {
            System.err.println("无法找到books目录");
            return new ArrayList<>();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return reader.lines()
                .filter(line -> line.endsWith(".txt"))
                .collect(Collectors.toList());
    }


    private String getFileName(String filePath) {
        // 从文件路径中提取文件名
        int lastSlashIndex = filePath.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < filePath.length() - 1) {
            return filePath.substring(lastSlashIndex + 1);
        }
        return filePath;
    }

    private void showBookContentView(String bookFile) {
        BookContentView bookContentView = new BookContentView(this, bookFile);
        this.setCenter(bookContentView);
    }

    public void showBookReaderView() {
        this.setCenter(bookGrid);
    }
}

