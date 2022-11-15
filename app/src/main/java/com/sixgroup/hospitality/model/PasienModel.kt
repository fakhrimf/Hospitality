package com.sixgroup.hospitality.model

import java.util.Date

data class PasienModel(
    private val nama: String,
    private val noHP: String,
    private val tglLahir: Date,
    override val idUser: String,
    override var email: String,
    override var foto: String,
    override var password: String
) : UserModel() {
    override fun login_() {
        TODO("Not yet implemented")
    }

    override fun logout_() {
        TODO("Not yet implemented")
    }
}