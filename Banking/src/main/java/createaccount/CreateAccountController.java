package createaccount;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import login.bankingsystem.Banking;

public class CreateAccountController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void backToLogIn(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Banking.class.getResource("login.fxml"));
        Banking.stage.getScene().setRoot(fxmlLoader.load());
    }

    public void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
