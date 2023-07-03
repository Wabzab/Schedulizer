package com.example.schedulizer

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class ActivityAdapter(
    private var activities: List<Activity>
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgActIcon: ImageView = itemView.findViewById(R.id.imgActIcon)
        val tvActName: TextView = itemView.findViewById(R.id.tvActName)
        val tvActDate: TextView = itemView.findViewById(R.id.tvActDate)
        val tvActDesc: TextView = itemView.findViewById(R.id.tvActDesc)
        val btnActDelete: ImageButton = itemView.findViewById(R.id.btnActDelete)
    }

    var baseActivities = activities

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val currentItem = activities[position]
        holder.tvActName.text = currentItem.name
        holder.tvActDate.text = formatDate(currentItem.start)
        holder.tvActDesc.text = currentItem.desc
        if (currentItem.imageUri.isNotEmpty()) {
            holder.imgActIcon.visibility = View.VISIBLE
            holder.imgActIcon.setImageURI(Uri.parse(currentItem.imageUri))
        } else {
            holder.imgActIcon.visibility = View.GONE
        }
        holder.btnActDelete.setOnClickListener {
            DatabaseManager.removeActivity(currentItem)
            baseActivities = baseActivities.filter { activity: Activity ->
                currentItem.id != activity.id
            }
            activities = baseActivities
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    fun filterActivitiesByDate(start: Date, end: Date) {
        activities = baseActivities.filter { activity: Activity ->
            activity.start.toDate() > start && activity.start.toDate() < end
        }
        notifyDataSetChanged()
    }

    private fun formatDate(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        return SimpleDateFormat("yyyy/MM/dd").format(date)
    }
}