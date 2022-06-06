package com.example.e_scheduler.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.e_scheduler.R
import com.example.e_scheduler.Receipes
import com.example.e_scheduler.entity.Schedule
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


class ScheduleAdapter(var context: Context, var noteList: ArrayList<Schedule>) : BaseAdapter() {
    private val schedules = FirebaseFirestore.getInstance().collection("schedule")

    override fun getCount(): Int {
        return noteList.size
    }

    override fun getItem(position: Int): Any {
        return noteList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout_schedule, parent, false)
        val noteTitle = view.findViewById<TextView>(R.id.note_title)
        val noteDescription = view.findViewById<TextView>(R.id.note_description)
        val btnDelete = view.findViewById<ImageButton>(R.id.btn_delete_schedule)
        noteTitle.text = noteList[position].title
        noteDescription.text = noteList[position].description

        btnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                schedules.document(noteList[position].uid).delete().await()
                noteList.remove(noteList[position])
                notifyDataSetChanged()
            }
        }

        return view
    }

}