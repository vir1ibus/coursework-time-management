package org.vi1ibus.courseworktimemanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToolBar;

import java.io.IOException;

public class StatusListController {

    @FXML
    ToolBar toolBar;

    @FXML
    MenuButton loginMenu;

    @FXML
    public void initialize() {
        loginMenu.setText(MainApplication.getUser().getLogin());
        MainApplication.setDraggable(toolBar);
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
