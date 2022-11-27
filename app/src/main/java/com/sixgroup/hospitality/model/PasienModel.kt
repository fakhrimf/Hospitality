package com.sixgroup.hospitality.model

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.utils.*
import com.sixgroup.hospitality.utils.repository.DatabaseMessageModel
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getChild
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getPasienData
import com.sixgroup.hospitality.utils.repository.Repository.Companion.reference
import com.sixgroup.hospitality.utils.repository.Repository.Companion.storageReference
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

    override fun login(context: Context, lifecycleOwner: LifecycleOwner): MutableLiveData<Boolean> {
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

    fun registerProfile(context: Context, path: Uri?): MutableLiveData<DatabaseMessageModel> {
        val liveData = MutableLiveData<DatabaseMessageModel>()
        idUser = "${reference.push().key}"
        val ref = storageReference.child(STORAGE_IMAGES).child(idUser!!)
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
                    if (path != null) {
                        ref.putFile(path).apply {
                            addOnSuccessListener {
                                ref.downloadUrl.apply {
                                    addOnSuccessListener {
                                        foto = it.toString()
                                        liveData.value =
                                            DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
                                        storePasien(context, this@PasienModel)
                                        getChild(DB_CHILD_PASIEN).child(idUser!!)
                                            .setValue(this@PasienModel) { error, _ ->
                                                if (error != null) {
                                                    liveData.value =
                                                        DatabaseMessageModel(false, error.message)
                                                }
                                            }
                                    }
                                    addOnFailureListener {
                                        liveData.value =
                                            DatabaseMessageModel(false, "${it.message}")
                                    }
                                }
                            }
                            addOnFailureListener {
                                liveData.value = DatabaseMessageModel(false, "${it.message}")
                            }
                        }
                    } else {
                        getChild(DB_CHILD_PASIEN).child(idUser!!)
                            .setValue(this@PasienModel) { error, _ ->
                                if (error != null) {
                                    liveData.value = DatabaseMessageModel(false, error.message)
                                } else {
                                    liveData.value =
                                        DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
                                    storePasien(context, this@PasienModel)
                                }
                            }
                    }
                }
            }
        })
        return liveData
    }

    fun editProfile(
        nama: String? = this.nama,
        noHP: String? = this.noHP,
        tglLahir: String? = this.tglLahir,
        password: String? = this.password,
        email: String? = this.email,
        foto: String? = this.foto,
        path: Uri? = null,
        context: Context,
    ): MutableLiveData<DatabaseMessageModel> {
        this.nama = nama
        this.noHP = noHP
        this.tglLahir = tglLahir
        this.password = password
        this.email = email
        this.foto = foto
        val ref = storageReference.child(STORAGE_IMAGES).child(idUser!!)
        val liveData = MutableLiveData<DatabaseMessageModel>()
        getChild(DB_CHILD_PASIEN).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                liveData.value = DatabaseMessageModel(false, p0.message)
            }
            override fun onDataChange(p0: DataSnapshot) {
                if (path != null) {
                    ref.putFile(path).apply {
                        addOnSuccessListener {
                            ref.downloadUrl.apply {
                                addOnSuccessListener {
                                    this@PasienModel.foto = it.toString()
                                    liveData.value =
                                        DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
                                    storePasien(context, this@PasienModel)
                                    getChild(DB_CHILD_PASIEN).child(idUser!!)
                                        .setValue(this@PasienModel) { error, _ ->
                                            if (error != null) {
                                                liveData.value =
                                                    DatabaseMessageModel(false, error.message)
                                            }
                                        }
                                }
                                addOnFailureListener {
                                    liveData.value = DatabaseMessageModel(false, "${it.message}")
                                }
                            }
                        }
                        addOnFailureListener {
                            liveData.value = DatabaseMessageModel(false, "${it.message}")
                        }
                    }
                } else {
                    getChild(DB_CHILD_PASIEN).child(idUser!!)
                        .setValue(this@PasienModel) { error, _ ->
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

}