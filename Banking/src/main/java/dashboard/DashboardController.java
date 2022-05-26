package dashboard;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import login.bankingsystem.Banking;
import login.bankingsystem.HelloController;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public static String ac = HelloController.acc;
    @FXML
    private Text name;
    @FXML
    private Circle profilepic;
    @FXML
    private Pane dash_main;
    private double xoffset = 0;
    private double yoffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData();
        try {
            mainScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout(MouseEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("/login/bankingsystem/login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(this.getClass().getResource("/design/designsheet.css").toExternalForm());
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xoffset = event.getSceneX();
                yoffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xoffset);
                stage.setY(event.getScreenY() - yoffset);
            }
        });
    }

    @FXML
    public void click(MouseEvent event){
        xoffset = event.getSceneX();
        yoffset = event.getSceneY();
    }

    @FXML
    public void drag(MouseEvent event){
        HelloController.stage.setX(event.getScreenX() - xoffset);
        HelloController.stage.setY(event.getScreenY() - yoffset);
    }

    @FXML
    public void accountInformation(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/accountinfo/AccountInfo.fxml"));
        dash_main.getChildren().removeAll();
        dash_main.getChildren().addAll(fxml);
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

    @FXML
    public void mainScreen() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        dash_main.getChildren().removeAll();
        dash_main.getChildren().addAll(fxml);
    }

    @FXML
    private void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public void setData(){
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
                name.setText(rs.getString(("Name")));
                InputStream is = rs.getBinaryStream("Photo");
                OutputStream os = new FileOutputStream(new File("pic.jpg"));
                byte[] content = new byte[1024];
                int size = 0;
                while((size = is.read(content))!= -1){
                    os.write(content, 0, size);
                }
                os.close();
                is.close();
                Image img = new Image("file:pic.jpg", false);
                profilepic.setFill(new ImagePattern(img));
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
