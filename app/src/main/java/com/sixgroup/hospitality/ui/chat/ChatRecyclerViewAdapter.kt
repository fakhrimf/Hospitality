package com.sixgroup.hospitality.ui.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sixgroup.hospitality.databinding.ItemChatBinding
import com.sixgroup.hospitality.model.ChatModel
import com.sixgroup.hospitality.ui.home.PostRecyclerViewAdapter
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentDokter
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentUser
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatRecyclerViewAdapter(
    private val values: ArrayList<ChatModel>,
    private val context: Context?
) : RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder>() {


    inner class ViewHolder(binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        val sent = binding.sent
        val sentMessage = binding.sentMessage
        val timestampSent = binding.timestampSent
        val reply = binding.reply
        val replyMessage = binding.replyMessage
        val timestampReply = binding.timeStampReply

        override fun toString(): String {
            return ""
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatRecyclerViewAdapter.ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        if (context != null) {
            val pasien = getCurrentUser(context)
            val dokter = getCurrentDokter(context)

            val sdfget = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)
            val sdf = SimpleDateFormat("HH:mm", Locale.US)
            if (pasien != null) {
                if (item.sender == pasien.idUser) {
                    holder.replyMessage.text = item.message!!.decryptCBC()
                    holder.timestampReply.text = sdf.format(sdfget.parse(item.timestamp!!.decryptCBC())!!)
                    holder.sent.visibility = View.GONE
                } else {
                    holder.sentMessage.text = item.message!!.decryptCBC()
                    holder.timestampSent.text = sdf.format(sdfget.parse(item.timestamp!!.decryptCBC())!!)
                    holder.reply.visibility = View.GONE
                }
            } else if (dokter != null) {
                if (item.sender == dokter.idUser) {
                    holder.replyMessage.text = item.message!!.decryptCBC()
                    holder.timestampReply.text = sdf.format(sdfget.parse(item.timestamp!!.decryptCBC())!!)
                    holder.sent.visibility = View.GONE
                } else {
                    holder.sentMessage.text = item.message!!.decryptCBC()
                    holder.timestampSent.text = sdf.format(sdfget.parse(item.timestamp!!.decryptCBC())!!)
                    holder.reply.visibility = View.GONE
                }
            }
        }
    }
}