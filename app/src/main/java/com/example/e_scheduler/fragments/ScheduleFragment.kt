package com.example.e_scheduler.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.e_scheduler.R
import com.example.e_scheduler.entity.Schedule
import com.example.e_scheduler.adapter.ScheduleAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_schedule.fab_add_note
import kotlinx.android.synthetic.main.fragment_schedule.lv_notes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class ScheduleFragment : Fragment(R.layout.fragment_schedule) {

    private val schedules = FirebaseFirestore.getInstance().collection("schedule")

    private lateinit var adapter: ScheduleAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUpdatedList()

        fab_add_note.setOnClickListener {

            val dialog = Dialog(requireActivity())
            dialog.setContentView(R.layout.add_edit_note_dialog)
            val noteTitle = dialog.findViewById<TextInputEditText>(R.id.et_note_title)
            val noteSubTitle = dialog.findViewById<TextInputEditText>(R.id.et_note_sub_title)
            val subTitle = dialog.findViewById<TextInputLayout>(R.id.sub_title)
            val noteDescription = dialog.findViewById<TextInputEditText>(R.id.et_note_description)
            val btnOk = dialog.findViewById<TextView>(R.id.btn_ok)
            val btnCancel = dialog.findViewById<TextView>(R.id.btn_cancel)

            noteSubTitle.visibility = View.GONE
            subTitle.visibility = View.GONE
            btnOk.setOnClickListener {
                if (noteTitle.text.toString().isEmpty() or noteDescription.text.toString()
                        .isEmpty()
                ) {
                    Toast.makeText(
                        requireActivity(),
                        "Please enter all fields\nAll fields are required",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val id = UUID.randomUUID().toString()
                    val schedule = Schedule(
                        uid = id,
                        title = noteTitle.text?.trim().toString(),
                        description = noteDescription.text?.trim().toString(),
                        owner = Firebase.auth.currentUser!!.uid,
                    )

                    CoroutineScope(Dispatchers.Main).launch {
                        schedules.document(id).set(schedule).await()
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
            val abc = schedules.whereEqualTo("owner",Firebase.auth.currentUser?.uid).get().await().toObjects(
                Schedule::class.java) as ArrayList
            adapter = ScheduleAdapter(requireContext(), abc)
            lv_notes.adapter = adapter
        }
    }
}