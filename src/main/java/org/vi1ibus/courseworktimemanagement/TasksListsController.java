package org.vi1ibus.courseworktimemanagement;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class TasksListsController {

    @FXML
    VBox leftSide;

    @FXML
    MenuButton loginMenu;

    @FXML
    ListView<String> listViewTasksLists;

    @FXML
    ImageView createTaskList;

    Popup popup;

    @FXML
    public void initialize(){
        loginMenu.setText(MainApplication.getUser().getLogin());

        try {
            updateTaskListListView();
        } catch (NullPointerException e){
            listViewTasksLists.setItems(FXCollections.observableArrayList());
        }

        MultipleSelectionModel<String> tasksListsSelectionModel = listViewTasksLists.getSelectionModel();
        tasksListsSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue) {

                leftSide.getChildren().clear();

                DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
                Node popupContent = datePickerSkin.getPopupContent();
                popupContent.setStyle("-fx-effect: null;");
                leftSide.getChildren().add(popupContent);

                for(TaskList taskList : ControllerDatabase.getTasksLists(MainApplication.getUser().getUserID())){ // FIX
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

    public void updateTaskListListView() throws NullPointerException{

        ObservableList<String> tasksLists = FXCollections.observableArrayList();

        for(TaskList taskList : ControllerDatabase.getTasksLists(MainApplication.getUser().getUserID())){
            tasksLists.add(taskList.getName());
        }

        listViewTasksLists.setItems(tasksLists);
    }

    @FXML
    public void showPopupWindowCreateTaskList(){
        if(popup != null){
            popup.hide();
        }
        popup = new Popup();
        FlowPane flowPane = new FlowPane();
        flowPane.setPrefSize(220, 140);
        flowPane.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #5999ff; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10;");
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setColumnHalignment(HPos.CENTER);
        flowPane.setOrientation(Orientation.VERTICAL);
        flowPane.setPadding(new Insets(15));
        Label name = new Label("Name task list");
        Label errMessage = new Label();
        TextField nameTaskList = new TextField();
        CheckBox everyone = new CheckBox();
        everyone.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        everyone.setText("Available to everyone");
        Button btn = new Button("Create task list");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(ControllerDatabase.checkDuplicateNameTaskList(MainApplication.getUser().getUserID(), nameTaskList.getText())){
                    errMessage.setText("This name has already been used.");
                } else if(nameTaskList.getText().isEmpty()) {
                    errMessage.setText("The task list name field cannot be empty.");
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
                    popup.hide();
                    updateTaskListListView();
                }
            }
        });
        btn.setTextAlignment(TextAlignment.CENTER);
        flowPane.getChildren().addAll(name, nameTaskList, everyone, btn, errMessage);
        popup.getContent().addAll(flowPane);
        Bounds boundsInScreen = createTaskList.localToScreen(createTaskList.getLayoutBounds());
        popup.setX(boundsInScreen.getMinX() - 220 / 2 + createTaskList.getFitWidth() / 2);
        popup.setY(boundsInScreen.getMinY() - 120 - 5);
        popup.show(MainApplication.getStage());
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
