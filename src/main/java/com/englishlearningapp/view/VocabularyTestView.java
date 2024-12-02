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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VocabularyTestView extends BorderPane {

    private HBox addBox;
    private TextField addField;
    private Button addButton;
    private VBox historyBox;
    private VocabularyDAO vocabularyDAO;
    private ScrollPane historyScrollPane;
    private int parameter;
    private Arc fanShape;
    private BorderPane fanPane;
    private StackPane fanStackPane;
    private Label fanLabel;
    private BorderPane fanLabelPane;

    public VocabularyTestView(){
        vocabularyDAO = new VocabularyDAO();
        addField = new TextField();
        addField.setPromptText("你的词汇量是");
        addButton = new Button("记录");
        addButton.setOnAction(event -> addVocabulary());


        Button button = new Button("点击开始词汇测试");
        button.setOnAction(e -> {
            try {
                openWebsite("https://preply.com/en/learn/english/test-your-vocab?msockid=2d4f837f09f76dfa2c2697fe08c16ce6");
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        });
        button.setAlignment(Pos.CENTER);
        addBox = new HBox(button,addField, addButton);
        this.setTop(addBox);
        addBox.setAlignment(Pos.CENTER);
        addBox.setSpacing(10);

        historyBox = new VBox();
        historyBox.setSpacing(5);
        historyBox.setPadding(new Insets(10));
        showVocabulary();
        historyScrollPane = new ScrollPane();
        historyScrollPane.setContent(historyBox);
        historyBox.setAlignment(Pos.CENTER);
        //this.setRight(historyScrollPane);


        fanPane = new BorderPane();
        fanLabelPane = new BorderPane();
        showFanShape();

        fanLabel=new Label(parameter+"/10000");
        fanLabel.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;-fx-color: yellow;");
        fanLabel.setAlignment(Pos.CENTER);
        fanLabelPane.setCenter(fanLabel);

        fanStackPane = new StackPane(fanPane,fanLabelPane);
//        fanBox=new VBox(fanShape,fanLabel);
//        fanBox.setAlignment(Pos.CENTER);
//        fanPane.setCenter(fanBox);
        SplitPane splitPane=new SplitPane(fanStackPane, historyScrollPane);
        splitPane.setDividerPositions(0.8);
        this.setCenter(splitPane);
    }

    private void showFanShape(){
        double angle = calculateAngle(parameter);
        fanShape = new Arc(0, 0, 150, 150, 0, angle);
        //System.out.println(fanPane.getWidth()+" "+fanPane.getHeight());
        fanShape.setType(ArcType.OPEN);
        //fanShape.setFill(Color.BLUE);
        fanShape.setFill(Color.TRANSPARENT);
        fanShape.setStroke(Color.rgb(250,150,37));
        fanShape.setStrokeWidth(20);
        fanPane.setCenter(fanShape);
        fanLabel=new Label(parameter+"/10000");
        fanLabel.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;-fx-color: yellow;");
        fanLabel.setAlignment(Pos.CENTER);
        fanLabelPane.setCenter(fanLabel);

    }

    private double calculateAngle(int parameter) {
        return (parameter / 10000.0) * 360;
    }

    private void openWebsite(String url) throws IOException, URISyntaxException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            System.out.println("Desktop browsing is not supported.");
        }
    }

    private void showVocabulary(){
        List<VocabularyData> list=vocabularyDAO.selectAll(10);
        if(list.size()!=0){
            parameter=list.get(0).getVocabulary();
        }else{
            parameter=0;
        }

        historyBox.getChildren().clear();
        for(VocabularyData vocabularyData:list){
            Label date=new Label(vocabularyData.getDate());
            date.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;-fx-text-alignment: left;");
            Label vocabularyNum=new Label(vocabularyData.getVocabulary()+"");
            vocabularyNum.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-alignment: left;");
            date.setAlignment(Pos.CENTER);
            vocabularyNum.setAlignment(Pos.CENTER);
            HBox hBox=new HBox(vocabularyNum,date);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setSpacing(10);
            historyBox.getChildren().add(hBox);
        }
    }

    private void addVocabulary(){
        if(isInteger(addField.getText())){
            int vocabulary=Integer.parseInt(addField.getText());
            if(vocabulary>=0){
                LocalDateTime now=LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd ");
                VocabularyData vocabularyData=new VocabularyData(vocabulary,now.format(formatter));
                vocabularyDAO.save(vocabularyData);
                parameter=vocabulary;
                showFanShape();
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
