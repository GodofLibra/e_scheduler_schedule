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
import com.example.e_scheduler.entity.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


class ManageUserAdapter(var context: Context, var noteList: ArrayList<User>) : BaseAdapter() {
    private val users = FirebaseFirestore.getInstance().collection("users")

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
            LayoutInflater.from(context).inflate(R.layout.row_layout_manage_users, parent, false)
        val username = view.findViewById<TextView>(R.id.tv_username)
        val email = view.findViewById<TextView>(R.id.tv_email)
        val role = view.findViewById<TextView>(R.id.tv_role)
        val btnDelete = view.findViewById<ImageButton>(R.id.btn_delete_user)
        username.text = noteList[position].userName
        email.text = noteList[position].email
        role.text = noteList[position].role

        btnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                users.document(noteList[position].uid).delete().await()
                noteList.remove(noteList[position])
                notifyDataSetChanged()
            }
        }

        return view
    }
}