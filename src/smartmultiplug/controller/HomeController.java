/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartmultiplug.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javax.microedition.io.StreamConnection;
import javax.swing.Timer;
import jssc.SerialPortException;
import smartmultiplug.bluetooth.BluetoothConnection;

/**
 * FXML Controller class
 *
 * @author Ronila
 */
public class HomeController implements Initializable {

    @FXML
    private AnchorPane paneHome;
    @FXML
    private ToggleButton tgBtnConnect;
    @FXML
    private ImageView imgViewConnecting;
    @FXML
    private ImageView imgViewDisconnect;
    @FXML
    private JFXToggleButton tBtnSwitch1;
    @FXML
    private JFXToggleButton tBtnSwitch2;
    @FXML
    private JFXToggleButton tBtnSwitch3;
    @FXML
    private JFXToggleButton tBtnSwitch4;
    @FXML
    private AnchorPane paneSwitch;
    @FXML
    private TextField txtTime1;
    @FXML
    private TextField txtTime2;
    @FXML
    private TextField txtTime3;
    @FXML
    private TextField txtTime4;
    @FXML
    private Label lblCountdown1;
    @FXML
    private Button btnStartCountdown1;
    @FXML
    private Button btnResetCountdown1;
    @FXML
    private Button btnStartCountdown2;
    @FXML
    private Button btnResetCountdown2;
    @FXML
    private Label lblCountdown2;
    @FXML
    private Button btnResetCountdown3;
    @FXML
    private Button btnStartCountdown3;
    @FXML
    private Label lblCountdown3;
    @FXML
    private Button btnResetCountdown4;
    @FXML
    private Button btnStartCountdown4;
    @FXML
    private Label lblCountdown4;
    @FXML
    private JFXButton btnMinimize;

    private double x = 0, y = 0;
    private static StreamConnection streamConnection;// = (StreamConnection) Connector.open(hc05Url);
    private static OutputStream os;// = streamConnection.openOutputStream();
    private int[] time1;
    private Timer timer1;
    private int[] time2;
    private Timer timer2;
    private int[] time3;
    private Timer timer3;
    private int[] time4;
    private Timer timer4;
    final int HOUR = 0;
    final int MIN = 1;
    final int SEC = 2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tgBtnConnect.requestFocus();
                makeDragable();
            }
        });
        tgBtnConnect.setSelected(true);
    }

    @FXML
    private void imgDetailsMouseClicked(MouseEvent event) throws IOException {
        try {
            if (!tgBtnConnect.isSelected()) {
                BluetoothConnection.getInstance().closeConnection();
                os.close();
            }
            Stage stage = (Stage) this.paneHome.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/smartmultiplug/view/About.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Cannot go to about page while bluetooth connection is on", ButtonType.OK).show();
        }
    }

    @FXML
    private void settingsMouseClicked(MouseEvent event) {
    }

    @FXML
    private void LogOutMouseClicked(MouseEvent event) throws IOException {
        try {
            if (!tgBtnConnect.isSelected()) {
                BluetoothConnection.getInstance().closeConnection();
                os.close();
            }
            Stage stage = (Stage) this.paneHome.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/smartmultiplug/view/LogIn.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Cannot logout while bluetooth connection is on", ButtonType.OK).show();
        }
    }

    private void makeDragable() {
        paneHome.setOnMousePressed(((event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        }));

        paneHome.setOnMouseDragged((event) -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
            stage.setOpacity(0.8f);
        });

        paneHome.setOnDragDone((event) -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setOpacity(1.0f);
        });

        paneHome.setOnMouseReleased((event) -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setOpacity(1.0f);
        });
    }

    @FXML
    private void tgBtnConnectAction(ActionEvent event) throws IOException {
        if (tgBtnConnect.isSelected()) {
            if ((lblCountdown1.getText().equals("") || lblCountdown1.getText().equals("End") || lblCountdown1.getText().equals("Reset")) && (lblCountdown2.getText().equals("") || lblCountdown2.getText().equals("End") || lblCountdown2.getText().equals("Reset")) && (lblCountdown3.getText().equals("") || lblCountdown3.getText().equals("End") || lblCountdown3.getText().equals("Reset")) && (lblCountdown4.getText().equals("") || lblCountdown4.getText().equals("End") || lblCountdown4.getText().equals("Reset"))) {
                try {
//                    BluetoothConnection.getInstance().closeConnection();
//                    os.close();
                    imgViewConnecting.setVisible(false);
                    imgViewDisconnect.setVisible(true);
                    new Alert(Alert.AlertType.INFORMATION, "Disconnected", ButtonType.OK).show();
                    paneSwitch.setDisable(true);
                    tgBtnConnect.setText("Connect");
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Something is Wrong Cannot Disconnect", ButtonType.OK).show();
                    tgBtnConnect.setSelected(false);
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "One or more countdown is not end yet. Please Reset them before disconnect", ButtonType.OK).show();
                tgBtnConnect.setSelected(false);
            }
        } else {
            try {
//                streamConnection = BluetoothConnection.getInstance().getConnection();
//                os = streamConnection.openOutputStream();
                imgViewConnecting.setVisible(true);
                imgViewDisconnect.setVisible(false);
                new Alert(Alert.AlertType.INFORMATION, "Connected", ButtonType.OK).show();
                paneSwitch.setDisable(false);
                tgBtnConnect.setText("Disconnect");
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Something is Wrong Cannot Connect. Please Check Bluetooth is on and Arduino is Powerd", ButtonType.OK).show();
                tgBtnConnect.setSelected(true);
            }
        }
    }

    @FXML
    private void tBtnSwitch1Action(ActionEvent event) throws SerialPortException, Exception {
        txtTime1.setText("00:00:00");
        lblCountdown1.setText("");
        if (tBtnSwitch1.isSelected()) {
//            os.write("a".getBytes());
            try {
                Thread.sleep(50);
            } catch (Exception ex) {
            }
        } else {
//            os.write("e".getBytes());
            try {
                Thread.sleep(50);
            } catch (Exception ex) {
            }
        }

    }

    @FXML
    private void tBtnSwitch2Action(ActionEvent event) throws Exception {
        txtTime2.setText("00:00:00");
        lblCountdown2.setText("");
        if (tBtnSwitch2.isSelected()) {
//            os.write("b".getBytes());
            try {
                Thread.sleep(50);
            } catch (Exception ex) {
            }
        } else {
//            os.write("f".getBytes());
            try {
                Thread.sleep(50);
            } catch (Exception ex) {
            }
        }
    }

    @FXML
    private void tBtnSwitch3Action(ActionEvent event) throws Exception {
        txtTime3.setText("00:00:00");
        lblCountdown3.setText("");
        if (tBtnSwitch3.isSelected()) {
//            os.write("c".getBytes());
            try {
                Thread.sleep(50);
            } catch (Exception ex) {
            }
        } else {
//            os.write("g".getBytes());
            try {
                Thread.sleep(50);
            } catch (Exception ex) {
            }
        }
    }

    @FXML
    private void tBtnSwitch4Action(ActionEvent event) throws Exception {
        txtTime4.setText("00:00:00");
        lblCountdown4.setText("");
        if (tBtnSwitch4.isSelected()) {
//            os.write("d".getBytes());
            try {
                Thread.sleep(50);
            } catch (Exception ex) {
            }
        } else {
//            os.write("h".getBytes());
            try {
                Thread.sleep(50);
            } catch (Exception ex) {
            }
        }
    }

    @FXML
    private void txtTime1Action(ActionEvent event) {
        if (Pattern.compile("^[0-2][0-3]:[0-5][0-9]:[0-5][0-9]$").matcher(txtTime1.getText()).matches()) {
            btnStartCountdown1.requestFocus();
        } else {
            new Alert(Alert.AlertType.ERROR, "You can only input time HH:MM:SS and maximum delay time you can set is 1 day and that is 23:59:59. Please set deley time correctly.", ButtonType.OK).show();
            txtTime1.requestFocus();
        }
    }

    @FXML
    private void txtTime2Action(ActionEvent event) {
        if (Pattern.compile("^[0-2][0-3]:[0-5][0-9]:[0-5][0-9]$").matcher(txtTime2.getText()).matches()) {
            btnStartCountdown2.requestFocus();
        } else {
            new Alert(Alert.AlertType.ERROR, "You can only input time HH:MM:SS and maximum delay time you can set is 1 day and that is 23:59:59. Please set deley time correctly.", ButtonType.OK).show();
            txtTime2.requestFocus();
        }
    }

    @FXML
    private void txtTime3Action(ActionEvent event) {
        if (Pattern.compile("^[0-2][0-3]:[0-5][0-9]:[0-5][0-9]$").matcher(txtTime3.getText()).matches()) {
            btnStartCountdown3.requestFocus();
        } else {
            new Alert(Alert.AlertType.ERROR, "You can only input time HH:MM:SS and maximum delay time you can set is 1 day and that is 23:59:59. Please set deley time correctly.", ButtonType.OK).show();
            txtTime3.requestFocus();
        }
    }

    @FXML
    private void txtTime4Action(ActionEvent event) {
        if (Pattern.compile("^[0-2][0-3]:[0-5][0-9]:[0-5][0-9]$").matcher(txtTime4.getText()).matches()) {
            btnStartCountdown4.requestFocus();
        } else {
            new Alert(Alert.AlertType.ERROR, "You can only input time HH:MM:SS and maximum delay time you can set is 1 day and that is 23:59:59. Please set deley time correctly.", ButtonType.OK).show();
            txtTime4.requestFocus();
        }
    }

    @FXML
    private void btnStartCountdown1Action(ActionEvent event) throws Exception {
        if (Pattern.compile("^[0-2][0-3]:[0-5][0-9]:[0-5][0-9]$").matcher(txtTime1.getText()).matches()) {
            if (!txtTime1.getText().equals("00:00:00")) {
                tBtnSwitch1.setSelected(true);
//                os.write("a".getBytes());
                tBtnSwitch1.setDisable(true);
                String time = txtTime1.getText();
                time1 = convertTimeToInt(time.split(":"));
                lblCountdown1.setText(time);
                timer1 = new Timer(1000, new TimerListener(time1, lblCountdown1, btnStartCountdown1, btnResetCountdown1, tBtnSwitch1, 1));
                timer1.start();
                btnStartCountdown1.setDisable(true);
                btnResetCountdown1.setDisable(false);
            } else {
                new Alert(Alert.AlertType.ERROR, "Deley time is zero seconds. Please set deley time correctly.", ButtonType.OK).show();
                txtTime1.requestFocus();
            }

        } else {
            new Alert(Alert.AlertType.ERROR, "You can only input time HH:MM:SS and maximum delay time you can set is 1 day and that is 23:59:59. Please set deley time correctly.", ButtonType.OK).show();
            txtTime1.requestFocus();
        }
    }

    @FXML
    private void btnResetCountdown1Action(ActionEvent event) {
        tBtnSwitch1.setDisable(false);
        timer1.stop();
        lblCountdown1.setText("Reset");
        txtTime1.setText("00:00:00");
        btnStartCountdown1.setDisable(false);
        btnResetCountdown1.setDisable(true);
    }

    @FXML
    private void btnStartCountdown2Action(ActionEvent event) throws Exception {
        if (Pattern.compile("^[0-2][0-3]:[0-5][0-9]:[0-5][0-9]$").matcher(txtTime2.getText()).matches()) {
            if (!txtTime2.getText().equals("00:00:00")) {
                tBtnSwitch2.setSelected(true);
//                os.write("b".getBytes());
                tBtnSwitch2.setDisable(true);
                String time = txtTime2.getText();
                time2 = convertTimeToInt(time.split(":"));
                lblCountdown2.setText(time);
                timer2 = new Timer(1000, new TimerListener(time2, lblCountdown2, btnStartCountdown2, btnResetCountdown2, tBtnSwitch2, 2));
                timer2.start();
                btnStartCountdown2.setDisable(true);
                btnResetCountdown2.setDisable(false);
            } else {
                new Alert(Alert.AlertType.ERROR, "Deley time is zero seconds. Please set deley time correctly.", ButtonType.OK).show();
                txtTime2.requestFocus();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "You can only input time HH:MM:SS and maximum delay time you can set is 1 day and that is 23:59:59. Please set deley time correctly.", ButtonType.OK).show();
            txtTime2.requestFocus();
        }
    }

    @FXML
    private void btnResetCountdown2Action(ActionEvent event) {
        tBtnSwitch2.setDisable(false);
        timer2.stop();
        lblCountdown2.setText("Reset");
        txtTime2.setText("00:00:00");
        btnStartCountdown2.setDisable(false);
        btnResetCountdown2.setDisable(true);
    }

    @FXML
    private void btnStartCountdown3Action(ActionEvent event) throws Exception {
        if (Pattern.compile("^[0-2][0-3]:[0-5][0-9]:[0-5][0-9]$").matcher(txtTime3.getText()).matches()) {
            if (!txtTime3.getText().equals("00:00:00")) {
                tBtnSwitch3.setSelected(true);
//                os.write("c".getBytes());
                tBtnSwitch3.setDisable(true);
                String time = txtTime3.getText();
                time3 = convertTimeToInt(time.split(":"));
                lblCountdown3.setText(time);
                timer3 = new Timer(1000, new TimerListener(time3, lblCountdown3, btnStartCountdown3, btnResetCountdown3, tBtnSwitch3, 3));
                timer3.start();
                btnStartCountdown3.setDisable(true);
                btnResetCountdown3.setDisable(false);
            } else {
                new Alert(Alert.AlertType.ERROR, "Deley time is zero seconds. Please set deley time correctly.", ButtonType.OK).show();
                txtTime3.requestFocus();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "You can only input time HH:MM:SS and maximum delay time you can set is 1 day and that is 23:59:59. Please set deley time correctly.", ButtonType.OK).show();
            txtTime3.requestFocus();
        }
    }

    @FXML
    private void btnResetCountdown3Action(ActionEvent event) {
        tBtnSwitch3.setDisable(false);
        timer3.stop();
        lblCountdown3.setText("Reset");
        txtTime3.setText("00:00:00");
        btnStartCountdown3.setDisable(false);
        btnResetCountdown3.setDisable(true);

    }

    @FXML
    private void btnStartCountdown4Action(ActionEvent event) throws Exception {
        if (Pattern.compile("^[0-2][0-3]:[0-5][0-9]:[0-5][0-9]$").matcher(txtTime4.getText()).matches()) {
            if (!txtTime4.getText().equals("00:00:00")) {
                tBtnSwitch4.setSelected(true);
//                os.write("d".getBytes());
                tBtnSwitch4.setDisable(true);
                String time = txtTime4.getText();
                time4 = convertTimeToInt(time.split(":"));
                lblCountdown4.setText(time);
                timer4 = new Timer(1000, new TimerListener(time4, lblCountdown4, btnStartCountdown4, btnResetCountdown4, tBtnSwitch4, 4));
                timer4.start();
                btnStartCountdown4.setDisable(true);
                btnResetCountdown4.setDisable(false);
            } else {
                new Alert(Alert.AlertType.ERROR, "Deley time is zero seconds. Please set deley time correctly.", ButtonType.OK).show();
                txtTime4.requestFocus();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "You can only input time HH:MM:SS and maximum delay time you can set is 1 day and that is 23:59:59. Please set deley time correctly.", ButtonType.OK).show();
            txtTime4.requestFocus();
        }
    }

    @FXML
    private void btnResetCountdown4Action(ActionEvent event) {
        tBtnSwitch4.setDisable(false);
        timer4.stop();
        lblCountdown4.setText("Reset");
        txtTime4.setText("00:00:00");
        btnStartCountdown4.setDisable(false);
        btnResetCountdown4.setDisable(true);
    }

    public int[] convertTimeToInt(String[] time) {
        int[] converted = new int[time.length];
        for (int i = 0; i < time.length; i++) {
            converted[i] = Integer.valueOf(time[i]);
        }
        return converted;
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

    private class TimerListener implements ActionListener {

        int[] timeX;
        Label lblCountdownX;
        Button btnStartCountdownX;
        Button btnResetCountdownX;
        ToggleButton switchX;
        int switchNo;

        public TimerListener(int[] timeX, Label lblCountdownX, Button btnStartCountdownX, Button btnResetCountdownX, ToggleButton switchX, int switchNo) {
            this.timeX = timeX;
            this.lblCountdownX = lblCountdownX;
            this.btnStartCountdownX = btnStartCountdownX;
            this.btnResetCountdownX = btnResetCountdownX;
            this.switchX = switchX;
            this.switchNo = switchNo;
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (timeX[HOUR] == 0 && timeX[MIN] == 0 && (timeX[SEC] == 1 || timeX[SEC] == 0)) {
                        lblCountdownX.setText("End");
                        btnStartCountdownX.setDisable(false);
                        btnResetCountdownX.setDisable(true);
                        switchX.setSelected(false);
                        switchX.setDisable(false);
                        switch (switchNo) {
                            case 1: {
//                                try {
//                                    os.write("e".getBytes());
//                                } catch (IOException ex) {
//                                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
//                                } 
                                timer1.stop();
                                break;
                            }
                            case 2: {
//                                try {
//                                    os.write("f".getBytes());
//                                } catch (IOException ex) {
//                                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
//                                }
                                timer2.stop();
                                break;
                            }
                            case 3: {
//                                try {
//                                    os.write("g".getBytes());
//                                } catch (IOException ex) {
//                                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
//                                }
                                timer3.stop();
                                break;
                            }
                            case 4: {
//                                try {
//                                    os.write("h".getBytes());
//                                } catch (IOException ex) {
//                                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
//                                }
                                timer4.stop();
                                break;
                            }

                        }
                    } else if (timeX[MIN] == 0 && (timeX[SEC] == 1 || timeX[SEC] == 0)) {
                        timeX[HOUR] -= 1;
                        timeX[SEC] = 59;
                        timeX[MIN] = 59;
                        displayTime(timeX[HOUR], timeX[MIN], timeX[SEC], lblCountdownX);
                    } else if (timeX[SEC] > 0) {
                        timeX[SEC] -= 1;
                        displayTime(timeX[HOUR], timeX[MIN], timeX[SEC], lblCountdownX);
                    } else if (timeX[SEC] == 0) {
                        timeX[SEC] = 59;
                        timeX[MIN] -= 1;
                        displayTime(timeX[HOUR], timeX[MIN], timeX[SEC], lblCountdownX);
                    }
                    lblCountdownX.setTextFill(timeX[HOUR] == 0 && timeX[MIN] == 0 && timeX[SEC] <= 6 ? Paint.valueOf("RED") : Paint.valueOf("BLACK"));
                }
            });
        }
    }

    public void displayTime(int hours, int min, int sec, Label lblCountdownX) {
        String hour = String.format("%02d", hours);
        String minute = String.format("%02d", min);
        String second = (String.format("%02d", sec));
        lblCountdownX.setText(hour + ":" + minute + ":" + second);
    }

}
