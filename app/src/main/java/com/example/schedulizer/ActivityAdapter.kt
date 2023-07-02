package com.example.schedulizer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import java.util.Date

class ActivityAdapter(
    private var activities: List<Activity>
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var base_activities = activities

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.tvActName).text = activities[position].name
            findViewById<ImageButton>(R.id.btnActDelete).setOnClickListener {
                DatabaseManager.removeActivity(activities[position])
                base_activities = base_activities.filter { activity: Activity ->
                    base_activities[position].id != activity.id
                }
                activities = base_activities
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    fun filterActivitiesByDate(start: Date, end: Date) {
        activities = base_activities.filter { activity: Activity ->
            activity.start.toDate() > start && activity.start.toDate() < end
        }
        notifyDataSetChanged()
    }
}