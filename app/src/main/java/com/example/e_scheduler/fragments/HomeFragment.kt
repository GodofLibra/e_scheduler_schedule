package com.example.e_scheduler.fragments

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_scheduler.*
import com.example.e_scheduler.entity.Announcement
import com.example.e_scheduler.entity.Notice
import com.example.e_scheduler.entity.Schedule
import com.example.e_scheduler.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val notices = FirebaseFirestore.getInstance().collection("notice")
    private val announcements = FirebaseFirestore.getInstance().collection("announcement")
    private val schedules = FirebaseFirestore.getInstance().collection("schedule")
    private val users = FirebaseFirestore.getInstance().collection("users")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cv_schedule.setOnClickListener {
            findNavController().navigate(R.id.scheduleFragment)
        }

        cv_Notice.setOnClickListener {
            findNavController().navigate(R.id.noticeFragment)
        }

        cv_ann.setOnClickListener {
            findNavController().navigate(R.id.announcementFragment)
        }



        CoroutineScope(Dispatchers.Main).launch {
            val user = users.document(FirebaseAuth.getInstance().currentUser!!.uid).get().await()
                .toObject(User::class.java)!!

            val navMenu: Menu = requireActivity().nav_view.menu
            navMenu.findItem(R.id.admin).isVisible = user.role == "Admin"
            navMenu.findItem(R.id.manageUsersFragment).isVisible = user.role == "Admin"
            navMenu.findItem(R.id.manageAnnouncementFragment).isVisible = user.role == "Admin"
            navMenu.findItem(R.id.manageNoticeFragment).isVisible = user.role == "Admin"

        }

        CoroutineScope(Dispatchers.Main).launch {

            progress_bar.isVisible = true
            parent_layout.isVisible = false

            val notice = notices.get().await().toObjects(Notice::class.java)
            var str = ""
            if (notice.size > 0) {
                if (notice.size > 3) {
                    for (n in notice) {
                        str += n.title + "\n"
                    }
                } else {
                    for (n in notice) {
                        str += n.title + "\n"
                    }
                }
            }
            tv_notices.text = str

            val ann = announcements.get().await().toObjects(Announcement::class.java)
            str = ""
            if (ann.size > 0) {
                if (ann.size > 3) {
                    for (a in ann) {
                        str += a.title + "\n"
                    }
                } else {
                    for (a in ann) {
                        str += a.title + "\n"
                    }
                }
            }
            tv_announcements.text = str


            val schedule =
                schedules.whereEqualTo("owner", Firebase.auth.currentUser?.uid).get().await()
                    .toObjects(
                        Schedule::class.java
                    )
            str = ""
            if (schedule.size > 0) {
                if (schedule.size > 3) {
                    for (a in schedule) {
                        str += a.title + "\n"
                    }
                } else {
                    for (a in schedule) {
                        str += a.title + "\n"
                    }
                }
            }
            tv_schedules.text = str

            progress_bar.isVisible = false
            parent_layout.isVisible = true
        }

    }
}