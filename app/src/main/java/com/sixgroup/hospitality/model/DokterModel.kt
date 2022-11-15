package com.sixgroup.hospitality.model

data class DokterModel(
    private var nama: String,
    private var spesialis: String,
    private var yoe: Int,
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

    fun acceptAppointment() {
        TODO("Fungsi accept appointment disini")
    }

    fun viewAppointment() {
        TODO("Fungsi view appointment")
    }

    fun editProfil(
        nama:String = this.nama,
        spesialis: String = this.spesialis,
        yoe: Int = this.yoe,
        email: String = this.email,
        foto: String = this.foto,
        password: String = this.password,
    ) {
        this.nama = nama
        this.spesialis = spesialis
        this.yoe = yoe
        this.email = email
        this.foto = foto
        this.password = password
    }

}
