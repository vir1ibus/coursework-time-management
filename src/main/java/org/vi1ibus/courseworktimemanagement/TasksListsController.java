package org.vi1ibus.courseworktimemanagement;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class TasksListsController {

    @FXML
    VBox leftSide;

    @FXML
    MenuButton loginMenu;

    @FXML
    ListView<String> listViewTasksLists;

    @FXML
    public void initialize(){
        loginMenu.setText(MainApplication.getUser().getLogin());

        DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
        Node popupContent = datePickerSkin.getPopupContent();
        leftSide.getChildren().add(popupContent);

        MainApplication.setTaskLists(ControllerDatabase.getTasksLists(MainApplication.getUser().getUserID()));

        ObservableList<String> tasksLists = FXCollections.observableArrayList();

        for(TaskList taskList : MainApplication.getTaskLists()){
            tasksLists.add(taskList.getName());
        }

        listViewTasksLists.setItems(tasksLists);

        MultipleSelectionModel<String> tasksListsSelectionModel = listViewTasksLists.getSelectionModel();
        tasksListsSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue) {
                for(TaskList taskList : MainApplication.getTaskLists()){
                    if(newValue.equals(taskList.getName())){
                        ArrayList<Pair<String, Integer>> statuses = ControllerDatabase.getTaskListStatuses(taskList.getId());
                        for(Pair<String, Integer> status : statuses){
                            Label name = new Label(status.getKey());
                            name.setOpaqueInsets(new Insets(0, 2, 0, 0));
                            Label count = new Label(String.valueOf(status.getValue()));
                            count.setOpaqueInsets(new Insets(0, 0, 0, 2));
                            HBox hBox = new HBox(name, count);
                            leftSide.getChildren().add(hBox);
                        }
                    }
                }
            }
        });
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
