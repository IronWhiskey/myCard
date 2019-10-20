package com.michaelguerrero.android.mycard.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import com.michaelguerrero.android.mycard.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "LifeCycle"


    // function that handles onCreate of activity lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Sets the columns to retrieve for the user profile
//        var projection = arrayOf(
//            ContactsContract.Profile._ID,
//            ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
//            ContactsContract.Profile.LOOKUP_KEY,
//            ContactsContract.Profile.PHOTO_THUMBNAIL_URI,
//        )

        // Retrieves the profile from the Contacts Provider
//        val profileCursor = contentResolver.query(
//            ContactsContract.Profile.CONTENT_URI,
//            projection,
//            null,
//            null,
//            null
//        )

        // debugging and testing statements //
        Log.d("TAG", "testing")
//        Log.d("TAG", projection[1])

            // sets an intent to call the ContactDetailsActivity after share_button has been clicked
        share_button.setOnClickListener{
            val myIntent = Intent(this, ContactDetailsActivity::class.java)
            startActivity(myIntent)
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
    }
}
