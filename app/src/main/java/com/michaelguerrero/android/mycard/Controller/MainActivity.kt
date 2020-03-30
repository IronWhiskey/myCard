package com.michaelguerrero.android.mycard.Controller
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.michaelguerrero.android.mycard.R
import kotlinx.android.synthetic.main.activity_main.*
import android.content.pm.PackageManager
import com.michaelguerrero.android.mycard.devices


class MainActivity : AppCompatActivity() {

    val TAG = "LifeCycle"


    // function that handles onCreate of activity lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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
            Log.d( "TAG","Cancel Button")

        }

        // terminate bluetooth thread
        off.setOnClickListener{
            Log.d( "TAG","Off Button")
        }
        /**
         ********* End OnClick Listeners *********
         * **/
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
    }
}
