package login.bankingsystem;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    public static Stage stage = null;
    public static String acc;
    @FXML
    private Pane main_area;
    @FXML
    private TextField AccountNo;
    @FXML
    private PasswordField PSW;

    @FXML
    private void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void newAccount(MouseEvent event) throws IOException {

        Parent fxml = FXMLLoader.load(getClass().getResource("/createaccount/CreateAccount.fxml"));
        main_area.getChildren().removeAll();
        main_area.getChildren().addAll(fxml);
    }

    @FXML
    private void forgotPassword(MouseEvent event) throws IOException {

        Parent fxml = FXMLLoader.load(getClass().getResource("/forgotpsw/ForgotPSW.fxml"));
        main_area.getChildren().removeAll();
        main_area.getChildren().addAll(fxml);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void loginAccount(MouseEvent event) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            String sql = "SELECT * FROM userdata WHERE AccountNo=? and PSW =?";
            ps = con.prepareStatement(sql);

            ps.setString(1, AccountNo.getText());
            ps.setString(2, PSW.getText());
            acc = AccountNo.getText();

            rs = ps.executeQuery();
            if (rs.next()) {
                ((Node) event.getSource()).getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/dashboard/Dashboard.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().addAll(this.getClass().getResource("/design/designsheet.css").toExternalForm());
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                stage.show();
                HelloController.stage = stage;
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in login");
                a.setContentText("Your account number or password is wrong. Please try again!");
                a.showAndWait();
            }

        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Error in login");
            a.setContentText("There is some error. TRY AGAIN! " + e.getMessage());
            a.showAndWait();
        }
    }
}