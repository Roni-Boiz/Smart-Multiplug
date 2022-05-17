/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartmultiplug.bluetooth;

import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.lang.Object;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author Ronila
 */
public class BluetoothConnection {

    private static BluetoothConnection bluetoothConnection;

    private StreamConnection streamConnection;

//    String hc05Url = "btspp://001891D7AB0B:1;authenticate=false;encrypt=false;master=false";
    private BluetoothConnection() throws IOException {
        streamConnection = (StreamConnection) Connector.open("btspp://001891D7AB0B:1;authenticate=false;encrypt=false;master=false");
    }

    public static BluetoothConnection getInstance() throws IOException {
        if (bluetoothConnection == null) {
            bluetoothConnection = new BluetoothConnection();
        }
        return bluetoothConnection;
    }

    public StreamConnection getConnection() {
        return streamConnection;
    }

    public void closeConnection() throws IOException {
        if (bluetoothConnection != null) {
            streamConnection.close();
            bluetoothConnection = null;
        }
    }

}
