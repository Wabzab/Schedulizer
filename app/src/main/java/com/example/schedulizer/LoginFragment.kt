package com.example.schedulizer

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment(R.layout.fragment_login) {

    lateinit var btnLogin : Button
    lateinit var btnRegister: Button
    lateinit var etName: EditText
    lateinit var etPassword: EditText
    lateinit var tvError: TextView
    lateinit var mainActivity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activitiesFragment = ActivitiesFragment()
        val registrationFragment = RegistrationFragment()

        btnLogin = view.findViewById(R.id.btnLogin)
        btnRegister = view.findViewById(R.id.btnRegister)
        etName = view.findViewById(R.id.etName)
        etPassword = view.findViewById(R.id.etPassword)
        tvError = view.findViewById(R.id.tvLoginError)
        mainActivity = requireActivity() as MainActivity

        mainActivity.setDrawerEnabled(false)

        val db = Firebase.firestore

        btnLogin.setOnClickListener {
            tvError.visibility = TextView.INVISIBLE
            if (Utilities.isInputValid(etName.text.toString(), etPassword.text.toString())) {
                db.collection("Users")
                    .whereEqualTo("Name", etName.text.toString())
                    .whereEqualTo("Password", etPassword.text.toString())
                    .get()
                    .addOnSuccessListener { result ->
                        if (result.documents.isNotEmpty()) {
                            SaveSharedPreferences.setUserName(mainActivity, result.documents[0].get("Name") as String)
                            mainActivity.setHeaderUsername(SaveSharedPreferences.getUserName(mainActivity))
                            mainActivity.setDrawerEnabled(true)
                            mainActivity.setFrameFragment(activitiesFragment)
                        }
                        else {
                            tvError.visibility = TextView.VISIBLE
                        }
                    }
                    .addOnFailureListener {
                        Log.d(TAG, it.toString())
                    }

            }
        }

        btnRegister.setOnClickListener {
            mainActivity.setFrameFragment(registrationFragment)
        }
    }
}