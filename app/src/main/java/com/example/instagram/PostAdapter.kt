package com.example.instagram

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(val context: Context, val posts: MutableList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
    fun clear() {
        posts.clear()
        notifyDataSetChanged()
    }


// Add a list of items -- change to type used

    // Add a list of items -- change to type used
    fun addAll(list: List<Post>) {
        posts.addAll(list)
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView
        val ivImage: ImageView
        val tvDesc: TextView
        val tvCreate: TextView

        init {
            tvUsername = itemView.findViewById(R.id.tvUserName)
            ivImage = itemView.findViewById(R.id.ivImage)
            tvDesc = itemView.findViewById(R.id.tvDesc)
            tvCreate = itemView.findViewById(R.id.tvCreated)
        }

        fun bind(post: Post) {
            tvDesc.text = post.getDescription()
            tvUsername.text = post.getUser()?.fetchIfNeeded()?.username
            tvCreate.text = TimeFormatter.getTimeDifference(post.getCreated().toString())

            Glide.with(itemView.context).load(post.getImage()?.url).into(ivImage)

        }

    }

}