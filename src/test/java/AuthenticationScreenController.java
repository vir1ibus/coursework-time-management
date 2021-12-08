import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.vi1ibus.courseworktimemanagement.MainApplication;

import java.io.IOException;

class AuthenticationScreenTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        try {
            MainApplication.setStage(stage);
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.getFxmlForTests("login-screen-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSignInError() {
        clickOn("#input_login").write("aa");
        clickOn("#input_password").write("aa");
        clickOn("#signIn");
        FxAssert.verifyThat("#output_error_message", LabeledMatchers.hasText("Wrong login or password."));
    }

    @Test
    public void testSuccessfulSignIn() {
        clickOn("#input_login").write("root");
        clickOn("#input_password").write("root");
        clickOn("#signIn");
        FxAssert.verifyThat("#loginMenu", LabeledMatchers.hasText("root"));
    }
}
