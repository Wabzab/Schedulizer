package com.example.schedulizer

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TagsFragment : Fragment(R.layout.fragment_tags) {

    lateinit var tagList : MutableList<Tag>
    lateinit var rvTags : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvTags = view.findViewById(R.id.rvTags)
        tagList = mutableListOf()

        Log.d(TAG, "Tags started")
        val db = Firebase.firestore
        db.collection("Tags")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    tagList.add(Tag(
                        document.id,
                        document.get("name") as String,
                        document.get("colour") as String
                    ))
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                val adapter = TagAdapter(tagList)
                rvTags.adapter = adapter
                rvTags.layoutManager = LinearLayoutManager(this.context)
                Log.d(TAG, tagList.toString())
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

    }

}