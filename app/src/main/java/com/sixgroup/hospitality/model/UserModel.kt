package com.sixgroup.hospitality.model

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.sixgroup.hospitality.utils.APP_SHARED_PREFERENCE
import com.sixgroup.hospitality.utils.PASIEN_SHARED_PREFERENCE

abstract class UserModel {
    abstract var idUser: String?
    abstract var email: String?
    abstract var foto: String?
    abstract var password: String?

    abstract fun login(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        remember: Boolean
    ): MutableLiveData<Boolean>

    fun logout(
        context: Context,
    ) {
        val sharedPref = context.getSharedPreferences(
            APP_SHARED_PREFERENCE,
            Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }
}
