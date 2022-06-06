package com.example.e_scheduler

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class AlarmService : Service() {
    var mp: MediaPlayer? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            mp = MediaPlayer.create(this, R.raw.alarm)
            mp?.start()
            Log.d("AlarmServiceTag", "onStartCommand: Alarm")
        }
        return START_STICKY
    }
    override fun onDestroy() {
        mp?.stop()
        super.onDestroy()
    }
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}