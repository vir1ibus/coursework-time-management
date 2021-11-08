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
import javafx.util.Callback;
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
    AnchorPane toolBar;

    Popup popup;

    ArrayList<GridPane> gridPanesTaskLists = new ArrayList<>();

    final String[] selected = {""};

    @FXML
    public void initialize(){
        loginMenu.setText(MainApplication.getUser().getLogin());
        MainApplication.setDraggable(toolBar);
        updateOwnerTaskListListView();
    }

    private void updateOwnerTaskListListView() throws NullPointerException{
        MainApplication.setCurrentArrayListTasksLists(ControllerDatabase.getOwnerTasksLists(MainApplication.getUser().getUserID()));
        if(MainApplication.getCurrentArrayListTasksLists() != null) {
            listViewTasksLists.getChildren().clear();
            for (TaskList taskList : MainApplication.getCurrentArrayListTasksLists()) {
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
                            for (TaskList taskList : MainApplication.getCurrentArrayListTasksLists()) {
                                if (String.valueOf(taskList.getId()).equals(gridPane.getId())) {
                                    MainApplication.setCurrentTaskList(taskList);
                                    break;
                                }
                            }

                            leftSide.getChildren().clear();

                            DatePicker datePicker = new DatePicker(LocalDate.now());
                            datePicker.setShowWeekNumbers(true);
                            Callback<DatePicker, DateCell> dayCellFactory= getDayCellFactory();
                            datePicker.setDayCellFactory(dayCellFactory);
                            DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
                            Node popupContent = datePickerSkin.getPopupContent();
                            popupContent.setStyle("-fx-effect: null;");
                            leftSide.getChildren().add(popupContent);

                            ArrayList<Pair<String, Integer>> statuses = ControllerDatabase.getCountTaskListStatuses(taskList.getId());
                            if(!statuses.isEmpty()) {
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

    private Callback<DatePicker, DateCell> getDayCellFactory() {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        LocalDate start = MainApplication.getCurrentTaskList().getStart();
                        LocalDate end = MainApplication.getCurrentTaskList().getEnd();
                        if((start.isBefore(item) && end.isAfter(item)) || start.equals(item) || end.equals(item)) {
                            setStyle(getStyle() + " -fx-text-fill: white; -fx-background-color: #5999ff;");
                        }
                        if(item.equals(LocalDate.now())) {
                            setStyle(getStyle() + " -fx-text-fill: white; -fx-background-color: red;");
                        }
                    }
                };
            }
        };
        return dayCellFactory;
    }

    @FXML
    public void deleteTaskList(){
        if(!selected[0].equals("")) {
            ControllerDatabase.deleteOwnerTasksLists(Integer.parseInt(selected[0]));
            updateOwnerTaskListListView();
            leftSide.getChildren().clear();
            selected[0] = "";
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
                if(nameTaskList.getText().isEmpty()) {
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