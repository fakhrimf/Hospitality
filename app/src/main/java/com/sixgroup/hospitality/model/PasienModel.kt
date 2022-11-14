package com.sixgroup.hospitality.model

import java.util.Date

data class PasienModel (
    private val nama: String,
    private val noHP: String,
    private val tglLahir: Date
) : UserModel()