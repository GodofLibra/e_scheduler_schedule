package com.example.e_scheduler.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.e_scheduler.entity.Notice
import com.example.e_scheduler.R
import com.example.e_scheduler.Receipes
import com.example.e_scheduler.entity.Announcement
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


class ManageAnnouncementAdapter(var context: Context, var noteList: ArrayList<Announcement>) :
    BaseAdapter() {
    private val announcements = FirebaseFirestore.getInstance().collection("announcement")

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
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_layout_manage_ann, parent, false)
        val noteTitle = view.findViewById<TextView>(R.id.note_title)
        val noteSubTitle = view.findViewById<TextView>(R.id.note_sub_title)
        val noteDescription = view.findViewById<TextView>(R.id.note_description)
        val btnDelete = view.findViewById<ImageButton>(R.id.btn_delete_ann)
        noteTitle.text = noteList[position].title
        noteSubTitle.text = noteList[position].subject
        noteDescription.text = noteList[position].description

        btnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                announcements.document(noteList[position].uid).delete().await()
                noteList.remove(noteList[position])
                notifyDataSetChanged()
            }
        }

        return view
    }
}