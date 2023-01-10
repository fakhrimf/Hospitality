package com.sixgroup.hospitality.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sixgroup.hospitality.ui.home.placeholder.PlaceholderContent.PlaceholderItem
import com.sixgroup.hospitality.databinding.FragmentPostBinding
import com.sixgroup.hospitality.ui.home.news.Article
import com.squareup.picasso.Picasso

class PostRecyclerViewAdapter(
    private val values: ArrayList<Article>,
    private val context: Context?
) : RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        if (item.source.name == "Google News") holder.cvPost.visibility = View.GONE
        holder.poster.text = item.author
        if (item.author == null) holder.poster.text = "Unknown Author"
        holder.posterlocation.text = item.source.name
        if (item.source.name == null) holder.posterlocation.text = "Unknown Source"
        holder.posttitle.text = item.title
        holder.postdetail.text = item.description
        Picasso.get().load(item.urlToImage).into(holder.postImage)
        holder.cvPost.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(item.url)
            context?.startActivity(intent)
        }
    }

    inner class ViewHolder(binding: FragmentPostBinding) : RecyclerView.ViewHolder(binding.root) {
        val poster: TextView = binding.poster
        val posterlocation: TextView = binding.posterlocation
        val posttitle: TextView = binding.posttitle
        val postdetail: TextView = binding.postdetail
        val postImage: ImageView = binding.postImage
        val cvPost = binding.cvPost

        override fun toString(): String {
            return super.toString() + " '" + posterlocation.text + "'"
        }
    }

    override fun getItemCount(): Int = values.size
}