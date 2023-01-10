package com.sixgroup.hospitality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.sixgroup.hospitality.model.AppointmentModel
import com.sixgroup.hospitality.model.DokterModel
import com.sixgroup.hospitality.model.PasienModel
import com.sixgroup.hospitality.ui.chat.ChatFragment
import com.sixgroup.hospitality.utils.APPOINTMENT_APP_DETAIL
import com.sixgroup.hospitality.utils.APPOINTMENT_DOK_DETAIL
import com.sixgroup.hospitality.utils.APPOINTMENT_PAS_DETAIL

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

    fun getAppt() : AppointmentModel? {
        val appt = intent.getStringExtra(APPOINTMENT_APP_DETAIL)
        if (appt != null) return Gson().fromJson(appt, AppointmentModel::class.java)
        return null
    }

    fun getDokter() : DokterModel? {
        val dokter = intent.getStringExtra(APPOINTMENT_DOK_DETAIL)
        if (dokter != null) return Gson().fromJson(dokter, DokterModel::class.java)
        return null
    }

    fun getPasien() : PasienModel? {
        val pasien = intent.getStringExtra(APPOINTMENT_PAS_DETAIL)
        if (pasien != null) return Gson().fromJson(pasien, PasienModel::class.java)
        return null
    }
}