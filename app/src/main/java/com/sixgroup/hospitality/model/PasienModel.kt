package com.sixgroup.hospitality.model

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.utils.DB_CHILD_PASIEN
import com.sixgroup.hospitality.utils.DB_SET_VALUE_SUCCESS
import com.sixgroup.hospitality.utils.STORAGE_IMAGES
import com.sixgroup.hospitality.utils.repository.DatabaseMessageModel
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getChild
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getPasienData
import com.sixgroup.hospitality.utils.repository.Repository.Companion.reference
import com.sixgroup.hospitality.utils.repository.Repository.Companion.rememberMePasien
import com.sixgroup.hospitality.utils.repository.Repository.Companion.storageReference
import com.sixgroup.hospitality.utils.repository.Repository.Companion.storePasien

data class PasienModel(
    var nama: String? = "",
    var noHP: String? = "",
    var tglLahir: String? = "",
    override var idUser: String? = "",
    override var password: String? = "",
    override var email: String? = "",
    override var foto: String? = "",
) : UserModel() {

    // Fungsi untuk login dengan akun pasien, yang mengembalikan livedata berupa boolean
    // livedata kemudian akan di observe untuk mengecek apabila kombinasi email dan password benar
    // jika benar, maka view akan berlanjut ke activity home
    override fun login(
        context: Context, lifecycleOwner: LifecycleOwner, remember: Boolean
    ): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        var check = false
        getPasienData().observe(lifecycleOwner, Observer<ArrayList<PasienModel>> {
            for (i in it) {
                if (email!! == i.email!!.decryptCBC() && password!! == i.password!!.decryptCBC()) {
                    check = true
                    storePasien(context, i)
                    if (remember) rememberMePasien(context, i)
                }
            }
            liveData.value = check
        })
        return liveData
    }

    // Fungsi register profil pasien, yang akan mengembalikan live data berupa pesan dari database
    // dimana data tersebut akan dicek apakah success atau tidak, jika success, maka akan login
    // dan akan berlanjut ke activity home, jika tidak akan menampilkan error dari database
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

    // Fungsi edit profil pasien, yang akan mengembalikan live data berupa pesan dari database
    // dimana data tersebut akan dicek apakah success atau tidak, jika success, maka profil pasien
    // akan diubah sesuai data yang diubah, fungsi ini juga bisa digunakan sebagai fungsi ganti password
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
        this.foto = foto
        val ref = storageReference.child(STORAGE_IMAGES).child(idUser!!)
        val liveData = MutableLiveData<DatabaseMessageModel>()
        getChild(DB_CHILD_PASIEN).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                liveData.value = DatabaseMessageModel(false, p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                var dupe = false
                for (i in p0.children) {
                    val model = i.getValue(DokterModel::class.java)
                    if (email != this@PasienModel.email) if (model?.email == email) {
                        dupe = true
                        liveData.value = DatabaseMessageModel(
                            false, context.getString(R.string.email_registered)
                        )
                        break
                    }
                }
                if (!dupe) if (path != null) {
                    this@PasienModel.email = email
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
                    this@PasienModel.email = email
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