# ReceiveBluetoothData_KotlinAndroid
This is a test program designed to receive broadcasted bluetooth data from an environmental sensor (Mijia Bluetooth Temperature/Humidity Sensor with LCD Display) and display it on an Android app (Kotlin)

## Uses:  
https://github.com/atc1441/ATC_MiThermometer
To develop custom firmware for the Mijia sensor.

## Based on:  
https://www.youtube.com/watch?v=Oz4CBHrxMMs 
Bluetooth Tutorial

Continuously scans for updated data after 'Get Data' is clicked.

## Requires:  
MAC ADDRESS of device (must be updated to use different device)

## Notes:  
This takes in the data string, breaks it into a scanResults byte array, then pulls out the data I want (temperature, humidity, battery voltage) and converts it to something readable.
The data string is (I think) 21 bytes long.
[0..3] - Advertising status?
[4..9] - MAC Address
[10..11] - Temperature (Concat Hex, convert to Dec, divide by 10 = deg C)
[12] - Humidity (convert to Dec = %rH)
[13] - Battery % (convert to Dec = %)
[14..15] - Battery Voltage ( Concat Hex, convert to Dec, divide by 1000 = V)
[16] - Counter? (Counts each advertising, increments by 1?)  

Useful to use nRF Connect App to read/verify data, addresses, services, characteristics, etc.   

## Recall:  
Android Studio Emulators are not Bluetooth-enabled (except maybe the default Pixel 2? I'm hazy on this).  
You must run/debug on a Bluetooth-enabled device such as a Tablet.

CAUTION: N00B  
This is a learning application to teach myself Android/Kotlin/Bluetooth from scratch. 
"Best practicies" (or even "correct practices" may not be implemented, and all feedback/guidance is welcome)
Also, I used a lot of deprecated functions. ¯\_(ツ)_/¯
