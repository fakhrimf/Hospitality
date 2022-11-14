package com.sixgroup.hospitality.model

data class DokterModel(
    private val nama: String,
    private val spesialis: String,
    private val yoe: Int
) : UserModel()
