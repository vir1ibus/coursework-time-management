package org.vi1ibus.courseworktimemanagement;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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

        updateTaskListListView();

        MultipleSelectionModel<String> tasksListsSelectionModel = listViewTasksLists.getSelectionModel();
        tasksListsSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue) {

                leftSide.getChildren().clear();

                DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
                Node popupContent = datePickerSkin.getPopupContent();
                popupContent.setId("datePickerSkin");
                popupContent.setStyle("-fx-effect: null;");
                leftSide.getChildren().add(popupContent);

                for(TaskList taskList : MainApplication.getTaskLists()){
                    if(newValue.equals(taskList.getName())){
                        ArrayList<Pair<String, Integer>> statuses = ControllerDatabase.getTaskListStatuses(taskList.getId());
                        for(Pair<String, Integer> status : statuses){
                            Label name = new Label(status.getKey() + ":");
                            name.setStyle("-fx-padding: 2px;\n" +
                                          "-fx-border-insets: 2px;\n" +
                                          "-fx-background-insets: 2px;");
                            Label count = new Label(String.valueOf(status.getValue()));
                            count.setStyle("-fx-padding: 2px;\n" +
                                           "-fx-border-insets: 2px;\n" +
                                           "-fx-background-insets: 2px;");
                            HBox hBox = new HBox(name, count);
                            hBox.setAlignment(Pos.CENTER);

                            leftSide.getChildren().add(hBox);
                        }
                    }
                }
            }
        });
    }

    public void updateTaskListListView(){
        MainApplication.setTaskLists(ControllerDatabase.getTasksLists(MainApplication.getUser().getUserID()));

        ObservableList<String> tasksLists = FXCollections.observableArrayList();

        for(TaskList taskList : MainApplication.getTaskLists()){
            tasksLists.add(taskList.getName());
        }

        listViewTasksLists.setItems(tasksLists);
    }

    @FXML
    public void showModalWindowCreateTaskList(){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("modal-window-create-task-list-view.fxml"));
            stage.setResizable(false);
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.showAndWait();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            updateTaskListListView();
        }
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
