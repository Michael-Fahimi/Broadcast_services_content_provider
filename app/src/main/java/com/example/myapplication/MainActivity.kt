package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.IntentFilter
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {

    private val bluetoothReceiver = BluetoothReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(bluetoothReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
        setContent {
            MyApplicationTheme {
                MusicContactsApp()
            }
        }
    }
}

//class MainActivity : ComponentActivity() {
//    private var selectedContact: Contact? = null
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.ActivityResultLauncher(requestCode, resultCode, data)
//        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
//            data?.data?.let { contactUri ->
//                val contact = getContactDetails(this, contactUri)
//                selectedContact = contact
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MyApplicationTheme {
//                ContactsScreen()
//            }
//        }
//    }
//
//    companion object {
//        const val PICK_CONTACT_REQUEST = 1001
//    }
//}

@Composable
fun MusicContactsApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "music"
    ) {
        composable("music") {
            MusicPlayerScreen(navController)
        }
        composable("contacts") {
            ContactsScreen(navController)
        }
    }
}


@Composable
fun RequestContactPermission() {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, proceed with contacts access
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }
}

