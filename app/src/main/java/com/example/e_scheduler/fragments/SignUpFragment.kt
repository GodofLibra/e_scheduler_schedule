package com.example.e_scheduler.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_scheduler.R
import com.example.e_scheduler.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val auth = FirebaseAuth.getInstance()
    private val users = FirebaseFirestore.getInstance().collection("users")

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


        if (auth.currentUser != null) {
            findNavController().navigate(R.id.homeFragment)
        }

        setStatusBarTransparent()


        var role = ""
        radio_group_roles.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rb_admin -> role = "Admin"
                R.id.rb_proff -> role = "Professor"
                R.id.rb_student -> role = "Student"
            }
        }

        btn_register.setOnClickListener {
            val name = et_register_uname.text.toString().trim()
            val enrNum = et_enr_number.text.toString().trim()
            val email = et_register_email.text.toString().trim()
            val password = et_register_password.text.toString().trim()
            val cPassword = et_register_cpassword.text.toString().trim()
            val city = et_register_city.text.toString().trim()

            when {
                name.isEmpty() or enrNum.isEmpty() or email.isEmpty() or
                        password.isEmpty() or cPassword.isEmpty() -> {
                    Toast.makeText(
                        requireActivity(),
                        "All fields are required!\nPlease fill all details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                password != cPassword -> {
                    Toast.makeText(
                        requireActivity(),
                        "Password and Confirm password must be same",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                role == "" -> {
                    Toast.makeText(requireActivity(), "Select Role", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            auth.createUserWithEmailAndPassword(email, password).await()
                            if (auth.currentUser != null) {
                                val uid = auth.currentUser?.uid!!
                                val user = User(
                                    uid = uid,
                                    userName = name,
                                    enrNo = enrNum,
                                    city = city,
                                    email = email,
                                    role = role
                                )
                                users.document(uid).set(user).await()

                                auth.currentUser?.updateProfile(
                                    userProfileChangeRequest {
                                        displayName = name
                                    }
                                )

                                Toast.makeText(
                                    requireActivity(),
                                    "Registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                findNavController().navigate(R.id.loginActivity)
                            } else {
                                Toast.makeText(
                                    requireActivity(),
                                    "Something went wrong. Please try again later",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                requireActivity(),
                                e.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            }
        }
        tv_login.setOnClickListener {
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


