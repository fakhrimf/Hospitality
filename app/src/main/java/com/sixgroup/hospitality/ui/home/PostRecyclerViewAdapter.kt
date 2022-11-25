package com.sixgroup.hospitality.ui.home

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sixgroup.hospitality.ui.home.placeholder.PlaceholderContent.PlaceholderItem
import com.sixgroup.hospitality.databinding.FragmentPostBinding
import com.squareup.picasso.Picasso

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class PostRecyclerViewAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder>() {

    private val linkPlaceholder = "https://cdn.discordapp.com/attachments/838616441951223868/904577420101812274/93615372_p0.png"
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
        holder.poster.text = item.id
        holder.posterlocation.text = item.content
        holder.posttitle.text = item.content
        holder.postdetail.text = item.details
        Picasso.get().load(linkPlaceholder).into(holder.postImage)
        Picasso.get().load(linkPlaceholder).into(holder.userImage)
    }

    inner class ViewHolder(binding: FragmentPostBinding) : RecyclerView.ViewHolder(binding.root) {
        val poster: TextView = binding.poster
        val posterlocation: TextView = binding.posterlocation
        val posttitle: TextView = binding.posttitle
        val postdetail: TextView = binding.postdetail
        val postImage: ImageView = binding.postImage
        val userImage: ImageView = binding.userImage

        override fun toString(): String {
            return super.toString() + " '" + posterlocation.text + "'"
        }
    }

    override fun getItemCount(): Int = values.size
}