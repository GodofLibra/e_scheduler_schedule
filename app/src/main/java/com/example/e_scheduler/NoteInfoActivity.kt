package com.example.e_scheduler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.util.*

class NoteInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_info)

        val customToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.custom_toolbar)
        setSupportActionBar(customToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        customToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val tvNoteTitle = findViewById<TextView>(R.id.tv_note_title)
        val tvNoteSubTitle = findViewById<TextView>(R.id.tv_note_sub_title)
        val tvNoteDescription = findViewById<TextView>(R.id.tv_note_description)
        val tvNoteTimeStamp = findViewById<TextView>(R.id.tv_note_time_stamp)
        val tvNoteReminderTime = findViewById<TextView>(R.id.tv_notes_reminder_time)
        val index = intent.getIntExtra("index", 0)
//        tvNoteTitle.text = Receipes.receipesArray[index].title
//        tvNoteSubTitle.text = Receipes.receipesArray[index].subTitle
//        tvNoteDescription.text = Receipes.receipesArray[index].description
//        val timeFormat = SimpleDateFormat("MMM, dd yyyy hh:mm:ss a", Locale.ENGLISH)
//        tvNoteTimeStamp.text = timeFormat.format(Receipes.receipesArray[index].timeStamp)
//        val time =
//            "Reminder at " + timeFormat.format(Receipes.receipesArray[index].modifiedTime.timeInMillis)
//        tvNoteReminderTime.text = time
    }
}