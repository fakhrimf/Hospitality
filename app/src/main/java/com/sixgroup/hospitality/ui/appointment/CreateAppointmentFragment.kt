package com.sixgroup.hospitality.ui.appointment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.SuccessAppointmentActivity
import com.sixgroup.hospitality.model.AppointmentModel
import com.sixgroup.hospitality.model.DokterModel
import com.sixgroup.hospitality.utils.STATUS_APP
import com.sixgroup.hospitality.utils.repository.Repository.Companion.addAppointment
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.encryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentUser
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getDokterData
import kotlinx.android.synthetic.main.fragment_create_appointment.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateAppointmentFragment : Fragment() {

    companion object {
        fun newInstance() = CreateAppointmentFragment()
    }

    private lateinit var dataDokter: ArrayList<DokterModel>
    private lateinit var idSelected: String
    private val namaDokter = ArrayList<String>()
    private val spDokter = ArrayList<String>()
    private val myCalendar = Calendar.getInstance()

    private lateinit var viewModel: CreateAppointmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreateAppointmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_create_appointment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        updateLabel()
    }

    @SuppressLint("SetTextI18n")
    private fun updateLabel() {
        val myFormat = "EEE, MMM dd"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        tanggalInput.text = dateFormat.format(myCalendar.time)
        val formatYear = "yyyy"
        val dateFormatYear = SimpleDateFormat(formatYear, Locale.US)
        tahunInput.text = dateFormatYear.format(myCalendar.time)
        jamInput.text = "${String.format("%02d",myCalendar.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d",myCalendar.get(Calendar.MINUTE))}"
    }

    private fun init() {
        backgroundDark.alpha = 0.7F
        spinner_Dokter.isClickable = false
        spinner_Dokter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val child: TextView = parent!!.getChildAt(0) as TextView
                    child.setTextColor(Color.BLACK)
                    for (i in dataDokter) {
                        if (i.nama!!.decryptCBC() == child.text.toString()) idSelected = i.idUser!!
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        getDokterData().observeForever {
            dataDokter = it
            backgroundDark.alpha = 0F
            val date =
                DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, month)
                    myCalendar.set(Calendar.DAY_OF_MONTH, day)
                    updateLabel()
                }
            val time =
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    myCalendar.set(Calendar.MINUTE, minute)
                    updateLabel()
                }
            cvInputTahun.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    R.style.DialogTheme,
                    date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH),
                ).show()
            }
            cvInputWaktu.setOnClickListener {
                TimePickerDialog(
                    requireContext(),
                    time,
                    myCalendar.get(Calendar.HOUR_OF_DAY),
                    myCalendar.get(Calendar.MINUTE),
                    true,
                ).show()
            }
            if (dataDokter.isNotEmpty()) {
                for (i in dataDokter) {
                    spDokter.add(i.spesialis!!.decryptCBC())
                }
                val adapter = ArrayAdapter<String>(
                    requireContext(),
                    R.layout.spinner_item,
                    spDokter,
                )
                spinner_dokterSpesialis.adapter = adapter
                spinner_dokterSpesialis.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?, view: View?, position: Int, id: Long
                        ) {
                            namaDokter.clear()
                            for (i in dataDokter) {
                                if (i.spesialis == dataDokter[position].spesialis) {
                                    namaDokter.add(i.nama!!.decryptCBC())
                                }
                            }
                            val adapterNama = ArrayAdapter<String>(
                                requireContext(),
                                R.layout.spinner_item,
                                namaDokter,
                            )
                            spinner_Dokter.adapter = adapterNama
                            spinner_Dokter.isClickable = true
                            val child: TextView = parent!!.getChildAt(0) as TextView
                            child.setTextColor(Color.BLACK)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }
            }
        }
        buatAppointmentButton.setOnClickListener {
            backgroundDark.alpha = 0.7F
            buatAppointmentButton.isClickable = false
            addAppointment(getAppointmentModel()).observeForever {
                backgroundDark.alpha = 0F
                if (it.success) {
                    requireActivity().finish()
                    startActivity(Intent(requireContext(), SuccessAppointmentActivity::class.java))
                } else {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getAppointmentModel() : AppointmentModel {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)

        return AppointmentModel(
            idAppointment = "",
            appointmentDate = sdf.format(myCalendar.time).encryptCBC(),
            complaint = keluhanInput.text.toString().encryptCBC(),
            status = STATUS_APP.NACC.toString().encryptCBC(),
            dokter = idSelected,
            pasien = getCurrentUser(requireContext())!!.idUser!!,
        )
    }
}