package org.vi1ibus.courseworktimemanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

public class TasksListsController {

    @FXML
    VBox leftSide;

    @FXML
    SplitMenuButton loginMenu;

    @FXML
    public void initialize(){
        loginMenu.setText(MainApplication.getUser().getLogin());
        DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
        Node popupContent = datePickerSkin.getPopupContent();
        leftSide.getChildren().add(popupContent);
    }

    @FXML
    public void logOut(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
            MainApplication.getStage().setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e){
            return;
        }
    }
}
