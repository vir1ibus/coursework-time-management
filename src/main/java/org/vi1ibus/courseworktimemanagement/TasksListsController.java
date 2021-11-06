package org.vi1ibus.courseworktimemanagement;

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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
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
    VBox listViewTasksLists;

    @FXML
    ImageView createTaskList;

    @FXML
    ToolBar toolBar;

    Popup popup;

    ArrayList<GridPane> gridPanesTaskLists = new ArrayList<>();

    @FXML
    public void initialize(){
        loginMenu.setText(MainApplication.getUser().getLogin());
        MainApplication.setDraggable(toolBar);
        updateOwnerTaskListListView();
    }

    private void updateOwnerTaskListListView() throws NullPointerException{
        MainApplication.setArrayListTasksLists(ControllerDatabase.getOwnerTasksLists(MainApplication.getUser().getUserID()));
        if(MainApplication.getArrayListTasksLists() != null) {
            final String[] selected = {""};
            listViewTasksLists.getChildren().clear();
            for (TaskList taskList : MainApplication.getArrayListTasksLists()) {
                GridPane gridPane = new GridPane();
                Label labelName = new Label("Name:");
                labelName.setStyle("-fx-text-fill: white;");
                gridPane.add(labelName, 0, 0);
                GridPane.setMargin(labelName, new Insets(5, 2, 5, 0));
                Label labelTextName = new Label(taskList.getName());
                labelTextName.setStyle("-fx-text-fill: white;");
                gridPane.add(labelTextName, 1, 0);
                GridPane.setMargin(labelTextName, new Insets(5, 0, 5, 2));
                Label labelPrivilege = new Label("Privilege:");
                labelPrivilege.setStyle("-fx-text-fill: white;");
                gridPane.add(labelPrivilege, 2, 0);
                GridPane.setMargin(labelPrivilege, new Insets(5, 2, 5, 0));
                Label labelTextPrivilege = new Label("owner");
                labelTextPrivilege.setStyle("-fx-text-fill: white;");
                gridPane.add(labelTextPrivilege, 3, 0);
                GridPane.setMargin(labelTextPrivilege, new Insets(5, 0, 5, 2));
                ColumnConstraints labelConstraints = new ColumnConstraints();
                labelConstraints.setHalignment(HPos.RIGHT);
                labelConstraints.setHgrow(Priority.ALWAYS);
                ColumnConstraints textConstraints = new ColumnConstraints();
                textConstraints.setHalignment(HPos.LEFT);
                textConstraints.setHgrow(Priority.ALWAYS);
                gridPane.getColumnConstraints().add(labelConstraints);
                gridPane.getColumnConstraints().add(textConstraints);
                gridPane.getColumnConstraints().add(labelConstraints);
                gridPane.getColumnConstraints().add(textConstraints);
                gridPane.setStyle("-fx-border-color: #5999ff; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-background-color: #5999ff;");
                gridPane.setId(String.valueOf(taskList.getId()));
                gridPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (selected[0].equals(gridPane.getId())) {
                            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("status-list-view.fxml"));
                            try {
                                MainApplication.getStage().setScene(new Scene(fxmlLoader.load()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            leftSide.getChildren().clear();

                            DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
                            Node popupContent = datePickerSkin.getPopupContent();
                            popupContent.setStyle("-fx-effect: null;");
                            leftSide.getChildren().add(popupContent);

                            for (TaskList taskList : ControllerDatabase.getOwnerTasksLists(MainApplication.getUser().getUserID())) {
                                if (String.valueOf(taskList.getId()).equals(gridPane.getId())) {
                                    ArrayList<Pair<String, Integer>> statuses = ControllerDatabase.getTaskListStatuses(taskList.getId());
                                    for (Pair<String, Integer> status : statuses) {
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
                                    break;
                                }
                            }
                            for (GridPane gp : gridPanesTaskLists) {
                                if (!gp.getStyle().contains("-fx-background-color: #5999ff;")) {
                                    gp.setStyle("-fx-border-color: #5999ff; " +
                                            "-fx-border-width: 2; " +
                                            "-fx-border-radius: 20; " +
                                            "-fx-background-radius: 20; " +
                                            "-fx-background-color: #5999ff;");
                                    gp.getChildren().get(0).setStyle("-fx-text-fill: white;");
                                    gp.getChildren().get(1).setStyle("-fx-text-fill: white;");
                                    gp.getChildren().get(2).setStyle("-fx-text-fill: white;");
                                    gp.getChildren().get(3).setStyle("-fx-text-fill: white;");
                                    break;
                                }
                            }
                            labelName.setStyle("-fx-text-fill: black;");
                            labelTextName.setStyle("-fx-text-fill: black;");
                            labelPrivilege.setStyle("-fx-text-fill: black;");
                            labelTextPrivilege.setStyle("-fx-text-fill: black;");
                            gridPane.setStyle("-fx-border-color: #5999ff; " +
                                    "-fx-border-width: 2; " +
                                    "-fx-border-radius: 20; " +
                                    "-fx-background-radius: 20;");
                            selected[0] = gridPane.getId();
                        }
                    }
                });
                gridPanesTaskLists.add(gridPane);
                listViewTasksLists.getChildren().add(gridPane);
                VBox.setMargin(gridPane, new Insets(2.5, 10, 2.5, 10));
            }
        }
    }

    @FXML
    public void showPopupWindowCreateTaskList(){
        popup = new Popup();
        popup.setAutoHide(true);
        FlowPane flowPane = new FlowPane();
        flowPane.setPrefSize(210, 140);
        flowPane.setStyle("-fx-background-color: #5999ff; " +
                          "-fx-background-radius: 20;");
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setColumnHalignment(HPos.CENTER);
        flowPane.setOrientation(Orientation.VERTICAL);
        flowPane.setPadding(new Insets(15));
        Label name = new Label("Name task list");
        name.setTextFill(Color.WHITE);
        Label errMessage = new Label();
        errMessage.setTextFill(Color.WHITE);
        TextField nameTaskList = new TextField();
        CheckBox everyone = new CheckBox();
        everyone.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        everyone.setText("Available to everyone");
        everyone.setTextFill(Color.WHITE);
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
                    updateOwnerTaskListListView();
                }
            }
        });
        btn.setTextAlignment(TextAlignment.CENTER);
        flowPane.getChildren().addAll(name, nameTaskList, everyone, btn, errMessage);
        popup.getContent().addAll(flowPane);
        Bounds boundsInScreen = createTaskList.localToScreen(createTaskList.getLayoutBounds());
        popup.setX(boundsInScreen.getMinX() - 110 + createTaskList.getFitWidth() / 2);
        popup.setY(boundsInScreen.getMinY() - 135);
        popup.show(MainApplication.getStage());
    }

    @FXML
    public void logOut(){
        try {
            MainApplication.setUser(null);
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            MainApplication.setDraggable(scene);
            MainApplication.getStage().setScene(scene);
        } catch (IOException e){
            return;
        }
    }

    @FXML
    public void closeWindow(){
        MainApplication.getStage().close();
    }
}