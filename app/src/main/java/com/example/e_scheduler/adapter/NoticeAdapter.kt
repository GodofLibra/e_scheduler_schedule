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
import java.util.*


class NoticeAdapter(var context: Context, var noteList: ArrayList<Notice>) : BaseAdapter() {
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
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout_notice, parent, false)
        val noteTitle = view.findViewById<TextView>(R.id.note_title)
        val noteSubTitle = view.findViewById<TextView>(R.id.note_sub_title)
        val noteDescription = view.findViewById<TextView>(R.id.note_description)
        noteTitle.text = noteList[position].title
        noteSubTitle.text = noteList[position].subject
        noteDescription.text = noteList[position].description

        return view
    }

    private var onEditClickListener: ((Receipes) -> Unit)? = null

    fun setOnEditClickListener(listener: (Receipes) -> Unit) {
        onEditClickListener = listener
    }

    private var onDeleteClickListener: ((Receipes) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (Receipes) -> Unit) {
        onDeleteClickListener = listener
    }
}