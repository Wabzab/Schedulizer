package com.example.schedulizer

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


class RegistrationFragment : Fragment(R.layout.fragment_registration) {


    lateinit var btnRegister : Button
    lateinit var btnCancel: Button
    lateinit var etName: EditText
    lateinit var etPassword: EditText
    lateinit var tvError: TextView
    lateinit var mainActivity: MainActivity


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRegister = view.findViewById(R.id.btnRegisterUser)
        btnCancel = view.findViewById(R.id.btnCancelRegister)
        etName = view.findViewById(R.id.etNewName)
        etPassword = view.findViewById(R.id.etNewPassword)
        tvError = view.findViewById(R.id.tvRegisterError)
        mainActivity = requireActivity() as MainActivity

        val loginFragment = LoginFragment()

        btnRegister.setOnClickListener {
            tvError.visibility = TextView.INVISIBLE
            if (Utilities.isInputValid(etName.text.toString(), etPassword.text.toString())) {
                DatabaseManager.addUser(etName.text.toString(), etPassword.text.toString())
                mainActivity.setFrameFragment(loginFragment)
            }
            else {
                tvError.visibility = TextView.VISIBLE
            }
        }

        btnCancel.setOnClickListener {
            mainActivity.setFrameFragment(loginFragment)
        }
    }
}