package com.sixgroup.hospitality.ui.account

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.encryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentDokter
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentUser
import kotlinx.android.synthetic.main.fragment_change_password.*

class ChangePasswordFragment : Fragment() {

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        btn_change_password.setOnClickListener {
            val passwordLama = passwordInput.text
            val passwordBaru = passwordInputBaru.text
            val konfirmPass = passwordInputKonfirm.text
            val dokter = getCurrentDokter(requireContext())
            val pasien = getCurrentUser(requireContext())

            if (!isEmpty())
            if (pasien != null) {
                if (passwordLama.toString() == pasien.password!!.decryptCBC()) {
                    if (passwordBaru.toString() == konfirmPass.toString()) {
                        pasien.editProfile(password = passwordBaru.toString().encryptCBC(), context = requireContext()).observeForever {
                            if (it.success) {
                                Toast.makeText(requireContext(), "Sukses mengubah password", Toast.LENGTH_LONG).show()
                                requireActivity().finish()
                            } else {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            } else if (dokter != null) {
                if (passwordLama.toString() == dokter.password!!.decryptCBC()) {
                    if (passwordBaru.toString() == konfirmPass.toString()) {
                        dokter.editProfil(password = passwordBaru.toString().encryptCBC(), context = requireContext()).observeForever {
                            if (it.success) {
                                Toast.makeText(requireContext(), "Sukses mengubah password", Toast.LENGTH_LONG).show()
                                requireActivity().finish()
                            } else {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
            else Toast.makeText(requireContext(), "Input tidak boleh kosong", Toast.LENGTH_LONG).show()
        }
    }

    private fun isEmpty(): Boolean {
        return passwordInput.text.toString().isEmpty() || passwordInputBaru.text.toString().isEmpty() || passwordInputKonfirm.text.toString().isEmpty()
    }
}