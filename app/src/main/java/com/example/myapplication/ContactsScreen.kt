package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ContactsScreen(navController: NavHostController) {
    val context = LocalContext.current
    var selectedContact by remember { mutableStateOf<Contact?>(null) }

    // Contact picker launcher
    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { contactUri ->
        if (contactUri != null) {
            val contact = getContactDetails(context, contactUri)
            selectedContact = contact
        }
    }

    // Request permission
    RequestContactPermission()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            contactPickerLauncher.launch(null)
        }) {
            Text("Pick a Contact")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedContact != null) {
            Text("Selected Contact: ${selectedContact!!.name}")
            Text("Phone: ${selectedContact!!.phone}")
        }
    }
}

@SuppressLint("Range")
fun getContactDetails(context: Context, contactUri: Uri): Contact? {
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(contactUri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
            val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

            // Fetch phone number
            val phoneCursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                arrayOf(id),
                null
            )
            phoneCursor?.use { pc ->
                if (pc.moveToFirst()) {
                    val phone = pc.getString(pc.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    return Contact(name, phone)
                }
            }
        }
    }
    return null
}



data class Contact(val name: String, val phone: String)