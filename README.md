# ReceiveBluetoothData_KotlinAndroid
This is a test program designed to receive broadcasted bluetooth data from an environmental sensor (Mijia Bluetooth Temperature/Humidity Sensor with LCD Display) and display it on an Android app (Kotlin)

Uses: https://github.com/atc1441/ATC_MiThermometer
To develop custom firmware for the Mijia sensor.

Based on: https://www.youtube.com/watch?v=Oz4CBHrxMMs 
Bluetooth Tutorial

Continuously scans for updated data after 'Get Data' is clicked.

Requires:
MAC ADDRESS of device (must be updated to use different device)

Recall:
Android Studio Emulators are not Bluetooth-enabled (except maybe the default Pixel 2? I'm hazy on this).  
You must run/debug on a Bluetooth-enabled device such as a Tablet.

CAUTION: N00B
This is a learning application to teach myself Android/Kotlin/Bluetooth from scratch. 
"Best practicies" (or even "correct practices" may not be implemented, and all feedback/guidance is welcome)
Also, I used a lot of deprecated functions. ¯\_(ツ)_/¯
