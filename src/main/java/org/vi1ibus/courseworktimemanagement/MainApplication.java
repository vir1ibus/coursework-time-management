package org.vi1ibus.courseworktimemanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainApplication extends Application {

    private static ArrayList<TaskList> taskLists;
    private static User user;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        MainApplication.stage = stage;
        ControllerDatabase controllerDatabase = new ControllerDatabase();
        controllerDatabase.checkExistsDB();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    public static void setUser(User user) {
        MainApplication.user = user;
    }

    public static User getUser() { return user; }

    public static void setStage(Stage stage) {
        MainApplication.stage = stage;
    }

    public static Stage getStage() { return stage; }


    public static void main(String[] args) {
        launch(args);
    }
}