package com.michaelguerrero.android.mycard.Controller
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.michaelguerrero.android.mycard.R
import kotlinx.android.synthetic.main.activity_main.*
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.UUID
import android.content.pm.PackageManager
import com.michaelguerrero.android.mycard.devices


class MainActivity : AppCompatActivity() {

    val TAG = "LifeCycle"
    val REQUEST_ENABLE_BT = 1
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    val MY_UUID = UUID.randomUUID()
    val NAME = "TEST_LED"
    val REQUEST_COARSE_LOCATION_PERMISSIONS = 121;


    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action
            val myBluethThread = AcceptThread()

            when(action) {
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

//                    myBluethThread.run()
                }

                BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED -> {
                    myBluethThread.cancel()
                    Log.d( "TAG","Bluetooth thread canceled")
                }
            }
        }
    }


    // function that handles onCreate of activity lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        // Get BluetoothAdapter
//        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
//        if (bluetoothAdapter == null) {
//            // Device doesn't support Bluetooth
//            Log.d("TAG", "bluetooth not supported")
//        }
//
//        // Enable Bluetooth Adapter
//        if (bluetoothAdapter?.isEnabled == false) {
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
//        }
//
//
//        // Register for broadcasts when a device is discovered.
//        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
//        registerReceiver(receiver, filter)
//
//        // setup intent to extend bluetooth discovery to 5 minutes
//        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
//            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
//        }
//
//
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED) {
//
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(this,
//                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
//                    REQUEST_COARSE_LOCATION_PERMISSIONS)
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        } else {
//            // Permission has already been granted
//        }
//
//        startActivity(discoverableIntent)

        // start discovering bluetooth devices
//        bluetoothAdapter?.startDiscovery()

        // debugging and testing statements //
//        Log.d("TAG", "testing")

        /**
         ********* All OnClick Listeners *********
         * **/
        // init bluetooth discovery process
        scan_button.setOnClickListener{
            val myIntent = Intent(this, devices::class.java)
            startActivity(myIntent)
//            bluetoothAdapter?.startDiscovery(
        }

        cancel_button.setOnClickListener{
            bluetoothAdapter?.cancelDiscovery()
            Log.d( "TAG","Canceling Bluetooth Discovery")

        }

        // terminate bluetooth thread
        off.setOnClickListener{
            bluetoothAdapter?.disable()
            Log.d( "TAG","Disabling Bluetooth Adapter")
        }
        /**
         ********* End OnClick Listeners *********
         * **/
    }



    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_COARSE_LOCATION_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
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
        // Don't forget to unregister the ACTION_FOUND receiver.
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
