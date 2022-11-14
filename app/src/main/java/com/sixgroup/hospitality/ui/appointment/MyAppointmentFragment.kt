package com.sixgroup.hospitality.ui.appointment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sixgroup.hospitality.R

class MyAppointmentFragment : Fragment() {

    companion object {
        fun newInstance() = MyAppointmentFragment()
    }

    private lateinit var viewModel: MyAppointmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyAppointmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_myappointment, container, false)
    }

}