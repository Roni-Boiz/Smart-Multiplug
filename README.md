# Smart-Multiplug
In this repository I have build desktop application to control switches(relays) of Arduino system by using Bluetooth connection.

### Setup the environment 🛠
Before you run this project make sure you have satisfy following conditions,

1. Install Java version 1.8.0/(above) type `java -version` in cmd to verify
2. Install MySQL Server version 8.0.15/(above) type `mysql -V` or `SELECT VERSION()` to verify
3. Import this [Database](/smartmultiplug.sql) to your MySQL server (after import no need to keep that file in your project directory)
4. Try to open the project in your favourite text editor eg:- NetBeans IDE, if you don't see any warning or errors your are good to go

### Setting up the database connection 🔌
You need to Update following three variables in following [file](/src/db/DBConnection.java), line 23 of Source_Packages/db/DBConnection.java 
   - DB_NAME -> your database name eg:- smartmultiplug
   - DB_USER -> User in MySQL server, default "root"
   - DB_PASSWORD -> database user password, default ""

### Updating MAC address of BluetoothConnection.java 
MAC address is unique to every device so, the one I have used is not work of any other Bluetooth module,

1. To find the MAC address of your Bluetooth module please follow this [tutorial](https://www.mathworks.com/help/supportpkg/arduinoio/ug/instructions-on-pairing-bluetooth-device.html)
2. The value display in the red box *Bluetooth Device Address* of following tutorial is your MAC address for your Bluetooth Module
3. Replace the MAC with the value I have used in `"btspp://MAC:1;authenticate=false;encrypt=false;master=false"` in line 27 of Source_Packages/smartmultiplug/bluetooth/[BluetoothConnection.java](/src/smartmultiplug/bluetooth/BluetoothConnection.java)

♦ You can enable more feature like authentication and encryption by making options to `true`.

### Login to Smart Multiplug
To login you must enter username and password. All the passwords are hash using `md5()` hashing algorithm.
To make login process more convinient I have add defalut user. Default user credentials are,
- Username - `admin`
- Password - `1234`

In case if we forgot password of any user you can create one or user the test account that I have create to bypass the authentication.
Test account creadentials are,
- Username - `test`
- Password - `abc123`

♦ Before deploy the system you must remove test account that bypass authentication **line 84-91** in Source_Packages/smartmultiplug/controller/[LogInController.java](/src/smartmultiplug/bluetooth/LogInController.java)

### Run the project
There is different ways to test and run project
1. Open project in text editor(IDE) eg:-NetBeans IDE, press `F6` or `Play button`
2. Double click [jar file](/dist/SmartMultiplug.jar) in Smart-Multiplug/dist/SmartMultiplug.jar 

### Special Points
To run project out of the box I have comment several lines such as,
```Java
streamConnection = BluetoothConnection.getInstance().getConnection();
os = streamConnection.openOutputStream();
os.write();
```

Uncomment following lines when you are ready with your Arduino setup, Bluetooth module, relays and switches.
Line numbers are `199, 200, 216, 217, 235, 241, 255, 261, 274, 280, 293, 299, 351, 387, 421, 456, 535, 536-539, 544-548, 553-557, 562-566.`

Arduino code to turn on the switches are **a, b, c, d** bit patterns while to turn off them I have use **e, f, g, h** bit patterns.
|Switch NO| To Turn ON| To Turn OFF|
|---------|-----------|------------|
| Switch 1 | os.write("a".getBytes()) | os.write("e".getBytes()) |
| Switch 2 | os.write("b".getBytes()) | os.write("f".getBytes()) |
| Switch 3 | os.write("c".getBytes()) | os.write("g".getBytes()) |
| Switch 4 | os.write("d".getBytes()) | os.write("h".getBytes()) |

Update following values accordindly values you have used.

## User Interfaces

![1](https://github.com/user-attachments/assets/fa04975b-8145-4db0-824b-fda72fc6ff84)

![2](https://github.com/user-attachments/assets/2704bfab-f18f-453d-aef9-771cd021e0ef)

![3](https://github.com/user-attachments/assets/21f836df-4871-435e-8995-ed41aea13cb2)

![4](https://github.com/user-attachments/assets/391234bf-cb87-4ba2-b51d-3d8090c0b105)
