package dashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import login.bankingsystem.HelloController;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    @FXML
    private Label name;
    @FXML
    private Label body;
    public static String ac = HelloController.acc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInfo();
        body.setText("Simple Bank is an international bank headquartered in Seattle, WA, USA, \nthat constitutes the consumer and commercial banking. \nIt is one of the largest banks in the world with over 50,000 branches and ATMs.");
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
                name.setText(rs.getString("Name"));

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
