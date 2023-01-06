package com.sixgroup.hospitality.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.RegisterDokterActivity
import kotlinx.android.synthetic.main.fragment_confirm_dokter.*

class ConfirmDokterFragment : Fragment() {

    companion object {
        fun newInstance() = ConfirmDokterFragment()
    }

    private lateinit var viewModel: ConfirmDokterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConfirmDokterViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_confirm_dokter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        confirm_button.setOnClickListener {
            startActivity(Intent(requireContext(), RegisterDokterActivity::class.java))
        }
    }
}