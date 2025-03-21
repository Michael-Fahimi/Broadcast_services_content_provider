package com.example.myapplication
import android.content.Intent
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController


@Composable
fun MusicPlayerScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: MusicViewModel = viewModel()
    var isBound by remember { mutableStateOf(false) }


    val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            viewModel.musicService = (service as MusicService.MusicBinder).getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    DisposableEffect(Unit) {
        val intent = Intent(context, MusicService::class.java)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        context.startService(intent)

        onDispose {
            if (isBound) context.unbindService(connection)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { viewModel.previous() }) {
                Text("Previous")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = { viewModel.playPause() }) {
                Text(if (viewModel.isPlaying.value == true) "Pause" else "Play")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = { viewModel.next() }) {
                Text("Next")
            }




        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("contacts") }) {Text("Contacts")}
    }
}