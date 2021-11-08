package org.vi1ibus.courseworktimemanagement;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.nio.charset.StandardCharsets;

public class AuthenticationScreenController {

    @FXML
    Label output_error_message;

    @FXML
    private TextField input_login, input_email;

    @FXML
    private PasswordField input_password, input_retry_password;

    public String get_SHA_512_SecurePassword(String password){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    @FXML
    protected void onSignInButtonClick() {
        try {
            MainApplication.setUser(ControllerDatabase.authenticationUser(input_login.getText(), get_SHA_512_SecurePassword(input_password.getText())));
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
    protected void onRegistrationButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("registration-screen-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            MainApplication.setDraggable(scene);
            MainApplication.getStage().setScene(scene);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MainApplication.setDraggable(scene);
        MainApplication.getStage().setScene(scene);
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
                            switch (ControllerDatabase.registrationUser(input_login.getText(), get_SHA_512_SecurePassword(input_password.getText()), input_email.getText())) {
                                case 0:
                                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-screen-view.fxml"));
                                    Scene scene = new Scene(fxmlLoader.load());
                                    MainApplication.setDraggable(scene);
                                    MainApplication.getStage().setScene(scene);
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

    @FXML
    public void closeWindow(){
        MainApplication.getStage().close();
    }
}