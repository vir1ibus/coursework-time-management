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

    private static User user;
    private static Stage currentStage;
    private static ControllerDatabase controllerDatabase;

    @Override
    public void start(Stage stage) throws IOException {
        controllerDatabase = new ControllerDatabase();
        currentStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    public static ControllerDatabase getControllerDatabase() { return controllerDatabase; }

    public static void setUser(User user) {
        MainApplication.user = user;
    }

    public static Stage getCurrentStage() { return currentStage; }

    public static void main(String[] args) {
        launch(args);
    }
}