package org.vi1ibus.courseworktimemanagement;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class MainApplication extends Application {

    private static ArrayList<TaskList> taskLists;
    private static User user;
    private static Stage current_stage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        AuthenticationScreenController.setCurrentStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    public static void setUser(User user) {
        MainApplication.user = user;
    }

    public static VBox createHeader(Stage stage) {
        MenuItem logout = new MenuItem("Log out");
        logout.setMnemonicParsing(false);
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    AuthenticationScreenController.setCurrentStage(stage);
                    stage.setScene(scene);
                } catch (IOException e){
                    return;
                }
            }
        });
        SplitMenuButton login = new SplitMenuButton(logout);
        login.setText(user.getLogin());
        login.setFont(new Font("Comic Sans MS Italic", 14));
        login.setStyle("-fx-text-fill: #fff");
        login.setAlignment(Pos.CENTER);
        login.setContentDisplay(ContentDisplay.BOTTOM);
        login.setMnemonicParsing(false);
        login.setPopupSide(Side.RIGHT);
        login.setTextAlignment(TextAlignment.CENTER);
        ToolBar toolBar = new ToolBar(login);
        toolBar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        toolBar.setPrefHeight(60);
        toolBar.setStyle("-fx-background-color: #000");
        VBox vBox = new VBox(toolBar);
        vBox.setPrefHeight(600);
        vBox.setPrefWidth(900);
        return vBox;
    }

    public static void showTaskLists(Stage stage){
        current_stage = stage;
        VBox vBox = createHeader(stage);
        DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
        Node popupContent = datePickerSkin.getPopupContent();
        VBox leftSide = new VBox(popupContent);
        leftSide.setAlignment(Pos.CENTER);
        leftSide.setPrefWidth(450);
        leftSide.setPrefHeight(540);
        leftSide.setPadding(new Insets(10, 50, 10, 50));
        VBox rightSide = new VBox();
        rightSide.setPrefWidth(450);
        rightSide.setPrefHeight(540);
        FlowPane flowPane = new FlowPane(leftSide, rightSide);
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setColumnHalignment(HPos.CENTER);
        vBox.getChildren().addAll(flowPane);
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}