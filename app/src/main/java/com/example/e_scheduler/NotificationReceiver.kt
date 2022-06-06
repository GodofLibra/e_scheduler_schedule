package com.example.e_scheduler

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.e_scheduler.fragments.channelId

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val index = intent?.getIntExtra("index", 0)
        val intentOpenActivity = Intent(context, NoteInfoActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("index", index)
        }
        val contentIntent = PendingIntent.getActivity(
            context,
            index!!,
            intentOpenActivity,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context!!, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentTitle(Receipes.receipesArray[index].title)
//            .setContentText(Receipes.receipesArray[index].description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
        with(NotificationManagerCompat.from(context)) {
            notify(index, builder.build())
        }
    }
}