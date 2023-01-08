package com.sixgroup.hospitality.ui.appointment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.sixgroup.hospitality.AppointmentDetailActivity
import com.sixgroup.hospitality.ChatActivity
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.utils.APPOINTMENT_APP_DETAIL
import com.sixgroup.hospitality.utils.APPOINTMENT_PAS_DETAIL
import com.sixgroup.hospitality.utils.STATUS_APP
import com.sixgroup.hospitality.utils.repository.Repository.Companion.accApt
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.rjctApt
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_appointment_detail.*
import java.text.SimpleDateFormat
import java.util.*

class AppointmentDetailFragment : Fragment() {

    companion object {
        fun newInstance() = AppointmentDetailFragment()
    }

    private lateinit var viewModel: AppointmentDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AppointmentDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_appointment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        val act = requireActivity() as AppointmentDetailActivity
        val dokter = act.getDokter()
        val pasien = act.getPasien()
        val apt = act.getAppt()
        val sdfget = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)
        val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US)
        val sdftime = SimpleDateFormat("HH:mm", Locale.US)
        if (dokter != null && apt != null) {
            val parsed = sdfget.parse(apt.appointmentDate!!.decryptCBC())!!
            namaDetail.text = dokter.nama!!.decryptCBC()
            spesialisDetail.text = dokter.spesialis!!.decryptCBC()
            yoeDetail.text = "Pengalaman : " + dokter.yoe.toString() + " Tahun"
            Picasso.get().load(dokter.foto!!).into(imgUser)
            jadwalDetail.text = sdf.format(parsed) + "\nJam " + sdftime.format(parsed)
            keluhanUser.text = apt.complaint!!.decryptCBC()
            cvChat.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra(
                    APPOINTMENT_PAS_DETAIL,
                    Gson().toJson(dokter) as String
                )
                intent.putExtra(
                    APPOINTMENT_APP_DETAIL,
                    Gson().toJson(apt) as String
                )
                startActivity(intent)
            }
            when (apt.status!!.decryptCBC()) {
                STATUS_APP.ACC.toString() -> {
                    statusDetail.text = "Status : Accepted"
                    cvAcc.visibility = View.GONE
                    rejectBtn.visibility = View.GONE
                }
                STATUS_APP.RJCT.toString() -> {
                    statusDetail.text = "Status : Rejected"
                    cvAcc.visibility = View.GONE
                    rejectBtn.visibility = View.GONE
                    cvChat.visibility = View.GONE
                }
                else -> {
                    statusDetail.text = "Status : Pending"
                    cvChat.visibility = View.GONE
                }
            }
        } else if (pasien != null && apt != null) {
            val parsed = sdfget.parse(apt.appointmentDate!!.decryptCBC())!!
            namaDetail.text = pasien.nama!!.decryptCBC()
            spesialisDetail.text = pasien.noHP!!.decryptCBC()
            yoeDetail.text = pasien.email!!.decryptCBC()
            val foto = pasien.foto
            if (foto != null && foto.isNotEmpty())
                Picasso.get().load(pasien.foto!!).into(imgUser)
            jadwalDetail.text = sdf.format(parsed) + "\nJam " + sdftime.format(parsed)
            keluhanUser.text = apt.complaint!!.decryptCBC()
            cvAcc.visibility = View.VISIBLE
            rejectBtn.visibility = View.VISIBLE
            if (apt.status!!.decryptCBC() == STATUS_APP.ACC.toString() || apt.status!!.decryptCBC() == STATUS_APP.ACC.toString()) {
                cvAcc.visibility = View.GONE
                rejectBtn.visibility = View.GONE
            }
            cvAcc.setOnClickListener {
                accApt(apt).observeForever {
                    if (it.success) {
                        Toast.makeText(requireContext(), "ACC Sukses", Toast.LENGTH_LONG).show()
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
            rejectBtn.setOnClickListener {
                rjctApt(apt).observeForever {
                    if (it.success) {
                        Toast.makeText(requireContext(), "Reject Sukses", Toast.LENGTH_LONG).show()
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
            cvChat.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra(
                    APPOINTMENT_PAS_DETAIL,
                    Gson().toJson(pasien) as String
                )
                intent.putExtra(
                    APPOINTMENT_APP_DETAIL,
                    Gson().toJson(apt) as String
                )
                startActivity(intent)
            }
            when (apt.status!!.decryptCBC()) {
                STATUS_APP.ACC.toString() -> {
                    statusDetail.text = "Status : Accepted"
                }
                STATUS_APP.RJCT.toString() -> {
                    statusDetail.text = "Status : Rejected"
                    cvChat.visibility = View.GONE
                }
                else -> {
                    statusDetail.text = "Status : Pending"
                    cvChat.visibility = View.GONE
                }
            }
        }
    }

}