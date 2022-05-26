package withdraw;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import login.bankingsystem.HelloController;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class WithdrawController implements Initializable {
    public static String ac = HelloController.acc;

    @FXML
    private Label accountno;
    @FXML
    private Label balance;
    @FXML
    private TextField amt_field;
    @FXML
    private TextField psw_field;
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR);
    int min = cal.get(Calendar.MINUTE);
    int sec = cal.get(Calendar.SECOND);
    int daynight = cal.get(Calendar.AM_PM);

    DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
    Date d = new Date();
    String date = dateformat.format(d);


    LocalTime localtime = LocalTime.now();
    DateTimeFormatter dt = DateTimeFormatter.ofPattern("hh:mm:ss a");
    String time = localtime.format(dt);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInfo();
    }

    public void setInfo(){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            String sql = "SELECT * FROM userdata WHERE AccountNo=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, ac);

            rs = ps.executeQuery();
            if (rs.next()) {
                accountno.setText(rs.getString("AccountNo"));
                balance.setText(rs.getString("Balance"));

            }

        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Error in login");
            a.setContentText("There is some error. TRY AGAIN! " + e.getMessage());
            a.showAndWait();
        }
    }

    public void withdrawButton(){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            String sql = "SELECT * FROM userdata WHERE AccountNo=? and PSW=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, ac);
            ps.setString(2, psw_field.getText());

            rs = ps.executeQuery();
            if (rs.next()) {
                int wda = Integer.parseInt(amt_field.getText());
                int ta = Integer.parseInt(balance.getText());
                if(wda > ta){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Error in Withdraw");
                    a.setContentText("Your don't have enough balance. Please try again!");
                    a.showAndWait();
                }else{
                    int total = ta - wda;
                    String sql1 = "UPDATE userdata SET Balance ='" + total + "' WHERE AccountNo = '" + ac + "'";
                    ps = con.prepareStatement(sql1);
                    ps.execute();

                    String sql2 = "INSERT INTO withdraw (AccountNo, WithdrawAmount, RemainingAmount, D, T) VALUES (?,?,?,?,?)";
                    ps = con.prepareStatement(sql2);
                    ps.setString(1, ac);
                    ps.setString(2, String.valueOf(wda));
                    ps.setString(3, String.valueOf(total));
                    ps.setString(4, date);
                    ps.setString(5, time);

                    int i = ps.executeUpdate();
                    if (i >0){
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setTitle("Amount Withdraw");
                        a.setHeaderText("Amount Withdraw Successfully");
                        a.setContentText("Amount "+wda+" has been successfully withdraw\n" + "Current Balance = "+total);
                        a.showAndWait();

                        amt_field.setText("");
                        psw_field.setText("");
                        balance.setText(String.valueOf(total));
                    }

                }

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
