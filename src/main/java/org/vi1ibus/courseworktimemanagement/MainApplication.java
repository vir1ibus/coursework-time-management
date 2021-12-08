package org.vi1ibus.courseworktimemanagement;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainApplication extends Application {

    private static User user;
    private static TaskList currentTaskList;
    private static ArrayList<TaskList> currentArrayListTasksLists;
    private static HashMap<String, String> currentStatuses = new HashMap<>();
    private static HashMap<String, HashMap<String, Task>> currentTasksByStatuses = new HashMap<>();
    private static Stage stage;


    @Override
    public void start(Stage stage) throws IOException {
        MainApplication.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        setDraggable(scene);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }



    public static URL getFxmlForTests(String filename) {
        return MainApplication.class.getResource(filename);
    }

    public static void setDraggable(Scene scene){
        double[] xOffset = {0};
        double[] yOffset = {0};
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset[0] = stage.getX() - event.getScreenX();
                yOffset[0] = stage.getY() - event.getScreenY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() + xOffset[0]);
                stage.setY(event.getScreenY() + yOffset[0]);
            }
        });
    }

    public static void setDraggable(Node node){
        double[] xOffset = {0};
        double[] yOffset = {0};
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset[0] = stage.getX() - event.getScreenX();
                yOffset[0] = stage.getY() - event.getScreenY();
            }
        });
        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() + xOffset[0]);
                stage.setY(event.getScreenY() + yOffset[0]);
            }
        });
    }

    public static void setCurrentArrayListTasksLists(ArrayList<TaskList> currentArrayListTasksLists) { MainApplication.currentArrayListTasksLists = currentArrayListTasksLists; }

    public static void setUser(User user) { MainApplication.user = user; }

    public static void setCurrentTaskList(TaskList currentTaskList) { MainApplication.currentTaskList = currentTaskList; }

    public static void setCurrentStatuses(HashMap<String, String> currentStatuses) { MainApplication.currentStatuses = currentStatuses; }

    public static void setCurrentTasksByStatuses(HashMap<String, HashMap<String, Task>> currentTasksByStatuses) { MainApplication.currentTasksByStatuses = currentTasksByStatuses; }

    public static HashMap<String, String> getCurrentStatuses() { return currentStatuses; }

    public static HashMap<String, HashMap<String, Task>> getCurrentTasksByStatuses() { return currentTasksByStatuses; }

    public static ArrayList<TaskList> getCurrentArrayListTasksLists() { return currentArrayListTasksLists; }

    public static TaskList getCurrentTaskList() { return currentTaskList; }

    public static User getUser() { return user; }

    public static Stage getStage() { return stage; }

    public static void setStage(Stage stage) { MainApplication.stage = stage; }

    public static void main(String[] args) {
        launch(args);
    }
}