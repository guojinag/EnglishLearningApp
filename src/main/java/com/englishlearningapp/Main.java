package com.englishlearningapp;

import com.englishlearningapp.view.BookReaderView;
import com.englishlearningapp.view.TestView;
import com.englishlearningapp.view.VocabularySearchView;
import com.englishlearningapp.view.VocabularyTestView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main extends Application {

    private BookReaderView bookReaderView;
    private VocabularySearchView vocabularySearchView;
    private TestView testView;
    private VocabularyTestView vocabularyTestView;

    @Override
    public void start(Stage primaryStage) {
        bookReaderView = new BookReaderView();
        vocabularySearchView = new VocabularySearchView();
        vocabularyTestView = new VocabularyTestView();
        testView = new TestView();
        //VScene vScene=new VScene(new Label("vUI"));
//        VStage stage = new VStage();
//        stage.show();
        // 中部窗口及根节点
        BorderPane root = new BorderPane();

        // 底部菜单选项
        Button bookReaderButton = new Button("书籍阅读");
        Button vocabularySearchButton = new Button("词汇查找");
        Button testButton = new Button("题目测试");
        Button vocabularyTestButton = new Button("词汇量测试");
        HBox buttonBox = new HBox(10, bookReaderButton, vocabularySearchButton, testButton, vocabularyTestButton);
        buttonBox.setPrefWidth(Double.MAX_VALUE);
        root.setBottom(buttonBox);
        buttonBox.setAlignment(Pos.CENTER);
        bookReaderButton.setOnAction(event -> showBookReaderView(root));
        vocabularySearchButton.setOnAction(event -> showVocabularySearchView(root));
        testButton.setOnAction(event -> showTestView(root));
        vocabularyTestButton.setOnAction(event -> showVocabularyTestView(root));
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setVisible(false);

        //好句label
        String pa= getClass().getResource("/sentence.txt").toString();
        pa=pa.substring(5,pa.length());
        String sentence=getRandomLine(pa);
        Label mainlabel = new Label(sentence);
        mainlabel.setId("sentence");
        mainlabel.setAlignment(Pos.CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setAlignment(Pos.CENTER);

        Button button1 = new Button();
        button1.setId("mainViewButton");
        button1.setGraphic(new Label("阅读"));
        button1.setStyle("-fx-background-image: url('" + getClass().getResource("/images/button/read.png").toExternalForm() + "'); ");
        button1.setOnAction(event -> {
            buttonBox.setVisible(true);
            showBookReaderView(root);
        });
        Button button2 = new Button();
        button2.setId("mainViewButton");
        button2.setGraphic(new Label("词汇"));
        button2.setStyle("-fx-background-image: url('" + getClass().getResource("/images/button/search.png").toExternalForm() + "'); ");
        button2.setOnAction(event -> {
            buttonBox.setVisible(true);
            showVocabularySearchView(root);
        });
        Button button3 = new Button();
        button3.setId("mainViewButton");
        button3.setGraphic(new Label("测试"));
        button3.setStyle("-fx-background-image: url('" + getClass().getResource("/images/button/question.png").toExternalForm() + "'); ");
        button3.setOnAction(event -> {
            buttonBox.setVisible(true);
            showTestView(root);
        });
        Button button4 = new Button();
        button4.setId("mainViewButton");
        button4.setGraphic(new Label("记录"));
        button4.setStyle("-fx-background-image: url('" + getClass().getResource("/images/button/record.png").toExternalForm() + "'); ");
        button4.setOnAction(event -> {
            buttonBox.setVisible(true);
            showVocabularyTestView(root);
        });

        gridPane.add(button1, 0, 0);
        gridPane.add(button2, 1, 0);
        gridPane.add(button3, 0, 1);
        gridPane.add(button4, 1, 1);
        BorderPane mainBox = new BorderPane();
        mainBox.setCenter(gridPane);
        mainBox.setTop(mainlabel);
        mainBox.setPadding(new Insets(20, 20, 20, 20));
        root.setCenter(mainBox);



        //绑定css，设置样式
        Random rand = new Random();
        int num=rand.nextInt(20)+1001;
        Image image = new Image(getClass().getResourceAsStream("/images/background/"+num+".png"));
        Background background = setBackground(image);
        root.setId("root");
        URL cssurl = getClass().getResource("/css/mainView.css");
        root.setBackground(background);
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(cssurl.toExternalForm());

        //设置窗口
        primaryStage.setTitle("Smart Read AI");
        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/sky.png")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public String getRandomLine(String filePath) {
        List<String> lines = new ArrayList<>();
        Random random = new Random();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lines.isEmpty()) {
            return null;
        }

        int randomIndex = random.nextInt(lines.size());
        return lines.get(randomIndex);
    }

    private Background setBackground(Image image) {
        BackgroundSize backgroundSize = new BackgroundSize(
                BackgroundSize.AUTO, // 宽度
                BackgroundSize.AUTO, // 高度
                true,                // 是否保持宽高比
                true,                // 是否保持宽高比
                true,                // 是否填满宽度
                true                 // 是否填满高度
        );

        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                backgroundSize
        );
        Background background = new Background(backgroundImage);
        return background;
    }

    private void showBookReaderView(BorderPane root) {
        root.setCenter(bookReaderView);
    }

    private void showVocabularySearchView(BorderPane root) {
        root.setCenter(vocabularySearchView);
    }

    private void showTestView(BorderPane root) {
        root.setCenter(testView);
    }

    private void showVocabularyTestView(BorderPane root) {
        root.setCenter(vocabularyTestView);
    }

    public static void main(String[] args) {
        launch(args);
        //String fileName = "D:\\Users\\kevin\\Desktop\\资源统筹\\2024下\\java\\english\\数据\\题目+答案完整版.xlsx";
        //EasyExcel.read(fileName, QuestionData.class, new easyExcelUtil.DemoDataListener()).sheet().doRead();

//        try {
//            // 指定 MDX 文件的路径
//            File mdxFile = new File("path/to/your/dictionary.mdx");
//
//            // 创建 MdictReader 对象
//            MdictReader reader = new MdictReader(mdxFile);
//
//            // 读取 MDX 文件的头部信息
//            MdxHead head = reader.readHead();
//            System.out.println("Dictionary Name: " + head.getName());
//            System.out.println("Description: " + head.getDescription());
//
//            // 查询一个词条
//            String word = "example";
//            List<MdxItem> entries = reader.lookup(word);
//
//            // 输出查询结果
//            for (MdxItem entry : entries) {
//                System.out.println("Word: " + entry.getKey());
//                System.out.println("Definition: " + entry.getValue());
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

