/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartmultiplug.controller;

import com.jfoenix.controls.JFXButton;
import db.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Ronila
 */
public class LogInController implements Initializable {

    @FXML
    private TextField txtUserName;
    @FXML
    private TextField txtPassword;
    @FXML
    private JFXButton btnLogIn;
    @FXML
    private AnchorPane paneLogIn;
    @FXML
    private JFXButton btnMinimize;

    private double x = 0, y = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                txtUserName.requestFocus();
                makeDragable();
            }
        });
    }

    @FXML
    private void txtUserNameAction(ActionEvent event) {
        txtPassword.requestFocus();
    }

    @FXML
    private void txtPasswordAction(ActionEvent event) {
        btnLogIn.requestFocus();
    }

    @FXML
    private void btnLogInMouseClicked(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
        try {
            if (txtUserName.getText().equals("test") && txtPassword.getText().equals("abc123")) {
                new Alert(Alert.AlertType.INFORMATION, "Test Account", ButtonType.OK).show();
                Stage stage = (Stage) this.paneLogIn.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/smartmultiplug/view/Home.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement pstm = connection.prepareStatement("SELECT * FROM users WHERE username=? && password=md5(?)");
                pstm.setObject(1, txtUserName.getText());
                pstm.setObject(2, txtPassword.getText());
                ResultSet rst = pstm.executeQuery();
                if (rst.next()) {
                    new Alert(Alert.AlertType.INFORMATION, "Welcome " + rst.getString(2), ButtonType.OK).show();
                    Stage stage = (Stage) this.paneLogIn.getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/smartmultiplug/view/Home.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Invalid User Name or Password !", ButtonType.OK).show();
                    txtUserName.requestFocus();
                    clearAll();
                }
            }
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Server is stoped. Please start server manually before login !. (TaskManager -> Services -> MYSQL80 -> Start)", ButtonType.OK).showAndWait();
            txtUserName.requestFocus();
            clearAll();
        }
    }

    @FXML
    private void btnForgotMouseClicked(MouseEvent event) {
        new Alert(Alert.AlertType.INFORMATION, "Please Contact Administrator to get master key to reset your password or use Test Account", ButtonType.OK).show();
    }

    private void makeDragable() {
        paneLogIn.setOnMousePressed(((event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        }));

        paneLogIn.setOnMouseDragged((event) -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
            stage.setOpacity(0.8f);
        });

        paneLogIn.setOnDragDone((event) -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setOpacity(1.0f);
        });

        paneLogIn.setOnMouseReleased((event) -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setOpacity(1.0f);
        });
    }

    @FXML
    private void btnClose(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void btnMinimizeAction(ActionEvent event) {
        Stage stage = (Stage) btnMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void btnSignInMouseClicked(MouseEvent event) throws IOException {
        Stage stage = (Stage) this.paneLogIn.getScene().getWindow();
        Parent prnt = FXMLLoader.load(getClass().getResource("/smartmultiplug/view/SignIn.fxml"));
        Scene scene = new Scene(prnt);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setResizable(false);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }

    private void clearAll() {
        txtUserName.setText("");
        txtPassword.setText("");
    }
}
