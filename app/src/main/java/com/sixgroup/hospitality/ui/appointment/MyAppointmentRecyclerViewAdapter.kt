package com.sixgroup.hospitality.ui.appointment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sixgroup.hospitality.databinding.ItemMyAppointmentBinding
import com.sixgroup.hospitality.model.AppointmentModel
import com.sixgroup.hospitality.model.DokterModel
import com.sixgroup.hospitality.model.PasienModel
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getDokterData
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getPasienData
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class MyAppointmentRecyclerViewAdapter(
    private val values: ArrayList<AppointmentModel>,
    private val isDokter: Boolean
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
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }
}