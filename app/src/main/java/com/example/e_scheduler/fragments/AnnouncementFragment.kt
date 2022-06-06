package com.example.e_scheduler.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.e_scheduler.*
import com.example.e_scheduler.adapter.AnnouncementAdapter
import com.example.e_scheduler.entity.Announcement
import com.example.e_scheduler.entity.Notice
import com.example.e_scheduler.entity.User
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_announcement.fab_add_note
import kotlinx.android.synthetic.main.fragment_announcement.lv_notes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class AnnouncementFragment : Fragment(R.layout.fragment_announcement) {

    private val users = FirebaseFirestore.getInstance().collection("users")

    private val announcements = FirebaseFirestore.getInstance().collection("announcement")

    private lateinit var adapter: AnnouncementAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatusBarTransparent()


        getUpdatedList()

        CoroutineScope(Dispatchers.Main).launch {
            val user = users.document(FirebaseAuth.getInstance().currentUser!!.uid).get().await()
                .toObject(User::class.java)!!
            fab_add_note.isVisible = user.role.trim() != "Student"
        }

        fab_add_note.setOnClickListener {
            val dialog = Dialog(requireActivity())
            dialog.setContentView(R.layout.add_edit_note_dialog)
            val noteTitle = dialog.findViewById<TextInputEditText>(R.id.et_note_title)
            val noteSubTitle = dialog.findViewById<TextInputEditText>(R.id.et_note_sub_title)
            val noteDescription = dialog.findViewById<TextInputEditText>(R.id.et_note_description)
            val btnOk = dialog.findViewById<TextView>(R.id.btn_ok)
            val btnCancel = dialog.findViewById<TextView>(R.id.btn_cancel)

            btnOk.setOnClickListener {
                if (noteTitle.text.toString().isEmpty() or noteSubTitle.text.toString()
                        .isEmpty() or noteDescription.text.toString()
                        .isEmpty()
                ) {
                    Toast.makeText(
                        requireActivity(),
                        "Please enter all fields\nAll fields are required",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val id = UUID.randomUUID().toString()
                    val announcement = Announcement(
                        uid = id,
                        title = noteTitle.text?.trim().toString(),
                        subject = noteSubTitle.text?.trim().toString(),
                        description = noteDescription.text?.trim().toString(),
                        owner = Firebase.auth.currentUser!!.uid,
                    )

                    CoroutineScope(Dispatchers.Main).launch {
                        announcements.document(id).set(announcement).await()
                        getUpdatedList()
                    }
                    dialog.dismiss()
                }
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()

        }
    }

    private fun getUpdatedList() {
        CoroutineScope(Dispatchers.Main).launch {
            val abc = announcements.get().await().toObjects(Announcement::class.java) as ArrayList
            adapter = AnnouncementAdapter(requireContext(), abc)
            lv_notes.adapter = adapter
        }
    }

//    private fun createNotificationChannel() {
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val description = "Channel for sending notes notification"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(channelId, channelName, importance)
//            channel.description = description
//            channel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
//            channel.enableVibration(true)
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

    private fun setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT in 19..20) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
            }
        }
        if (Build.VERSION.SDK_INT >= 19) {
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            requireActivity().window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val winParameters = requireActivity().window.attributes
        if (on) {
            winParameters.flags = winParameters.flags or bits
        } else {
            winParameters.flags = winParameters.flags and bits.inv()
        }
        requireActivity().window.attributes = winParameters
    }
}