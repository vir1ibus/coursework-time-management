package org.vi1ibus.courseworktimemanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthenticationScreenController {

    private static Stage currentStage;
    private static User user;

    public static void setCurrentStage(Stage currentStage) {
        AuthenticationScreenController.currentStage = currentStage;
    }

    @FXML
    Label output_error_message;

    @FXML
    private TextField input_login, input_email;

    @FXML
    private PasswordField input_password, input_retry_password;


    @FXML
    protected void onSignInButtonClick() {
        try {
            user = ControllerDatabase.authenticationUser(input_login.getText(), input_password.getText());
            if(user != null) {
                MainApplication.setUser(user);
                MainApplication.showTaskLists(currentStage);
            } else {
                output_error_message.setText("Wrong login or password.");
            }
        } catch (NullPointerException e){
            output_error_message.setText("Some fields are not filled.");
        }

    }

    @FXML
    protected void onRegistrationButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration-screen-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        currentStage.setScene(scene);
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        currentStage.setScene(scene);
    }

    @FXML
    protected void onConfirmRegistrationButtonClick() throws IOException {
        if(input_login.getText().length() > 0 && input_password.getText().length() > 0 && input_email.getText().length() > 0) {
            if(input_login.getText().length() >= 3) {
                if(input_password.getText().length() > 6) {
                    if(input_email.getText().length() >= 5 && input_email.getText().contains("@") && input_email.getText().contains(".")){
                        if (input_password.equals(input_retry_password)) {
                            output_error_message.setText("Passwords do not match.");
                        } else {
                            switch (ControllerDatabase.registrationUser(input_login.getText(), input_password.getText(), input_email.getText())) {
                                case 0:
                                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
                                    Scene scene = new Scene(fxmlLoader.load());
                                    currentStage.setScene(scene);
                                    break;
                                case 1:
                                    output_error_message.setText("Login already exists.");
                                    break;
                                case 2:
                                    output_error_message.setText("This email already registered.");
                                    break;
                                case -1:
                                    output_error_message.setText("Registration unsuccessful.");
                                    break;
                            }
                        }
                    } else {
                        output_error_message.setText("Incorrect email.");
                    }
                } else {
                    output_error_message.setText("Password must be at least 6 characters.");
                }
            } else {
                output_error_message.setText("Login must contain at least 3 characters.");
            }
        } else {
            output_error_message.setText("Some fields are not filled.");
        }
    }
}