package com.sixgroup.hospitality.utils.repository

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.sixgroup.hospitality.model.AdminModel
import com.sixgroup.hospitality.model.DokterModel
import com.sixgroup.hospitality.model.PasienModel
import com.sixgroup.hospitality.utils.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Repository {
    companion object {
        fun newInstance() = Repository()
        private val database = FirebaseDatabase.getInstance()
        private val storage = FirebaseStorage.getInstance()
        val reference = database.reference
        val storageReference = storage.reference
        fun getChild(child: String) = reference.child(child)
        fun storePasien(context: Context, pasienModel: PasienModel) {
            val sharedPref = context.getSharedPreferences(APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE
            )
            val editor = sharedPref.edit()
            val gson = Gson()
            val json = gson.toJson(pasienModel) as String
            editor.putString(PASIEN_SHARED_PREFERENCE, json)
            editor.apply()
        }
        fun removePasienSP(context: Context) {
            val sharedPref = context.getSharedPreferences(APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE
            )
            val editor = sharedPref.edit()
            editor.remove(PASIEN_SHARED_PREFERENCE)
            editor.apply()
        }
        fun removeDokterSP(context: Context) {
            val sharedPref = context.getSharedPreferences(APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE
            )
            val editor = sharedPref.edit()
            editor.remove(DOKTER_SHARED_PREFERENCE)
            editor.apply()
        }
        fun rememberMePasien(context: Context, pasienModel: PasienModel) {
            val sharedPref = context.getSharedPreferences(APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE
            )
            val editor = sharedPref.edit()
            val gson = Gson()
            val json = gson.toJson(pasienModel) as String
            editor.putString(PASIEN_RM_SHARED_PREFERENCE, json)
            editor.apply()
        }
        fun getRememberMe(context: Context): PasienModel? {
            val gson = Gson()
            val profileJson = context.getSharedPreferences(APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE).getString(PASIEN_RM_SHARED_PREFERENCE, null)
            return if (profileJson != null) gson.fromJson(profileJson, PasienModel::class.java)
            else null
        }
        fun storeDokter(context: Context, dokterModel: DokterModel) {
            val sharedPref = context.getSharedPreferences(APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE
            )
            val editor = sharedPref.edit()
            val gson = Gson()
            val json = gson.toJson(dokterModel) as String
            editor.putString(DOKTER_SHARED_PREFERENCE, json)
            editor.apply()
            Log.d("PROFILE", json)
            getCurrentUser(context)
        }
        fun storeOnboard(context: Context, boolean: Boolean) {
            val sharedPref = context.getSharedPreferences(APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE
            )
            val editor = sharedPref.edit()
            editor.putBoolean(ONBOARD_SHARED_PREFERENCE, boolean)
            editor.apply()
            getCurrentUser(context)
        }
        fun storeAdmin(context: Context, adminModel: AdminModel) {
            val sharedPref = context.getSharedPreferences(APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE
            )
            val editor = sharedPref.edit()
            val gson = Gson()
            val json = gson.toJson(adminModel) as String
            editor.putString(ADMIN_SHARED_PREFERENCE, json)
            editor.apply()
            Log.d("PROFILE", json)
            getCurrentUser(context)
        }
        fun getPasienData(): MutableLiveData<ArrayList<PasienModel>> {
            val liveData = MutableLiveData<ArrayList<PasienModel>>()
            val list = ArrayList<PasienModel>()

            getChild(DB_CHILD_PASIEN).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    throw Throwable(p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    list.clear()
                    for (data in p0.children) {
                        val model = data.getValue(PasienModel::class.java)
                        model?.let {
                            list.add(model)
                        }
                    }
                    liveData.value = list
                }
            })
            return liveData
        }
        fun getDokterData(): MutableLiveData<ArrayList<DokterModel>> {
            val liveData = MutableLiveData<ArrayList<DokterModel>>()
            val list = ArrayList<DokterModel>()

            getChild(DB_CHILD_DOKTER).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    throw Throwable(p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    list.clear()
                    for (data in p0.children) {
                        val model = data.getValue(DokterModel::class.java)
                        model?.let {
                            list.add(model)
                        }
                    }
                    liveData.value = list
                }
            })
            return liveData
        }
        fun getAdminData(): MutableLiveData<ArrayList<AdminModel>> {
            val liveData = MutableLiveData<ArrayList<AdminModel>>()
            val list = ArrayList<AdminModel>()

            getChild(DB_CHILD_ADMIN).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    throw Throwable(p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    list.clear()
                    for (data in p0.children) {
                        val model = data.getValue(AdminModel::class.java)
                        model?.let {
                            list.add(model)
                        }
                    }
                    liveData.value = list
                }
            })
            return liveData
        }
        fun getCurrentUser(context: Context): PasienModel? {
            val gson = Gson()
            val profileJson = context.getSharedPreferences(APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE).getString(PASIEN_SHARED_PREFERENCE, null)
            return if (profileJson != null) gson.fromJson(profileJson, PasienModel::class.java)
            else null
        }
        fun getCurrentDokter(context: Context): DokterModel? {
            val gson = Gson()
            val profileJson = context.getSharedPreferences(APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE).getString(DOKTER_SHARED_PREFERENCE, null)
            return if (profileJson != null) gson.fromJson(profileJson, DokterModel::class.java)
            else null
        }
        fun getOnboard(context: Context): Boolean {
            return context.getSharedPreferences(
                APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE
            ).getBoolean(ONBOARD_SHARED_PREFERENCE, false)
        }
        fun getImageIntent(): Intent {
            val intent = Intent()
            intent.apply {
                type = IMAGE_TYPE
                action = Intent.ACTION_GET_CONTENT
            }
            return Intent.createChooser(intent, "Pilih gambar yang ingin digunakan")
        }
        fun getImageBitmap(contentResolver: ContentResolver, path: Uri): Bitmap {
            @Suppress("DEPRECATION")
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, path))
        }
        fun String.encryptCBC(): String {
            val iv = IvParameterSpec(SECRET_IV.toByteArray())
            val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
            val crypted = cipher.doFinal(this.toByteArray())
            val encodedByte = Base64.encode(crypted, Base64.DEFAULT)
            return String(encodedByte)
        }

        fun String.decryptCBC(): String {
            val decodedByte: ByteArray = Base64.decode(this, Base64.DEFAULT)
            val iv = IvParameterSpec(SECRET_IV.toByteArray())
            val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)
            val output = cipher.doFinal(decodedByte)
            return String(output)
        }
    }
}