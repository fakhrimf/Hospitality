package com.sixgroup.hospitality.ui.appointment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sixgroup.hospitality.AppointmentDetailActivity
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.databinding.ItemMyAppointmentBinding
import com.sixgroup.hospitality.model.AppointmentModel
import com.sixgroup.hospitality.model.DokterModel
import com.sixgroup.hospitality.model.PasienModel
import com.sixgroup.hospitality.utils.APPOINTMENT_APP_DETAIL
import com.sixgroup.hospitality.utils.APPOINTMENT_DOK_DETAIL
import com.sixgroup.hospitality.utils.APPOINTMENT_PAS_DETAIL
import com.sixgroup.hospitality.utils.STATUS_APP
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getDokterData
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getPasienData
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.SSLEngineResult.Status

class MyAppointmentRecyclerViewAdapter(
    private val values: ArrayList<AppointmentModel>,
    private val isDokter: Boolean,
    private val context: Context?,
) :
    RecyclerView.Adapter<MyAppointmentRecyclerViewAdapter.ViewHolder>() {

    lateinit var dokter: ArrayList<DokterModel>

    inner class ViewHolder(binding: ItemMyAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val userImage: ImageView = binding.userImage
        val nama: TextView = binding.nama
        val spesialis: TextView = binding.spesialis
        val tanggal: TextView = binding.tanggal
        val waktu: TextView = binding.waktu
        val cv: CardView = binding.cvAppointment
        val statusAcc: LinearLayout = binding.statusAcc

        override fun toString(): String {
            return ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMyAppointmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US)
        val sdftime = SimpleDateFormat("HH:mm", Locale.US)
        val sdfget = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)
        if (item.status!!.decryptCBC() == STATUS_APP.RJCT.toString()) {
            holder.statusAcc.setBackgroundResource(R.color.rjct)
        } else if (item.status!!.decryptCBC() == STATUS_APP.ACC.toString()) {
            holder.statusAcc.setBackgroundResource(R.color.acced)
        } else {
            holder.statusAcc.setBackgroundResource(R.color.nacced)
        }
        Log.d("CARIINIIII", "onBindViewHolder: " + item.status!!.decryptCBC() + " " + STATUS_APP.ACC.toString())
        if (isDokter) {
            getPasienData().observeForever {
                lateinit var selectedPasien: PasienModel
                for (i in it) {
                    if (item.pasien == i.idUser) selectedPasien = i
                }
                Picasso.get().load(selectedPasien.foto).into(holder.userImage)
                holder.nama.text = selectedPasien.nama!!.decryptCBC()
                holder.tanggal.text =
                    sdf.format(sdfget.parse(item.appointmentDate!!.decryptCBC())!!)
                holder.waktu.text =
                    sdftime.format(sdfget.parse(item.appointmentDate!!.decryptCBC())!!)
                holder.spesialis.text = item.complaint!!.decryptCBC()
                holder.cv.setOnClickListener {
                    val intent = Intent(context, AppointmentDetailActivity::class.java)
                    intent.putExtra(
                        APPOINTMENT_PAS_DETAIL,
                        Gson().toJson(selectedPasien) as String
                    )
                    intent.putExtra(
                        APPOINTMENT_APP_DETAIL,
                        Gson().toJson(item) as String
                    )
                    context!!.startActivity(intent)
                }
            }
        } else {
            getDokterData().observeForever {
                dokter = it
                lateinit var selectedDokter: DokterModel
                if (this::dokter.isInitialized) {
                    for (i in dokter) {
                        if (item.dokter == i.idUser) selectedDokter = i
                    }
                    Picasso.get().load(selectedDokter.foto).into(holder.userImage)
                    holder.nama.text = selectedDokter.nama!!.decryptCBC()
                    holder.tanggal.text =
                        sdf.format(sdfget.parse(item.appointmentDate!!.decryptCBC())!!)
                    holder.waktu.text =
                        sdftime.format(sdfget.parse(item.appointmentDate!!.decryptCBC())!!)
                    holder.spesialis.text = "Dokter " + selectedDokter.spesialis!!.decryptCBC()
                    holder.cv.setOnClickListener {
                        val intent = Intent(context, AppointmentDetailActivity::class.java)
                        intent.putExtra(
                            APPOINTMENT_DOK_DETAIL,
                            Gson().toJson(selectedDokter) as String
                        )
                        intent.putExtra(
                            APPOINTMENT_APP_DETAIL,
                            Gson().toJson(item) as String
                        )
                        context!!.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }
}