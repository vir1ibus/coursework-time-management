package org.vi1ibus.courseworktimemanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;

import java.io.IOException;

public class StatusListController {

    @FXML
    MenuButton loginMenu;

    @FXML
    public void initialize() {
        loginMenu.setText(MainApplication.getUser().getLogin());
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
}
