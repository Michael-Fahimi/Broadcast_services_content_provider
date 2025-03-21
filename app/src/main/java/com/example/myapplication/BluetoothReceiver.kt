package com.example.myapplication

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

//dynamic vs static receivers
// this is dynamic  the app is active-> it is destroyed when it is ondestroy
// static = always ( without the app running ) -> increase the battery (
class BluetoothReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        // here we are checking if we are dealing with the intent we think we have

        if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED)
        {
            val TurnOn = intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            println("Bluetooth is turned on lol")
            Log.d("BluetoothReceiver", "Bluetooth is turned on")
            println(TurnOn)
        }

    }

}