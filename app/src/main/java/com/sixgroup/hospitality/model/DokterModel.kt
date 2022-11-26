package com.sixgroup.hospitality.model

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

data class DokterModel(
    private var nama: String? = "",
    private var spesialis: String? = "",
    private var yoe: Int? = 0,
    override var idUser: String? = "",
    override var email: String? = "",
    override var foto: String? = "",
    override var password: String? = "",
) : UserModel() {

    override fun login(context: Context, lifecycleOwner: LifecycleOwner) : MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        return liveData
        TODO("Not yet Implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    fun acceptAppointment() {
        TODO("Fungsi accept appointment disini")
    }

    fun viewAppointment() {
        TODO("Fungsi view appointment")
    }

    fun editProfil(
        nama:String = this.nama!!,
        spesialis: String = this.spesialis!!,
        yoe: Int = this.yoe!!,
        email: String = this.email!!,
        foto: String = this.foto!!,
        password: String = this.password!!,
    ) {
        this.nama = nama
        this.spesialis = spesialis
        this.yoe = yoe
        this.email = email
        this.foto = foto
        this.password = password
    }

}
