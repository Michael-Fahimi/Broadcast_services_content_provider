package com.example.myapplication
import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var currentSongIndex = 0
    private val songs = listOf(R.raw.song1, R.raw.song2, R.raw.song3)

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent): IBinder = MusicBinder()

    @SuppressLint("ForegroundServiceType")
    fun play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex])
            mediaPlayer?.setOnCompletionListener { next() }
        }
        mediaPlayer?.start()
        startForeground(1, createNotification())
    }

    fun pause() {
        mediaPlayer?.pause()

        stopForeground(false)
    }

    fun next() {
        currentSongIndex = (currentSongIndex + 1) % songs.size
        resetMediaPlayer()
        play()
    }

    fun previous() {
        currentSongIndex = (currentSongIndex - 1).mod(songs.size)
        resetMediaPlayer()
        play()
    }

    private fun resetMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun createNotification(): Notification {
        val channelId = "music_channel"
        val channel = NotificationChannel(
            channelId,
            "Music Player",
            NotificationManager.IMPORTANCE_LOW
        ).apply { lockscreenVisibility = Notification.VISIBILITY_PUBLIC }

        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Now Playing")
            .setContentText("Song ${currentSongIndex + 1}")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        resetMediaPlayer()
    }
}