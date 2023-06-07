package com.example.schedulizer

import android.graphics.Color
import android.media.Image
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TagAdapter(
    private var tags: List<Tag>
) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.tvTagName).text = tags[position].name
            findViewById<ImageView>(R.id.imgColor).setColorFilter(tags[position].color.toInt())
            findViewById<ImageButton>(R.id.btnDelete).setOnClickListener {
                DatabaseManager.removeTag(tags[position])
                tags = tags.filter { tag: Tag ->
                    tags[position].name != tag.name
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return tags.size
    }
}