package com.sixgroup.hospitality.ui.onboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sixgroup.hospitality.R

class OnboardMainFragment : Fragment() {

    companion object {
        fun newInstance() = OnboardMainFragment()
    }

    private lateinit var viewModel: OnboardMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[OnboardMainViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_onboard_main, container, false)
    }

}