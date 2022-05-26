package createaccount;

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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.ResourceBundle;

import javafx.stage.FileChooser;
import login.bankingsystem.Banking;


public class CreateAccountController implements Initializable {
    @FXML
    private TextField Name;
    @FXML
    private TextField CardNo;
    @FXML
    private TextField PhoneNo;
    @FXML
    private TextField Address;
    @FXML
    private TextField ZipCode;
    @FXML
    private TextField AccountNo;
    @FXML
    private TextField Balance;
    @FXML
    private TextField PSW;
    @FXML
    private TextField Answer;
    @FXML
    private DatePicker DOB;
    @FXML
    private ComboBox<String> Gender;
    @FXML
    private ComboBox<String> MartialStatus;
    @FXML
    private ComboBox<String> RaceEthnicity;
    @FXML
    private ComboBox<String> AccountType;
    @FXML
    private ComboBox<String> SecurityQ;

    private FileChooser filechooser = new FileChooser();
    private File file;
    private FileInputStream fis;
    @FXML
    private ImageView Photo;

    ObservableList<String> list = FXCollections.observableArrayList("Male", "Female", "Other");
    ObservableList<String> list1 = FXCollections.observableArrayList("Single", "Married", "Other");
    ObservableList<String> list2 = FXCollections.observableArrayList("White", "Black", "Natives", "Asian", "Other");
    ObservableList<String> list3 = FXCollections.observableArrayList("Saving", "Checking", "Other");
    ObservableList<String> list4  = FXCollections.observableArrayList("What is your pet name?", "Which city were you born in?", "What is your nickname?");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Gender.setItems(list);
        MartialStatus.setItems(list1);
        RaceEthnicity.setItems(list2);
        AccountType.setItems(list3);
        SecurityQ.setItems(list4);
    }

    public void backToLogIn(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Banking.class.getResource("login.fxml"));
        Banking.stage.getScene().setRoot(fxmlLoader.load());
    }

    public void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public void setUpPic(MouseEvent event){
        filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images Files", "*.png","*.jpg"));
        file = filechooser.showOpenDialog(null);
        if (file!=null){
            Image img = new Image(file.toURI().toString(), 150, 150, true, true);
            Photo.setImage(img);
            Photo.setPreserveRatio(true);
        }
    }

    public void newAccount(MouseEvent event){
        Connection con = null;
        PreparedStatement ps = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","");
            String sql = "INSERT INTO userdata (Name, CardNo, PhoneNo, Gender, MartialStatus, RaceEthnicity, " +
                    "DOB, Address, ZipCode, AccountNo, Balance, AccountType, PSW, SecurityQ, Answer, Photo) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);

            ps.setString(1, Name.getText());
            ps.setString(2, CardNo.getText());
            ps.setString(3, PhoneNo.getText());
            ps.setString(4, Gender.getValue());
            ps.setString(5, MartialStatus.getValue());
            ps.setString(6, RaceEthnicity.getValue());
            ps.setString(7, DOB.getEditor().getText());
            ps.setString(8, Address.getText());
            ps.setString(9, ZipCode.getText());
            ps.setString(10, AccountNo.getText());
            ps.setString(11, Balance.getText());
            ps.setString(12, AccountType.getValue());
            ps.setString(13, PSW.getText());
            ps.setString(14, SecurityQ.getValue());
            ps.setString(15, Answer.getText());
            fis = new FileInputStream(file);
            ps.setBinaryStream(16, (InputStream)fis, (int)file.length());

            int i = ps.executeUpdate();
            if (i > 0){
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Account Created");
                a.setHeaderText("Account is created successfully");
                a.setContentText("Your account has been created successfully. You can now login with your account number and password.");
                a.showAndWait();
            }else{
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Account is NOT created");
                a.setContentText("Your account is not created. There is some error in the form. TRY AGAIN! ");
                a.showAndWait();
            }

        }catch(Exception e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Error in creating account");
            a.setContentText("Your account is not created due to some technical issues."+ e.getMessage());
            a.showAndWait();
        }
    }
}
