package accountinfo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import login.bankingsystem.HelloController;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AccountInfoController implements Initializable {
    public static String ac = HelloController.acc;
    @FXML
    private Text accountno;
    @FXML
    private Text gender;
    @FXML
    private Text accounttype;
    @FXML
    private Text race;
    @FXML
    private Label balance;
    @FXML
    private Pane dash_main;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInfo();
    }

    public void setInfo(){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            String sql = "SELECT * FROM userdata WHERE AccountNo=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, ac);

            rs = ps.executeQuery();
            if (rs.next()) {
                accountno.setText(rs.getString("AccountNo"));
                gender.setText(rs.getString("Gender"));
                accounttype.setText(rs.getString("AccountType"));
                race.setText(rs.getString("RaceEthnicity"));
                balance.setText(rs.getString("Balance"));
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

    @FXML
    public void withdraw(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/withdraw/Withdraw.fxml"));
        dash_main.getChildren().removeAll();
        dash_main.getChildren().addAll(fxml);
    }

    @FXML
    public void deposit(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/deposit/Deposit.fxml"));
        dash_main.getChildren().removeAll();
        dash_main.getChildren().addAll(fxml);
    }
}
