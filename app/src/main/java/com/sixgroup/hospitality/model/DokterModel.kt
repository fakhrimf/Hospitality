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
import com.sixgroup.hospitality.utils.DB_CHILD_DOKTER
import com.sixgroup.hospitality.utils.DB_SET_VALUE_SUCCESS
import com.sixgroup.hospitality.utils.STORAGE_IMAGES
import com.sixgroup.hospitality.utils.repository.DatabaseMessageModel
import com.sixgroup.hospitality.utils.repository.Repository
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getChild
import com.sixgroup.hospitality.utils.repository.Repository.Companion.storeDokter

data class DokterModel(
    var nama: String? = "",
    var spesialis: String? = "",
    var yoe: Int? = 0,
    override var idUser: String? = "",
    override var email: String? = "",
    override var foto: String? = "",
    override var password: String? = "",
) : UserModel() {

    // Fungsi untuk login dengan akun dokter, yang mengembalikan livedata berupa boolean
    // livedata kemudian akan di observe untuk mengecek apabila kombinasi email dan password benar
    // jika benar, maka view akan berlanjut ke activity home
    override fun login(
        context: Context, lifecycleOwner: LifecycleOwner, remember: Boolean
    ): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        var check = false
        Repository.getDokterData().observe(lifecycleOwner, Observer<ArrayList<DokterModel>> {
            for (i in it) {
                if (email!! == i.email!!.decryptCBC() && password!! == i.password!!.decryptCBC()) {
                    check = true
                    storeDokter(context, i)
                    if (remember) Repository.rememberMeDokter(context, i)
                }
            }
            liveData.value = check
        })
        return liveData
    }

    // Fungsi register profil dokter, yang akan mengembalikan live data berupa pesan dari database
    // dimana data tersebut akan dicek apakah success atau tidak, jika success, maka akan login
    // dan akan berlanjut ke activity home, jika tidak akan menampilkan error dari database
    fun registerProfile(context: Context, path: Uri?): MutableLiveData<DatabaseMessageModel> {
        val liveData = MutableLiveData<DatabaseMessageModel>()
        idUser = "${Repository.reference.push().key}"
        val ref = Repository.storageReference.child(STORAGE_IMAGES).child(idUser!!)
        getChild(DB_CHILD_DOKTER).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                liveData.value = DatabaseMessageModel(false, p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                var dupe = false
                for (i in p0.children) {
                    val model = i.getValue(DokterModel::class.java)
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
                                        storeDokter(context, this@DokterModel)
                                        getChild(DB_CHILD_DOKTER).child(idUser!!)
                                            .setValue(this@DokterModel) { error, _ ->
                                                if (error != null) {
                                                    liveData.value = DatabaseMessageModel(
                                                        false, error.message
                                                    )
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
                        getChild(DB_CHILD_DOKTER).child(idUser!!)
                            .setValue(this@DokterModel) { error, _ ->
                                if (error != null) {
                                    liveData.value = DatabaseMessageModel(false, error.message)
                                } else {
                                    liveData.value =
                                        DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
                                    storeDokter(context, this@DokterModel)
                                }
                            }
                    }
                }
            }
        })
        return liveData
    }

    // Fungsi edit profil dokter, yang akan mengembalikan live data berupa pesan dari database
    // dimana data tersebut akan dicek apakah success atau tidak, jika success, maka profil pasien
    // akan diubah sesuai data yang diubah, fungsi ini juga bisa digunakan sebagai fungsi ganti password
    fun editProfil(
        nama: String? = this.nama,
        spesialis: String? = this.spesialis,
        yoe: Int? = this.yoe,
        email: String? = this.email,
        foto: String? = this.foto,
        password: String? = this.password,
        path: Uri? = null,
        context: Context,
    ): MutableLiveData<DatabaseMessageModel> {
        this.nama = nama
        this.spesialis = spesialis
        this.yoe = yoe
        this.foto = foto
        this.password = password
        val liveData = MutableLiveData<DatabaseMessageModel>()
        idUser = "${Repository.reference.push().key}"
        val ref = Repository.storageReference.child(STORAGE_IMAGES).child(idUser!!)
        getChild(DB_CHILD_DOKTER).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                liveData.value = DatabaseMessageModel(false, p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                var dupe = false
                for (i in p0.children) {
                    val model = i.getValue(DokterModel::class.java)
                    if (email != this@DokterModel.email)
                    if (model?.email == email) {
                        dupe = true
                        liveData.value = DatabaseMessageModel(
                            false, context.getString(R.string.email_registered)
                        )
                        break
                    }
                }
                if (!dupe)
                if (path != null) {
                    this@DokterModel.email = email
                    ref.putFile(path).apply {
                        addOnSuccessListener {
                            ref.downloadUrl.apply {
                                addOnSuccessListener {
                                    this@DokterModel.foto = it.toString()
                                    liveData.value =
                                        DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
                                    storeDokter(context, this@DokterModel)
                                    getChild(DB_CHILD_DOKTER).child(idUser!!)
                                        .setValue(this@DokterModel) { error, _ ->
                                            if (error != null) {
                                                liveData.value = DatabaseMessageModel(
                                                    false, error.message
                                                )
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
                    this@DokterModel.email = email
                    getChild(DB_CHILD_DOKTER).child(idUser!!)
                        .setValue(this@DokterModel) { error, _ ->
                            if (error != null) {
                                liveData.value = DatabaseMessageModel(false, error.message)
                            } else {
                                liveData.value = DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
                                storeDokter(context, this@DokterModel)
                            }
                        }
                }
            }
        })
        return liveData
    }

}
