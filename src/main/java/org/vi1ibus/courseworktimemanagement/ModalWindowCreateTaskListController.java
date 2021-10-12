package org.vi1ibus.courseworktimemanagement;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class ModalWindowCreateTaskListController {

    @FXML
    Label errorMessage;

    @FXML
    TextField nameTaskList;

    @FXML
    CheckBox everyone;

    @FXML
    FlowPane rootPane;

    @FXML
    public void createTaskList(){
        if(ControllerDatabase.checkDuplicateNameTaskList(MainApplication.getUser().getUserID(), nameTaskList.getText())){
            errorMessage.setText("This name has already been used.");
        } else {
            if(everyone.isSelected()) {
                ControllerDatabase.createTaskList(new TaskList(
                        nameTaskList.getText(),
                        1,
                        MainApplication.getUser().getUserID()
                ));
            } else {
                ControllerDatabase.createTaskList(new TaskList(
                        nameTaskList.getText(),
                        0,
                        MainApplication.getUser().getUserID()
                ));
            }
            rootPane.getScene().getWindow().hide();
        }
    }
}
