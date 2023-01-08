package com.sixgroup.hospitality.ui.chat

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sixgroup.hospitality.databinding.ItemChatBinding
import com.sixgroup.hospitality.model.ChatModel
import com.sixgroup.hospitality.ui.home.PostRecyclerViewAdapter

class ChatRecyclerViewAdapter(
    private val values: ArrayList<ChatModel>,
    private val context: Context?
) : RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder>() {


    inner class ViewHolder(binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
//        val poster: TextView = binding.poster
//        val posterlocation: TextView = binding.posterlocation
//        val posttitle: TextView = binding.posttitle
//        val postdetail: TextView = binding.postdetail
//        val postImage: ImageView = binding.postImage
//        val cvPost = binding.cvPost

        override fun toString(): String {
            return ""
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostRecyclerViewAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PostRecyclerViewAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}