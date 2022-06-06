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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


class ManageNoticeAdapter(var context: Context, var noteList: ArrayList<Notice>) : BaseAdapter() {
    private val notices = FirebaseFirestore.getInstance().collection("notice")

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
            LayoutInflater.from(context).inflate(R.layout.row_layout_manage_notice, parent, false)
        val noteTitle = view.findViewById<TextView>(R.id.note_title)
        val noteSubTitle = view.findViewById<TextView>(R.id.note_sub_title)
        val noteDescription = view.findViewById<TextView>(R.id.note_description)
        val btnDelete = view.findViewById<ImageButton>(R.id.btn_delete_notice)
        noteTitle.text = noteList[position].title
        noteSubTitle.text = noteList[position].subject
        noteDescription.text = noteList[position].description

        btnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                notices.document(noteList[position].uid).delete().await()
                noteList.remove(noteList[position])
                notifyDataSetChanged()
            }
        }

        return view
    }
}