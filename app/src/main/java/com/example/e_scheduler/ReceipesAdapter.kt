package com.example.e_scheduler

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class ReceipesAdapter(var context: Context, var noteList: ArrayList<Receipes>) : BaseAdapter() {
    private val receipes = FirebaseFirestore.getInstance().collection("receipes")

    override fun getCount(): Int {
        return noteList.size
    }
    override fun getItem(position: Int): Any {
        return noteList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout_notice, parent, false)
        val noteTitle = view.findViewById<TextView>(R.id.note_title)
        val noteSubTitle = view.findViewById<TextView>(R.id.note_sub_title)
        val noteDescription = view.findViewById<TextView>(R.id.note_description)
        noteTitle.text = noteList[position].receipeName
        noteSubTitle.text = noteList[position].typeOfReceipe
        noteDescription.text = noteList[position].receipeDescription

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