package com.sixgroup.hospitality.utils.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.sixgroup.hospitality.model.PasienModel
import com.sixgroup.hospitality.utils.APP_SHARED_PREFERENCE
import com.sixgroup.hospitality.utils.DB_CHILD_PASIEN
import com.sixgroup.hospitality.utils.USER_SHARED_PREFERENCE

class Repository {
    companion object {
        fun newInstance() = Repository()
        private val database = FirebaseDatabase.getInstance()
        private val storage = FirebaseStorage.getInstance()
        val reference = database.reference
        val storageReference = storage.reference
        fun getChild(child: String) = reference.child(child)
        fun getSharedPreferences(context: Context): SharedPreferences? =
            context.getSharedPreferences(APP_SHARED_PREFERENCE,
            Context.MODE_PRIVATE
        )
        fun storePasien(context: Context, pasienModel: PasienModel) {
            val sharedPref = context.getSharedPreferences(APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE
            )
            val editor = sharedPref.edit()
            val gson = Gson()
            val json = gson.toJson(pasienModel) as String
            editor.putString(USER_SHARED_PREFERENCE, json)
            editor.apply()
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
    }

}