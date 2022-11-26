package com.sixgroup.hospitality.model

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

abstract class UserModel() {
    abstract var idUser: String?
    abstract var email: String?
    abstract var foto: String?
    abstract var password: String?

    abstract fun login(context: Context, lifecycleOwner: LifecycleOwner) : MutableLiveData<Boolean>
    abstract fun logout()
}
