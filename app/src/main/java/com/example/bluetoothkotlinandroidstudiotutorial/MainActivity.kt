package com.example.bluetoothkotlinandroidstudiotutorial

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //Bluetooth Adapter
    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var m_pairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1

    companion object {
        val EXTRA_ADDRESS: String = "Device Address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter == null) {
            Toast.makeText(this, "This device does not support bluetooth", Toast.LENGTH_LONG).show()
            return
        }
        if(!m_bluetoothAdapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
            img_bleIv.setImageResource(R.drawable.ic_ble_off)
        }else{
            img_bleIv.setImageResource(R.drawable.ic_ble_on)
        }

        btn_select_device_refresh.setOnClickListener{ pairedDeviceList() }


        //Turn on Bluetooth
        btn_BLE_On.setOnClickListener {
            if (m_bluetoothAdapter!!.isEnabled) {
                img_bleIv.setImageResource(R.drawable.ic_ble_on)
                Toast.makeText(this, "Bluetooth is already Enabled.", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH)
                img_bleIv.setImageResource(R.drawable.ic_ble_on)
            }
        }

        //Turn off Bluetooth
        btn_BLE_Off.setOnClickListener {
            if (!m_bluetoothAdapter!!.isEnabled) {
                img_bleIv.setImageResource(R.drawable.ic_ble_off)
                Toast.makeText(this, "Bluetooth is already Disabled.", Toast.LENGTH_LONG).show()
            } else {
                m_bluetoothAdapter!!.disable()
                Toast.makeText(this, "Turning Bluetooth Off...", Toast.LENGTH_LONG).show()
                img_bleIv.setImageResource(R.drawable.ic_ble_off)
            }
        }

    } //OnCreate

    private fun pairedDeviceList() {
        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
        val list : ArrayList<BluetoothDevice> = ArrayList()

        if (!m_pairedDevices.isEmpty()) {
            for (device: BluetoothDevice in m_pairedDevices) {
                list.add(device)
                Log.i("device", ""+device)
            }
        } else {
            Toast.makeText(this, "No paired Bluetooth devices found", Toast.LENGTH_LONG).show()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        select_device_list.adapter = adapter
        select_device_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val address: String = device.address

            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            startActivity(intent)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (m_bluetoothAdapter!!.isEnabled) {
                    img_bleIv.setImageResource(R.drawable.ic_ble_on)
                    Toast.makeText(this, "Bluetooth has been Enabled.", Toast.LENGTH_LONG).show()
                } else {
                    img_bleIv.setImageResource(R.drawable.ic_ble_off)
                    Toast.makeText(this, "Bluetooth has been Disabled.", Toast.LENGTH_LONG).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Bluetooth enabling has been Canceled.", Toast.LENGTH_LONG).show()
            }
        }
    } //OnActivityResult

}//MainActivity