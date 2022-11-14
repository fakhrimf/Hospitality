package com.sixgroup.hospitality.ui.appointment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sixgroup.hospitality.R

class CreateAppointmentFragment : Fragment() {

    companion object {
        fun newInstance() = CreateAppointmentFragment()
    }

    private lateinit var viewModel: CreateAppointmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreateAppointmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_create_appointment, container, false)
    }

}