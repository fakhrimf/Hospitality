package com.sixgroup.hospitality.model

data class AdminModel(
    private var nama: String,
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

    fun editProfil(
        nama: String = this.nama,
        email: String = this.email,
        foto: String = this.foto,
        password: String = this.password,
    ) {
        this.nama = nama
        this.email = email
        this.foto = foto
        this.password = password
    }
}