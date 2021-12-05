package org.vi1ibus.courseworktimemanagement;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;

import java.io.IOException;

public class StatusListController {

    @FXML
    AnchorPane toolBar;

    @FXML
    MenuButton loginMenu;

    @FXML
    HBox listStatuses;

    Popup popup;

    @FXML
    public void initialize() {
        loginMenu.setText(MainApplication.getUser().getLogin());
        MainApplication.setDraggable(toolBar);
        updateStatusListView();
    }

    public void updateStatusListView(){
        listStatuses.getChildren().clear();
        MainApplication.setCurrentStatuses(ControllerDatabase.getStatuses(MainApplication.getCurrentTaskList().getId()));
        for(String key : MainApplication.getCurrentStatuses().keySet()){
            GridPane gridPane = new GridPane();
            gridPane.setId(key);
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            gridPane.getRowConstraints().add(rowConstraints);
            gridPane.getRowConstraints().add(rowConstraints);
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefHeight(442);
            anchorPane.setPrefWidth(250);
            VBox vBox = new VBox();
            vBox.setFillWidth(true);
            Label nameStatus = new Label(MainApplication.getCurrentStatuses().get(key));
            nameStatus.setAlignment(Pos.CENTER);
            nameStatus.setStyle("-fx-background-color: #5999ff; " +
                    "-fx-background-radius: 30;" );
            nameStatus.setTextAlignment(TextAlignment.CENTER);
            nameStatus.setTextFill(Color.WHITE);
            nameStatus.setWrapText(true);
            nameStatus.setMaxWidth(Double.MAX_VALUE);
            vBox.getChildren().add(nameStatus);
            VBox.setMargin(nameStatus, new Insets(5, 5, 5, 5));
            Label listTasks = new Label("List tasks:");
            listTasks.setTextFill(Color.web("#5999ff"));
            vBox.getChildren().add(listTasks);
            VBox.setMargin(listTasks, new Insets(2.5, 5, 2.5, 5));
            if(ControllerDatabase.getTasksByStatus(key).values() != null) {
                for (Task task : ControllerDatabase.getTasksByStatus(key).values()) {
                    Label nameTask = new Label(task.getName());
                    nameTask.setAlignment(Pos.CENTER);
                    nameTask.setContentDisplay(ContentDisplay.CENTER);
                    nameTask.setStyle("-fx-border-color: #5999ff;");
                    nameTask.setTextAlignment(TextAlignment.JUSTIFY);
                    nameTask.setTextFill(Color.web("#5999ff"));
                    nameTask.setMaxWidth(Double.MAX_VALUE);
                    vBox.getChildren().add(nameTask);
                    VBox.setMargin(nameTask, new Insets(2.5, 5, 5, 2.5));
                }
            }
            AnchorPane.setBottomAnchor(vBox, 0.0);
            AnchorPane.setLeftAnchor(vBox, 0.0);
            AnchorPane.setRightAnchor(vBox, 0.0);
            AnchorPane.setTopAnchor(vBox, 0.0);
            anchorPane.getChildren().add(vBox);
            ScrollPane scrollPane = new ScrollPane(anchorPane);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent; " +
                    "-fx-border-color: #5999ff; " +
                    "-fx-border-width: 3;");
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            gridPane.add(scrollPane, 0, 0);
            AnchorPane controlPanel = new AnchorPane();
            ImageView plus = new ImageView(String.valueOf(MainApplication.class.getResource("img/plus.png")));
            plus.setFitHeight(25);
            plus.setFitWidth(25);
            AnchorPane.setLeftAnchor(plus, 0.0);
            plus.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    popup = new Popup();
                    popup.setAutoHide(true);
                    VBox popupVBox = new VBox();
                    popupVBox.setPrefSize(200, 200);
                    popupVBox.setPadding(new Insets(5,5,5,5));
                    popupVBox.setStyle("-fx-background-color: #5999ff;");
                    Label errMessage = new Label();
                    errMessage.setTextFill(Color.WHITE);
                    errMessage.setAlignment(Pos.CENTER);
                    VBox.setMargin(errMessage, new Insets(2.5,0,0,2.5));
                    TextField nameTask = new TextField();
                    nameTask.setPromptText("Name task");
                    VBox.setMargin(popupVBox, new Insets(2.5, 0, 0,0));
                    TextArea descriptionTask = new TextArea();
                    descriptionTask.setPromptText("Description task");
                    VBox.setMargin(descriptionTask, new Insets(2.5, 0,0,2.5));
                    Button btn = new Button("Create task");
                    VBox.setMargin(btn, new Insets(2.5, 0,0,2.5));
                    btn.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            if(nameTask.getText().isEmpty()) {
                                errMessage.setText("The task name field cannot be empty.");
                            } else {
                                ControllerDatabase.createTask(
                                        MainApplication.getCurrentTaskList().getId(),
                                        nameTask.getText(),
                                        descriptionTask.getText(),
                                        Integer.parseInt(gridPane.getId())
                                );
                                popup.hide();
                                updateStatusListView();
                            }
                        }
                    });
                    btn.setTextAlignment(TextAlignment.CENTER);
                    popupVBox.getChildren().addAll(nameTask, descriptionTask, btn, errMessage);
                    popup.getContent().addAll(popupVBox);
                    Bounds boundsInScreen = plus.localToScreen(plus.getLayoutBounds());
                    popup.setX(boundsInScreen.getMinX());
                    popup.setY(boundsInScreen.getMinY() - 150 - controlPanel.getHeight() * 1.5);
                    popup.show(MainApplication.getStage());
                }
            });
            ImageView minus = new ImageView(String.valueOf(MainApplication.class.getResource("img/minus.png")));
            minus.setFitHeight(25);
            minus.setFitWidth(25);
            minus.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                }
            });
            AnchorPane.setLeftAnchor(minus, 25.0);
            ImageView blueCross = new ImageView(String.valueOf(MainApplication.class.getResource("img/blue-cross.png")));
            blueCross.setFitWidth(20);
            blueCross.setFitHeight(13);
            blueCross.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    ControllerDatabase.deleteStatus(Integer.parseInt(gridPane.getId()));
                    updateStatusListView();
                }
            });
            AnchorPane.setTopAnchor(blueCross, 6.0);
            AnchorPane.setRightAnchor(blueCross, 0.0);
            controlPanel.getChildren().add(plus);
            controlPanel.getChildren().add(minus);
            controlPanel.getChildren().add(blueCross);
            gridPane.add(controlPanel, 0, 1);
            listStatuses.getChildren().add(gridPane);
            HBox.setMargin(gridPane, new Insets(10, 10, 10, 10));
        }
        VBox vBox = new VBox();
        TextArea textArea = new TextArea();
        textArea.setPromptText("Name status");
        textArea.setWrapText(true);
        textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    if(!textArea.getText().isEmpty()) {
                        ControllerDatabase.createStatus(textArea.getText());
                        updateStatusListView();
                    }
                }
            }
        });
        vBox.getChildren().add(textArea);
        VBox.setVgrow(textArea, Priority.SOMETIMES);
        VBox.setMargin(textArea, new Insets(5, 5, 5, 5));
        AnchorPane.setBottomAnchor(vBox, 0.0);
        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
        AnchorPane.setTopAnchor(vBox, 0.0);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(250);
        anchorPane.getChildren().add(vBox);
        ScrollPane scrollPane = new ScrollPane(anchorPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; " +
                "-fx-border-color: #5999ff; " +
                "-fx-border-width: 3;");
        HBox.setMargin(scrollPane, new Insets(10, 10, 10, 10));
        listStatuses.getChildren().add(scrollPane);
    }

    @FXML
    public void back(){
        try {
            MainApplication.setCurrentTaskList(null);
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("task-list-view.fxml"));
            MainApplication.getStage().setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e){
            e.printStackTrace();
            return;
        }
    }

    @FXML
    public void logOut(){
        try {
            MainApplication.setUser(null);
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
            MainApplication.getStage().setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e){
            return;
        }
    }

    @FXML
    public void closeWindow(){
        MainApplication.getStage().close();
    }
}
