package com.englishlearningapp.view;

import com.englishlearningapp.dao.WordDAO;
import com.englishlearningapp.model.WordData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class VocabularySearchView extends BorderPane {

    private TextField searchField;
    private Button searchButton;
    private HBox searchBar;

    private SplitPane splitPane;
    private BorderPane rightPane;
    private VBox resultBox;
    private ScrollPane resultScrollPane;
    private VBox detailPane;

    private WordDAO wordDAO;

    public VocabularySearchView() {

        setPadding(new Insets(10));

        // 顶部菜单：搜索栏
        searchField = new TextField();
        searchField.setPromptText("Enter a word to search");
        searchField.setPrefWidth(300);
        searchButton = new Button("搜索");
        searchButton.setOnAction(event -> {
            performSearch();
        });
        searchBar = new HBox(searchField, searchButton);
        searchBar.setSpacing(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        setTop(searchBar);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        // 中部区域：搜索结果列表及单词详情界面
        resultBox = new VBox();
        resultBox.setSpacing(5);
        resultBox.setPadding(new Insets(10));

        resultScrollPane = new ScrollPane();
        resultScrollPane.setContent(resultBox);
        rightPane = new BorderPane();
        splitPane = new SplitPane(resultScrollPane, rightPane);
        splitPane.setDividerPositions(0.66);
        // 将结果展示区域添加到 BorderPane 的中心
        setCenter(splitPane);


    }

    private void performSearch()  {
        // 获取搜索词
        String searchTerm = searchField.getText();

        wordDAO = new WordDAO();
        // 这里可以调用数据库查询方法，获取搜索结果
        List<WordData> searchResults = null;
        try {
            searchResults = wordDAO.selectWordByWord(searchTerm);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // 清空结果展示区域
        resultBox.getChildren().clear();

        // 将搜索结果添加到结果展示区域
        for (WordData word : searchResults) {
            Button wordButton = new Button(word.getWord());
            wordButton.setFont(Font.font("Arial", 14));
            wordButton.setTextFill(Color.BLACK);
            wordButton.setPrefWidth(200);
            wordButton.setOnAction(event -> showWordDetails(word));
            resultBox.getChildren().add(wordButton);
        }

        wordDAO.closeConnection();
    }

    private void showWordDetails(WordData word) {
        detailPane = new VBox(10);

        // 创建单词拼写标签，并设置字体大小和加粗
        Label wordLabel = new Label(word.getWord());
        wordLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        // 创建单词音标、释义和星级标签
        Label phoneticLabel = new Label(word.getPhonetic());
        Label explanationLabel = new Label("\n"+word.getExplanation());
        Label starLabel = new Label("柯林斯星级："+word.getCOBUILD_star());

        Button collectButton = new Button(word.getIsCollected() == 0 ? "收藏" : "取消收藏");
        collectButton.setOnAction(event -> {
            collectButton.setText(word.getIsCollected() == 1 ? "收藏" : "取消收藏");
            try {
                setCollected(word);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            word.setIsCollected(word.getIsCollected() == 1 ? 0 : 1);
        });
        // 创建收藏按钮
//        String collectImg= String.valueOf(getClass().getResource("/images/button/collect.svg"));
//        collectImg=collectImg.substring(6,collectImg.length());
//        String uncollectImg= String.valueOf(getClass().getResource("/images/button/uncollect.svg"));
//        uncollectImg=uncollectImg.substring(6,uncollectImg.length());
//        SVGPath collectIcon = new SVGPath();
//        // 读取 SVG 文件内容
//        String collectSvgContent = readSvgContent(COLLECT_SVG_PATH);
//        String uncollectSvgContent = readSvgContent(UNCOLLECT_SVG_PATH);
//        // 设置初始 SVG 内容
//        collectIcon.setContent(collectSvgContent);
//        collectIcon.setScaleX(0.5); // 设置 SVG 图片的缩放比例
//        collectIcon.setScaleY(0.5);
//        collectButton.setGraphic(collectIcon);
//        Button collectButton = new Button();
//        String finalCollectImg = readSvgContent(collectImg);
//        String finalUncollectImg = readSvgContent(uncollectImg);
//        collectIcon.setContent(word.getIsCollected() == 0 ? finalCollectImg : finalUncollectImg);
//        collectIcon.setScaleX(0.5); // 设置 SVG 图片的缩放比例
//        collectIcon.setScaleY(0.5);
//        collectButton.setGraphic(collectIcon);
//        collectButton.setOnAction(event -> {
//            collectIcon.setContent(word.getIsCollected() == 1 ? finalCollectImg : finalUncollectImg);
//            try {
//                setCollected(word);
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//            word.setIsCollected(word.getIsCollected() == 1 ? 0 : 1);
//        });

        detailPane.getChildren().addAll(
                wordLabel,
                phoneticLabel,
                explanationLabel,
                starLabel,
                collectButton
        );
        rightPane.setCenter(detailPane);
    }
    private String readSvgContent(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private void setCollected(WordData word) throws SQLException {
        WordDAO wordDAO = new WordDAO();
        wordDAO.collectWord(word.getWord(),word.getIsCollected()==0?1:0);
    }

}
