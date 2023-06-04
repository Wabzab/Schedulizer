package com.example.schedulizer

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await

class ActivitiesFragment : Fragment(R.layout.fragment_activities) {

    lateinit var btnTags: Button
    lateinit var mainActivity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragments this page can access
        val tagsFragment = TagsFragment()

        // Get button view
        btnTags = view.findViewById(R.id.btnTags)
        // Get parent activity of this fragment
        mainActivity = requireActivity() as MainActivity

        // Make button click change fragment
        btnTags.setOnClickListener {
            mainActivity.setFrameFragment(tagsFragment)
        }
    }
}