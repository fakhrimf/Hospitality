package com.sixgroup.hospitality.model

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sixgroup.hospitality.utils.repository.Repository
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getAdminData
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getDokterData

data class AdminModel(
    private var nama: String? = "",
    override var idUser: String? = "",
    override var email: String? = "",
    override var foto: String? = "",
    override var password: String? = ""
) : UserModel() {
    override fun login(context: Context, lifecycleOwner: LifecycleOwner): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        var check = false
        getAdminData().observe(lifecycleOwner, Observer<ArrayList<AdminModel>> {
            for (i in it) {
                if (email!! == i.email!!.decryptCBC() && password!! == i.password!!.decryptCBC()) {
                    check = true
                    Repository.storeAdmin(context, i)
                }
            }
            liveData.value = check
        })
        return liveData
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