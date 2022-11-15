package com.sixgroup.hospitality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sixgroup.hospitality.ui.chat.ChatFragment

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ChatFragment.newInstance())
                .commitNow()
        }
    }
}