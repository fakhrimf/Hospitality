package com.sixgroup.hospitality.ui.home

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.TextView
import com.sixgroup.hospitality.ui.home.placeholder.PlaceholderContent.PlaceholderItem
import com.sixgroup.hospitality.databinding.FragmentPostBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class PostRecyclerViewAdapter(
    private val values: List<PlaceholderItem>
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
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    inner class ViewHolder(binding: FragmentPostBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.poster
        val contentView: TextView = binding.posterlocation

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    override fun getItemCount(): Int = values.size

}