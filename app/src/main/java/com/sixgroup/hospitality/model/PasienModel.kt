package com.sixgroup.hospitality.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import java.util.Date

data class PasienModel(
    private val nama: String,
    private val noHP: String,
    private val tglLahir: Date,
    override val idUser: String,
    override var password: String,
    override var email: String,
    override var foto: String
) : UserModel() {
    override fun login_() {
        TODO("Not yet implemented")
    }

    override fun logout_() {
        TODO("Not yet implemented")
    }

//    fun registerProfile(context: Context): MutableLiveData<> {
//
//    }
}