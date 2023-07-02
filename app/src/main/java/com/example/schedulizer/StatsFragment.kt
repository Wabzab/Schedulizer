package com.example.schedulizer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import java.util.Calendar

class StatsFragment : Fragment() {

    lateinit var bcHours: BarChart
    lateinit var btnWeekUp: ImageButton
    lateinit var btnWeekDown: ImageButton
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        bcHours = view.findViewById(R.id.bcHours)
        btnWeekDown = view.findViewById(R.id.btnWeekDown)
        btnWeekUp = view.findViewById(R.id.btnWeekUp)

        var currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
        var activityList = mutableListOf<Activity>()

        val fetchActivities = DatabaseManager.getAllActivities()
        fetchActivities.addOnSuccessListener { result ->
            for (document in result) {
                activityList.add(Activity(
                    document.id,
                    document.get("name") as String,
                    document.get("desc") as String,
                    document.get("start") as Timestamp,
                    document.get("duration") as Long,
                    document.get("tagID") as String,
                    document.get("userID") as String,
                ))
            }
        }

        val onFetchActivities = Tasks.whenAll(fetchActivities)
        onFetchActivities.addOnSuccessListener {
            updateBarChart(bcHours, activityList, currentWeek)
        }

        btnWeekDown.setOnClickListener {
            currentWeek -= 1
            updateBarChart(bcHours, activityList, currentWeek)
        }

        btnWeekUp.setOnClickListener {
            currentWeek += 1
            updateBarChart(bcHours, activityList, currentWeek)
        }

    }

    private fun updateBarChart(chart: BarChart, activityList: List<Activity>, currentWeek: Int) {
        var day = Calendar.getInstance()
        var datas = mutableListOf<BarEntry>()

        for (x in 0..6) {
            day.set(Calendar.DAY_OF_YEAR, currentWeek * 7 + x)
            var totalDuration: Float = 0F
            for (act in activityList) {
                var date = Calendar.getInstance()
                date.time = act.start.toDate()
                if (date.get(Calendar.DAY_OF_YEAR) == day.get(Calendar.DAY_OF_YEAR)) {
                    Log.d("STATISTICS", "Doc Day: ${date.get(Calendar.DAY_OF_YEAR)}")
                    totalDuration += act.duration.toFloat()
                }
            }

            // Convert milliseconds to hours
            totalDuration /= 3600000F
            datas.add( BarEntry(
                x.toFloat(),
                totalDuration
            ))
        }

        val dataSet = BarDataSet(datas, "Hours")
        val barData = BarData(dataSet)
        chart.data = barData
        chart.invalidate()
    }

}