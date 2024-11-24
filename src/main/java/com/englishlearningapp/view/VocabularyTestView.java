package com.englishlearningapp.view;

import com.englishlearningapp.dao.VocabularyDAO;
import com.englishlearningapp.model.VocabularyData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

public class VocabularyTestView extends BorderPane {

    private HBox addBox;
    private TextField addField;
    private Button addButton;
    private VBox historyBox;
    private VocabularyDAO vocabularyDAO;
    private ScrollPane historyScrollPane;

    public VocabularyTestView(){
        vocabularyDAO = new VocabularyDAO();
        addField = new TextField();
        addField.setPromptText("你的词汇量是");
        addButton = new Button("记录");
        addButton.setOnAction(event -> addVocabulary());
        addBox = new HBox(addField, addButton);
        this.setTop(addBox);
        addBox.setAlignment(Pos.CENTER);

        Button button = new Button("点击开始词汇测试");
        button.setOnAction(e -> {
            try {
                openWebsite("https://preply.com/en/learn/english/test-your-vocab?msockid=2d4f837f09f76dfa2c2697fe08c16ce6");
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        });
        this.setRight(button);
        button.setAlignment(Pos.CENTER);


        historyBox = new VBox();
        historyBox.setSpacing(5);
        historyBox.setPadding(new Insets(10));
        showVocabulary();
        historyScrollPane = new ScrollPane();
        historyScrollPane.setContent(historyBox);
        this.setCenter(historyScrollPane);
    }

    private void openWebsite(String url) throws IOException, URISyntaxException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            System.out.println("Desktop browsing is not supported.");
        }
    }

    private void showVocabulary(){
        List<VocabularyData> list=vocabularyDAO.selectAll();

        historyBox.getChildren().clear();
        for(VocabularyData vocabularyData:list){
            Label label=new Label(vocabularyData.getDate());
            Label vocabularyNum=new Label(vocabularyData.getVocabulary()+"");
            HBox hBox=new HBox(vocabularyNum,label);
            historyBox.getChildren().add(hBox);
        }
    }
    private void addVocabulary(){
        if(isInteger(addField.getText())){
            int vocabulary=Integer.parseInt(addField.getText());
            if(vocabulary>=0){
                LocalDateTime now=LocalDateTime.now();
                VocabularyData vocabularyData=new VocabularyData(vocabulary,now.toString());
                vocabularyDAO.save(vocabularyData);
                showVocabulary();
                return;
            }
        }
        Stage stage=new Stage();
        Scene scene=new Scene(new Label("请输入非负整数！"));
        stage.setScene(scene);
        stage.show();
    }
    private boolean isInteger(String input) {
        try {
            int test=Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }
}
