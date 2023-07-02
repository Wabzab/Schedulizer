package com.example.schedulizer

import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date

class ActivityList : Fragment() {

    lateinit var actList : MutableList<Activity>
    lateinit var rvActList : RecyclerView
    lateinit var btnActFilter : Button
    lateinit var mainActivity: MainActivity
    lateinit var adapter: ActivityAdapter
    lateinit var btnStartDate: Button
    lateinit var btnEndDate: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actList = mutableListOf()
        rvActList = view.findViewById(R.id.rvActList)
        btnActFilter = view.findViewById(R.id.btnActFilter)
        btnStartDate = view.findViewById(R.id.btnActFilterStart)
        btnEndDate = view.findViewById(R.id.btnActFilterEnd)
        mainActivity = requireActivity() as MainActivity
        var startDate = Calendar.getInstance()
        var endDate = Calendar.getInstance()

        var startDatePicker = DatePickerDialog(mainActivity)
        startDatePicker.setOnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            btnStartDate.text = "$year/$month/$day"
            startDate.set(year, month, day)
        }
        var endDatePicker = DatePickerDialog(mainActivity)
        endDatePicker.setOnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            btnEndDate.text = "$year/$month/$day"
            endDate.set(year, month, day)
        }

        var activityTask = DatabaseManager.getAllActivities()
        activityTask.addOnSuccessListener { result ->
            for (document in result) {
                actList.add(Activity(
                    document.id,
                    document.get("name") as String,
                    document.get("desc") as String,
                    document.get("start") as Timestamp,
                    document.get("duration") as Long,
                    document.get("tagID") as String,
                    document.get("userID") as String,
                ))
            }
            adapter = ActivityAdapter(actList)
            rvActList.adapter = adapter
            rvActList.layoutManager = LinearLayoutManager(this.context)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        btnStartDate.setOnClickListener {
            startDatePicker.show()
        }

        btnEndDate.setOnClickListener {
            endDatePicker.show()
        }

        btnActFilter.setOnClickListener {
            val startText = btnStartDate.text
            val endText = btnEndDate.text

            if (startText.isNotEmpty() && endText.isNotEmpty()) {
                adapter.filterActivitiesByDate(startDate.time, endDate.time)
            }
        }
    }
}