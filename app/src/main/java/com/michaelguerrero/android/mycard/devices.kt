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
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import java.io.IOException
import java.util.*

class devices : AppCompatActivity() {

    val TAG = "LifeCycle"
    val REQUEST_ENABLE_BT = 1
    val MY_UUID = UUID.randomUUID()
    val NAME = "TEST_LED"
    val REQUEST_COARSE_LOCATION_PERMISSIONS = 121;

    var deviceList = arrayListOf<String>()
    var bluetoothAdapter: BluetoothAdapter? = null
    lateinit var arrayAdapter : ArrayAdapter<String>



    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action
            val myBluethThread = AcceptThread()


            Log.d("TAG", "inside reciever")

            when(action) {
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

                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    Log.d("TAG", "***************** DEVICE WAS FOUND ***************** ")
                    val deviceName = device.name
                    Log.d("TAG", "DEVICE NAME:" + deviceName)
                    val deviceHardwareAddress = device.address // MAC address
                    Log.d("TAG", "DEVICE NAME:" + deviceHardwareAddress)
                    deviceList.add(device.name)
//                    myBluethThread.run()
                    arrayAdapter.notifyDataSetChanged()
                }

                BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED -> {
                    myBluethThread.cancel()
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
        deviceList.add("Device 1")
        deviceList.add("Device 2")
        deviceList.add("Device 3")
        deviceList.add("Device 4")

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
        unregisterReceiver(receiver)
    }


    private inner class AcceptThread : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID)
        }

        override fun run() {
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                Log.d("TAG", "inside while loop")
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()

                } catch (e: IOException) {
                    Log.e(TAG, "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    Log.d("TAG", "The try statement passed.")
                    manageMyConnectedSocket(it)
                    mmServerSocket?.close()
                    shouldLoop = false
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }

        fun manageMyConnectedSocket(param: BluetoothSocket) {
//            val bluetoothService = MyBluetoothService(BluetoothSocket)
            Log.d("TAG", "inside manageMyConnectedSocket")

        }
    }
}
