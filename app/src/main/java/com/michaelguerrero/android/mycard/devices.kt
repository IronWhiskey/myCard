package com.michaelguerrero.android.mycard

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.*
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.ParcelUuid
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import java.io.IOException
import java.util.*
import android.view.ViewGroup
import android.widget.SimpleAdapter
import java.io.Serializable


class devices : AppCompatActivity() {

    val TAG = "LifeCycle"
    val REQUEST_ENABLE_BT = 1
    val NAME = "TEST_LED"
    val REQUEST_COARSE_LOCATION_PERMISSIONS = 121;


    var UUID = ""
    var bt_uuid = java.util.UUID.fromString("00000000-0000-0000-0000-000000000000")
    var deviceList = arrayListOf<BluetoothDevice>()
    var bluetoothAdapter: BluetoothAdapter? = null
    lateinit var arrayAdapter : ArrayAdapter<BluetoothDevice>



    // THREAD USED TO CONNECT AS A CLIENT TO BLUETOOTH SERVER
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(bt_uuid)
        }

        public override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter?.cancelDiscovery()

            mmSocket?.use { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
//                manageMyConnectedSocket(socket)
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }

    // Create a BroadcastReceiver for BLUETOOTH ACTIONS
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action
            var devName = "unknown"
            var devAddress = "unknown"
//            val uuid = arrayOf<String>()

            when(action) {
                // OCCURS WHEN THE BLUE TOOTH ADAPTER STATE CHANGES
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    var bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                    Log.d("TAG", "*********** BLUETOOTH ADAPTER STATE CHANGED *********** ")

                    when(bluetoothState) {
                        BluetoothAdapter.STATE_ON -> {
                            //Bluetooth is on, now you can perform your tasks
                            Log.d("TAG", "*********** SCANNING!!! *********** ")
                            bluetoothAdapter?.startDiscovery()
                        }
                    }
                }

                // OCCURS WHEN A NEW BLUETOOTH DEVICE IS FOUND DURING A DISCOVERY
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.d("TAG", "***************** DEVICE WAS FOUND ***************** ")
                    if (device.name != null){
                        devName = device.name
                    }
//                    Log.d("TAG", "DEVICE NAME:" + name)
                    if (device.address != null){
                        devAddress = device.address // MAC address
                    }
//                    Log.d("TAG", "DEVICE NAME:" + address)
                    var name = devName.plus("--").plus(devAddress)
                    Log.d("TAG", "DEVICE: ".plus(name))
                    deviceList.add(device)
                    arrayAdapter.notifyDataSetChanged()
                }

                // OCCURS WHEN A BLUETOOTH DISCONNECT REQUEST IS RECIEVED
                BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED -> {
//                    myBluethThread.cancel()
                    Log.d( "TAG","Bluetooth thread canceled")
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devices)
        var mListView = findViewById<ListView>(R.id.bt_devices)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceList)
        mListView.adapter = arrayAdapter
//        deviceList.add("Device 1")
//        deviceList.add("Device 2")
//        deviceList.add("Device 3")

        // Onclick Listener to engage next activity after selecting a device from the list
        mListView.setOnItemClickListener { parent, view, position, id ->
            val element = arrayAdapter.getItem(position) // The item that was clicked
            val duration = Toast.LENGTH_SHORT
            if(element.uuids != null) {
                UUID = element.uuids.toString()
                bt_uuid = java.util.UUID.fromString(UUID)
                var message = "Found Open Bluetooth Server Socket"
                var toast = Toast.makeText(applicationContext, message, duration)
                toast.show()

                // EGAGEING THREAD AND CONNECTING TO A BLUETOOTH SERVER DEVICE
//                val myBluethThread = ConnectThread(element)
//                myBluethThread.run()
            }
            else{
                var message = "Device has no Bluetooth Server Socket Open"
                var toast = Toast.makeText(applicationContext, message, duration)
                toast.show()
            }
        }

        /****************** SETTING UP BLUETOOTH ADAPTER FOR DEVICE DISCOVERY ******************/
        // Get BluetoothAdapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Log.d("TAG", "bluetooth not supported")
        }

        // Enable Bluetooth Adapter
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

//        // Register reciever when the bluetooth adapter state changes
//        val filter2 = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
//        registerReceiver(receiver, filter2)

        // setup intent to extend bluetooth discovery to 5 minutes
//        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
//            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
//        }
//        startActivity(discoverableIntent)

        // CHECKING AND REQUESTING PROPER PERMISSIONS
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_COARSE_LOCATION_PERMISSIONS)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        /****************** START DEVICE DISCOVERY ******************/
        if (bluetoothAdapter?.isDiscovering!!){
            bluetoothAdapter?.cancelDiscovery()
        }
        bluetoothAdapter?.startDiscovery()
    }


    // function that handles onStart of activity lifecycle
    override fun onStart() {
        Log.d(TAG, "${javaClass.simpleName} OnStart")
        super.onStart()
    }



    // function that handles onResume of activity lifecycle
    override fun onResume() {
        Log.d(TAG, "${javaClass.simpleName} OnResume")
        super.onResume()
    }



    // function that handles onRestart of activity lifecycle
    override fun onRestart() {
        Log.d(TAG, "${javaClass.simpleName} onRestart")
        super.onRestart()
    }



    // function that handles onPause of activity lifecycle
    override fun onPause() {
        Log.d(TAG, "${javaClass.simpleName} onPause")
        super.onPause()
    }



    // function that handles onStop of activity lifecycle
    override fun onStop() {
        Log.d(TAG, "${javaClass.simpleName} onStop")
        super.onStop()
    }



    // function that handles onDestroy of activity lifecycle
    override fun onDestroy() {
        Log.d(TAG, "${javaClass.simpleName} onDestroy")
        super.onDestroy()
        bluetoothAdapter?.cancelDiscovery()
        unregisterReceiver(receiver)
    }

}
