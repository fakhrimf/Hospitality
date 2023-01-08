package com.sixgroup.hospitality.ui.chat

import android.icu.util.Calendar
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.sixgroup.hospitality.AppointmentDetailActivity
import com.sixgroup.hospitality.ChatActivity
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.model.ChatModel
import com.sixgroup.hospitality.utils.repository.Repository.Companion.addChat
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.encryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getChat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat.*
import java.text.SimpleDateFormat
import java.util.*

class ChatFragment : Fragment() {

    companion object {
        fun newInstance() = ChatFragment()
    }

    private lateinit var viewModel: ChatViewModel
    private val myCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val act = requireActivity() as ChatActivity
        val dokter = act.getDokter()
        val pasien = act.getPasien()
        val apt = act.getAppt()
        var itemcount = 0
        if (dokter != null && apt != null) {
            titleNama.text = dokter.nama!!.decryptCBC()
            inputChat.hint = "Chat dengan ${dokter.nama!!.decryptCBC()}"
            Picasso.get().load(dokter.foto).into(imgUser)
            getChat(apt).observeForever {
                val layoutmgr = LinearLayoutManager(context)
                layoutmgr.stackFromEnd = true
                val adapter = ChatRecyclerViewAdapter(it, context)
                if (list != null) {
                    list.layoutManager = layoutmgr
                    list.adapter = adapter
                    if (itemcount != 0)
                        list.smoothScrollToPosition(adapter.itemCount-1)
                }
            }
            cvSend.setOnClickListener {
                if (inputChat.text.isNotEmpty())
                    addChat(apt, getChatModel(dokter.idUser!!))
                inputChat.text.clear()
            }
        } else if (pasien != null && apt != null) {
            titleNama.text = pasien.nama!!.decryptCBC()
            inputChat.hint = "Chat dengan ${pasien.nama!!.decryptCBC()}"
            Picasso.get().load(pasien.foto).into(imgUser)
            getChat(apt).observeForever {
                val layoutmgr = LinearLayoutManager(context)
                layoutmgr.stackFromEnd = true
                val adapter = ChatRecyclerViewAdapter(it, context)
                if (list != null) {
                    list.layoutManager = layoutmgr
                    list.adapter = adapter
                    if (itemcount != 0)
                        list.smoothScrollToPosition(adapter.itemCount-1)
                }
            }
            cvSend.setOnClickListener {
                if (inputChat.text.isNotEmpty())
                    addChat(apt, getChatModel(pasien.idUser!!))
                inputChat.text.clear()
            }
        }
    }

    private fun getChatModel(sender: String): ChatModel {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)
        return ChatModel(
            sender = sender,
            message = inputChat.text.toString().encryptCBC(),
            timestamp = sdf.format(myCalendar.time).encryptCBC(),
        )
    }
}