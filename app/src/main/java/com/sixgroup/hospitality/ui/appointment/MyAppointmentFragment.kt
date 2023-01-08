package com.sixgroup.hospitality.ui.appointment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.model.AppointmentModel
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getAppointmentDokter
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getAppointmentPasien
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentDokter
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentUser
import kotlinx.android.synthetic.main.fragment_myappointment.*

class MyAppointmentFragment : Fragment() {

    companion object {
        fun newInstance() = MyAppointmentFragment()
    }

    private lateinit var viewModel: MyAppointmentViewModel
    private lateinit var listAppointment: ArrayList<AppointmentModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MyAppointmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_myappointment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (getCurrentUser(requireContext()) == null) {
            val i = getAppointmentDokter(getCurrentDokter(requireContext())!!)
            i.observeForever {
                listAppointment = it
                if (list != null)
                    list.adapter = MyAppointmentRecyclerViewAdapter(listAppointment, true, context)
            }
        } else {
            getAppointmentPasien(getCurrentUser(requireContext())!!).observeForever {
                listAppointment = it
                if (list != null)
                    list.adapter = MyAppointmentRecyclerViewAdapter(listAppointment, false, context)
            }
            list.layoutManager = LinearLayoutManager(requireContext())
        }
        backbtn.setOnClickListener {
            requireActivity().finish()
        }
    }
}