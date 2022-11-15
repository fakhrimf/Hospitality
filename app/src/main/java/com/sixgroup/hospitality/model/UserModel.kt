package com.sixgroup.hospitality.model

abstract class UserModel() {
    abstract val idUser: String
    abstract var email: String
    abstract var foto: String
    abstract var password: String

    abstract fun login_()
    abstract fun logout_()
}
