package com.sixgroup.hospitality.model

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

data class AdminModel(
    private var nama: String? = "",
    override var idUser: String? = "",
    override var email: String? = "",
    override var foto: String? = "",
    override var password: String? = ""
) : UserModel() {
    override fun login(context: Context, lifecycleOwner: LifecycleOwner) : MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        return liveData
        TODO("Not yet Implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    fun editProfil(
        nama: String = this.nama!!,
        email: String = this.email!!,
        foto: String = this.foto!!,
        password: String = this.password!!,
    ) {
        this.nama = nama
        this.email = email
        this.foto = foto
        this.password = password
    }
}