package com.example.bluetoothkotlinandroidstudiotutorial

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import java.util.*
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetoothkotlinandroidstudiotutorial.ControlActivity.Companion.m_isConnected
import com.example.bluetoothkotlinandroidstudiotutorial.ControlActivity.Companion.m_myUUID
import com.example.bluetoothkotlinandroidstudiotutorial.ControlActivity.Companion.m_progress
import kotlinx.android.synthetic.main.activity_control.*
import java.io.IOException
import java.lang.Integer.toHexString

// https://developer.android.google.cn/guide/topics/connectivity/bluetooth/connect-bluetooth-devices?hl=zh-cn

class ControlActivity: AppCompatActivity() {
    companion object {
        var m_myUUID: UUID = UUID.fromString("00002A1F-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String

        lateinit var macAddress: String

        val tempPos1: Int = 10
        val tempPos2: Int = 11
        val humiPos: Int = 12
        val batPerPos: Int = 13
        val batVPos1: Int = 14
        val batVPos2: Int = 15
        val cntPos: Int = 16
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        m_address = intent.getStringExtra(MainActivity.EXTRA_ADDRESS).toString()
        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        btn_Disconnect.setOnClickListener {
            disconnect()
            Toast.makeText(this, "Disconnecting...", Toast.LENGTH_LONG).show()
        }

        btn_getData.setOnClickListener{
            Log.d("BLE", "onclick")
            Toast.makeText(this, "Scanning for broadcasted data...", Toast.LENGTH_LONG).show()
            macAddress = ed_MACADDRESS.text.toString()
            m_bluetoothAdapter.bluetoothLeScanner.startScan(scanCallback)
        }
    }

    var scanCallback: ScanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device: BluetoothDevice = result.getDevice()
            val scanRecord: ByteArray? = result.getScanRecord()?.getBytes()
            val rssi: Int = result.getRssi()


//ToDo: Add other sensor?
           //if (device.address == "A4:C1:38:A6:AF:D1") {
            if (device.address == macAddress)  {
                Log.d("BLE", "Scan")
                Log.d("BLE", device.address)
                Log.d("BLE", result.scanRecord.toString())

                val entries = result.scanRecord?.serviceData?.entries
                if (entries != null) {
                    for (entry in entries) {
                        Log.d("Entry", entry.toString())
                        Log.d("Entry KEY", entry.key.toString())
                        Log.d("Entry VALUE", entry.value.contentToString())
                        //txt_receivedData.text = entry.value.contentToString()
                    }

                    val tempHex: ByteArray = byteArrayOf(scanRecord!![tempPos1], scanRecord!![tempPos2])
                    val humiHex: ByteArray = byteArrayOf(scanRecord!![humiPos])
                    val batVHex: ByteArray = byteArrayOf(scanRecord!![batVPos1], scanRecord!![batVPos2])

                    val t = bytesToHex(tempHex)
                    val h = bytesToHex(humiHex)
                    val b = bytesToHex(batVHex)

                    val tempCDec: Double = (Integer.parseInt(t,16)).toDouble()/10
                    val tempFDec: Double = ((tempCDec * 1.8) + 32)
                    val humiDec: Int = Integer.parseInt(h,16)
                    val batVDec: Double = (Integer.parseInt(b, 16)).toDouble()/1000

                    txt_tempData.text = String.format("%.2f", tempFDec)
                    txt_humiData.text = humiDec.toString()
                    txt_batVData.text = String.format("%.3f",batVDec)
                }
            }
        }
            // Search through raw data for the type identifier 0xFF, decode
            // the following bytes over the encoded packet length ...
            // yourCallback.onScanResult(device, scanRecord, rssi);


        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d("BLE", "Error $errorCode")
        }
    }

    private val hexArray = "0123456789ABCDEF".toCharArray()

    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = bytes[j].toInt() and 0xFF

            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    private fun disconnect() {
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }


}//ControlActivity