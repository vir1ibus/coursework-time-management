package org.vi1ibus.courseworktimemanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainApplication extends Application {

    private static User user;
    private static TaskList currentTaskList;
    private static ArrayList<TaskList> arrayListTasksLists;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        MainApplication.stage = stage;
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    public static void setArrayListTasksLists(ArrayList<TaskList> arrayListTasksLists) { MainApplication.arrayListTasksLists = arrayListTasksLists; }

    public static void setUser(User user) { MainApplication.user = user; }

    public static void setCurrentTaskList(TaskList current_task_list) { MainApplication.currentTaskList = currentTaskList; }

    public static ArrayList<TaskList> getArrayListTasksLists() { return arrayListTasksLists; }

    public static TaskList getCurrentTaskList() { return currentTaskList; }

    public static User getUser() {
        return user;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}