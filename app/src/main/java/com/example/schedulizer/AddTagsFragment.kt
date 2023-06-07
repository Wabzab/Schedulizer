package com.example.schedulizer

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Dictionary


class AddTagsFragment : Fragment(R.layout.fragment_add_tags) {

    lateinit var tagName: EditText
    lateinit var spnColors: Spinner
    lateinit var btnCreate: Button
    lateinit var btnCancel: Button
    lateinit var mainActivity: MainActivity
    lateinit var user: DocumentReference

    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tagsFragment = TagsFragment()
        val db = Firebase.firestore

        tagName = view.findViewById(R.id.etTagName)
        spnColors = view.findViewById(R.id.spnColors)
        btnCreate = view.findViewById(R.id.btnCreateTag)
        btnCancel = view.findViewById(R.id.btnCancelCreate)
        mainActivity = requireActivity() as MainActivity

        val colors = mapOf<String, Int>(
            "Red" to Color.RED,
            "Green" to Color.GREEN,
            "Blue" to Color.BLUE,
            "Yellow" to Color.YELLOW,
        )
        var color = colors["Red"]

        db.collection("Users")
            .whereEqualTo("Name", SaveSharedPreferences.getUserName(mainActivity))
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    user = snapshot
                        .documents[0]
                        .reference
                }
            }

        // Handle Spinner Item Selection
        spnColors.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                color = colors[adapterView?.getItemAtPosition(position)]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Do nothing when select nothing
            }

        }

        // Handle Tag Creation
        btnCreate.setOnClickListener {
            if (tagName.text.isNotEmpty()) {
                val tag = hashMapOf(
                    "name" to tagName.text.toString(),
                    "colour" to color.toString(),
                    "userId" to user.id,
                )
                DatabaseManager.addTag(tag)
                mainActivity.setFrameFragment(tagsFragment)
            }
        }

        // Handle Cancel Create
        btnCancel.setOnClickListener {
            mainActivity.setFrameFragment(tagsFragment)
        }
    }
}