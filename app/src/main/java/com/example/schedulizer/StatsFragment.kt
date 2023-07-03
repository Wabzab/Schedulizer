package com.example.schedulizer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import java.util.Calendar
import kotlin.math.roundToInt

class StatsFragment : Fragment() {

    lateinit var bcHours: BarChart
    lateinit var btnWeekUp: ImageButton
    lateinit var btnWeekDown: ImageButton
    lateinit var mainActivity: MainActivity
    lateinit var tvStatsWeek: TextView
    lateinit var tvStatsYear: TextView

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
        tvStatsWeek = view.findViewById(R.id.tvStatsWeek)
        tvStatsYear = view.findViewById(R.id.tvStatsYear)

        styleBarChart(bcHours)

        var currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)-1
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
            tvStatsWeek.text = "Week: $currentWeek"
            tvStatsYear.text = "Year: ${Calendar.getInstance().get(Calendar.YEAR)}"
            updateBarChart(bcHours, activityList, currentWeek)
        }

        btnWeekUp.setOnClickListener {
            currentWeek += 1
            tvStatsWeek.text = "Week: $currentWeek"
            tvStatsYear.text = "Year: ${Calendar.getInstance().get(Calendar.YEAR)}"
            updateBarChart(bcHours, activityList, currentWeek)
        }

    }

    private fun updateBarChart(chart: BarChart, activityList: List<Activity>, currentWeek: Int) {
        var day = Calendar.getInstance()
        var datas = mutableListOf<BarEntry>()

        for (x in 1..7) {
            day.set(Calendar.DAY_OF_YEAR, currentWeek * 7 + x)
            var totalDuration: Float = 0F
            for (act in activityList) {
                var date = Calendar.getInstance()
                date.time = act.start.toDate()
                if (date.get(Calendar.DAY_OF_YEAR) == day.get(Calendar.DAY_OF_YEAR)) {
                    totalDuration += act.duration.toFloat()
                }
            }

            // Convert milliseconds to hours
            totalDuration /= 3600000F
            datas.add(BarEntry(
                day.get(Calendar.DAY_OF_YEAR).toFloat()-1,
                totalDuration
            ))
        }

        val dataSet = BarDataSet(datas, "Hours")
        val barData = BarData(dataSet)
        chart.data = barData
        barData.setDrawValues(false)
        chart.invalidate()
    }

    private fun styleBarChart(chart: BarChart) {
        chart.setBackgroundColor(resources.getColor(R.color.primary, null))
        chart.description.text = ""
        chart.setNoDataText("Fetching Data...")
        chart.setDrawValueAboveBar(false)
        var left = chart.axisLeft
        left.setDrawLabels(true)
        left.setDrawAxisLine(true)
        left.setDrawGridLines(false)
        left.setDrawZeroLine(true)
        left.axisMinimum = 0f
        left.textSize = 16f
        left.textColor = resources.getColor(R.color.secondary, null)
        left.valueFormatter = YAxisFormatter()
        chart.axisRight.isEnabled = false
        var bottom = chart.xAxis
        bottom.position = XAxis.XAxisPosition.BOTTOM
        bottom.textSize = 16f
        bottom.textColor = resources.getColor(R.color.secondary, null)
        bottom.setDrawGridLines(false)
        bottom.valueFormatter = XAxisFormatter()

    }

}

class XAxisFormatter : ValueFormatter() {

    private val days = listOf<String>("Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        var day = Calendar.getInstance()
        day.set(Calendar.DAY_OF_YEAR, value.toInt())
        return days.getOrNull(day.get(Calendar.DAY_OF_WEEK)-1) ?: value.toString()
    }
}

class YAxisFormatter : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        var rounded = (value * 100.0).roundToInt() / 100.0
        return "$rounded Hrs"
    }
}