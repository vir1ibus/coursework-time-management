package org.vi1ibus.courseworktimemanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AuthenticationScreenController {

    @FXML
    Label output_error_message;

    @FXML
    private TextField input_login, input_email;

    @FXML
    private PasswordField input_password, input_retry_password;


    @FXML
    protected void onSignInButtonClick() {
        try {
            MainApplication.setUser(ControllerDatabase.authenticationUser(input_login.getText(), input_password.getText()));
            if(MainApplication.getUser() != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("task-list-view.fxml"));
                MainApplication.getStage().setScene(new Scene(fxmlLoader.load()));
            } else {
                output_error_message.setText("Wrong login or password.");
            }
        } catch (NullPointerException | IOException e){
            e.printStackTrace();
            output_error_message.setText("Some fields are not filled.");
        }

    }

    @FXML
    protected void onRegistrationButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration-screen-view.fxml"));
        MainApplication.getStage().setScene(new Scene(fxmlLoader.load()));
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
        MainApplication.getStage().setScene(new Scene(fxmlLoader.load()));
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
                                    MainApplication.getStage().setScene(new Scene(fxmlLoader.load()));
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