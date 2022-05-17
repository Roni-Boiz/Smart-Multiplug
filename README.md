# Smart-Multiplug
In this repository I have build desktop application to control switches(relays) of Arduino system by using Bluetooth connection.

### Setup the environment
Before you run this project make sure you have satisfy following conditions,

1. Install Java version 1.8.0/(above) type `java -version` in cmd to verify
2. Install MySQL Server version 8.0.15/(above) type `mysql -V` or `SELECT VERSION()` to verify
3. Import this [Database](/smartmultiplug.sql) to your MySQL server (after import no need to keep that file in your project directory)
4. Try to open the project in your favourite text editor eg:- NetBeans IDE, if you don't see any warning or errors your are good to go

### Setting up the database connection
You need to Update following three variables in following [file](/src/db/DBConnection.java), line 23 of Source_Packages/db/DBConnection.java 
   - DB_NAME -> your database name eg:- smartmultiplug
   - DB_USER -> User in MySQL server, default "root"
   - DB_PASSWORD -> database user password, default ""

### Updating MAC address of BluetoothConnection.java
MAC address is unique to every device the one I have used is not work of any other Bluetooth module,

1. To find the MAC address of your Bluetooth module please follow this [tutorial](https://www.mathworks.com/help/supportpkg/arduinoio/ug/instructions-on-pairing-bluetooth-device.html)
2. The value display in the red box *Bluetooth Device Address* of following tutorial is your MAC address
3. Replace the MAC with the value you have in `"btspp://MAC:1;authenticate=false;encrypt=false;master=false"` in line 27 of Source_Packages/smartmultiplug/bluetooth/[BluetoothConnection.java](/src/smartmultiplug/bluetooth/BluetoothConnection.java)

♦ You can enable more feature like authentication and encryption by making them to true.
