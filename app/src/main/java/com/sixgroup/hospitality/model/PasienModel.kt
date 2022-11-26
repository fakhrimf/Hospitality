package com.sixgroup.hospitality.model

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.utils.DB_CHILD_PASIEN
import com.sixgroup.hospitality.utils.DB_SET_VALUE_SUCCESS
import com.sixgroup.hospitality.utils.SECRET_IV
import com.sixgroup.hospitality.utils.SECRET_KEY
import com.sixgroup.hospitality.utils.repository.DatabaseMessageModel
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getChild
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getPasienData
import com.sixgroup.hospitality.utils.repository.Repository.Companion.reference
import com.sixgroup.hospitality.utils.repository.Repository.Companion.storePasien
import java.util.Date
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

data class PasienModel(
    var nama: String? = "",
    var noHP: String? = "",
    var tglLahir: String? = "",
    override var idUser: String? = "",
    override var password: String? = "",
    override var email: String? = "",
    override var foto: String? = "",
) : UserModel() {

    override fun login(context: Context, lifecycleOwner: LifecycleOwner) : MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        var check = false
        getPasienData().observe(lifecycleOwner, Observer<ArrayList<PasienModel>> {
            for (i in it) {
                if (email!! == i.email!!.decryptCBC() && password!! == i.password!!.decryptCBC()) {
                    check = true
                    storePasien(context, i)
                }
            }
            liveData.value = check
        })
        return liveData
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    fun registerProfile(context: Context): MutableLiveData<DatabaseMessageModel> {
        val liveData = MutableLiveData<DatabaseMessageModel>()
        idUser = "${reference.push().key}"
        getChild(DB_CHILD_PASIEN).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                liveData.value = DatabaseMessageModel(false, p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                var dupe = false
                for (i in p0.children) {
                    val model = i.getValue(PasienModel::class.java)
                    if (model?.email == email) {
                        dupe = true
                        liveData.value = DatabaseMessageModel(
                            false, context.getString(R.string.email_registered)
                        )
                        break
                    }
                }

                if (!dupe) {
                    getChild(DB_CHILD_PASIEN).child(idUser!!).setValue(this@PasienModel) { error, _ ->
                        if (error != null) {
                            liveData.value = DatabaseMessageModel(false, error.message)
                        } else {
                            liveData.value = DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
                            storePasien(context, this@PasienModel)
                        }
                    }
                }
            }
        })
        return liveData
    }

    private fun String.decryptCBC(): String {
        val decodedByte: ByteArray = Base64.decode(this, Base64.DEFAULT)
        val iv = IvParameterSpec(SECRET_IV.toByteArray())
        val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)
        val output = cipher.doFinal(decodedByte)
        return String(output)
    }
}