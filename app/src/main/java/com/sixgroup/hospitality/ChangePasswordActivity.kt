package com.sixgroup.hospitality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sixgroup.hospitality.ui.account.ChangePasswordFragment

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ChangePasswordFragment.newInstance())
                .commitNow()
        }
    }
}