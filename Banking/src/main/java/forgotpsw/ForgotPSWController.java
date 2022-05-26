package forgotpsw;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.stage.FileChooser;
import login.bankingsystem.Banking;

public class ForgotPSWController implements Initializable{

    @FXML
    private TextField AccountNo;
    @FXML
    private TextField Answer;
    @FXML
    private ComboBox<String> SecurityQ;

    ObservableList<String> list  = FXCollections.observableArrayList("What is your pet name?", "Which city were you born in?", "What is your nickname?");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SecurityQ.setItems(list);
    }

    public void backToLogIn(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Banking.class.getResource("login.fxml"));
        Banking.stage.getScene().setRoot(fxmlLoader.load());
    }

    public void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public void recoverPsw(MouseEvent event) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            String sql = "SELECT * FROM userdata WHERE AccountNo=? and SecurityQ=? and Answer=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, AccountNo.getText());
            ps.setString(2, SecurityQ.getValue());
            ps.setString(3, Answer.getText());


            rs = ps.executeQuery();
            if (rs.next()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Password Recovery");
                a.setHeaderText("Below is your password.");
                a.setContentText("Account Number: " + rs.getString("AccountNo")+"\nPassword: "+rs.getString("PSW"));
                a.showAndWait();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Wrong Data");
                a.setContentText("Please try again!");
                a.showAndWait();
            }

        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Error in Recovery");
            a.setContentText("There is some error. TRY AGAIN! " + e.getMessage());
            a.showAndWait();
        }
    }
}
