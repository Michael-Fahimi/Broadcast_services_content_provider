package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MusicViewModel(application: Application) : AndroidViewModel(application) {
    var musicService: MusicService? = null
    val isPlaying = MutableLiveData(false)

    fun playPause() {
        musicService?.let {
            if (isPlaying.value == true) {
                it.pause()
            } else {
                it.play()
            }
            isPlaying.postValue(!isPlaying.value!!)
        }
    }

    fun next() {
        musicService?.next()
        isPlaying.postValue(true)
    }

    fun previous() {
        musicService?.previous()
        isPlaying.postValue(true)
    }
}