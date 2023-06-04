package com.example.schedulizer

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddTagsFragment : Fragment(R.layout.fragment_add_tags) {
    // TODO: Create UI and link input to Firestore

    lateinit var tagName: EditText
    lateinit var btnCreate: Button
    lateinit var mainActivity: MainActivity
    lateinit var user: DocumentReference

    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tagsFragment = TagsFragment()
        val db = Firebase.firestore

        tagName = view.findViewById(R.id.etTagName)
        btnCreate = view.findViewById(R.id.btnCreateTag)
        mainActivity = requireActivity() as MainActivity

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

        btnCreate.setOnClickListener {

            val tag = hashMapOf(
                "name" to tagName.text.toString(),
                "colour" to Color.BLUE.toString(),
                "userId" to user.id,
            )
            db.collection("Tags")
                .add(tag)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    mainActivity.setFrameFragment(tagsFragment)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

        }
    }
}