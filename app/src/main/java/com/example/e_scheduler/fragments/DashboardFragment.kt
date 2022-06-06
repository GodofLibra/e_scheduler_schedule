package com.example.e_scheduler.fragments

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_scheduler.R
import com.example.e_scheduler.entity.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    lateinit var heart: AnimationDrawable
    private val users = FirebaseFirestore.getInstance().collection("users")
    private val receipes = FirebaseFirestore.getInstance().collection("receipes")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarTransparent()


        CoroutineScope(Dispatchers.Main).launch {
            val user = users.document(Firebase.auth.currentUser!!.uid).get().await()
                .toObject(User::class.java)!!

            Log.d("TAG_DASHBOARD", "onCreate: $user")
            tv_user_name.text = user.userName
            tv_user_email.text = Firebase.auth.currentUser!!.email
            tv_name.text = user.userName
            tv_user_enr_number.text = user.enrNo
//            tvUserBio.text = user.bio
            tv_email_id.text = Firebase.auth.currentUser!!.email


        }


        btn_logout.setOnClickListener {

            Firebase.auth.signOut()

            findNavController().navigate(R.id.loginActivity)
        }

    }

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

