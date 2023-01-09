package com.sixgroup.hospitality.ui.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sixgroup.hospitality.ChangePasswordActivity
import com.sixgroup.hospitality.EditProfileActivity
import com.sixgroup.hospitality.LoginActivity
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentDokter
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_myaccount.*

class MyAccountFragment : Fragment() {

    companion object {
        fun newInstance() = MyAccountFragment()
    }

    private lateinit var viewModel: MyAccountViewModel
    private val model by lazy {
        getCurrentUser(requireActivity())
    }
    private val modelDokter by lazy {
        getCurrentDokter(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MyAccountViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_myaccount, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        if (model != null) {
            val picasso = Picasso.get()
            picasso.setIndicatorsEnabled(true)
            if (model!!.foto!!.isNotEmpty()) picasso.load(model!!.foto!!).into(userDP)
            if (model!!.nama!!.isNotEmpty()) namaUser.text = model!!.nama!!.decryptCBC()
            if (model!!.noHP!!.isNotEmpty()) hpUser.text = model!!.noHP!!.decryptCBC()
            logoutLayout.setOnClickListener {
                model!!.logout(requireContext())
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
                Toast.makeText(requireContext(), "Logout berhasil!", Toast.LENGTH_LONG).show()
            }
            linearLayoutgp.setOnClickListener {
                startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
            }
            linearLayoutEdit.setOnClickListener {
                startActivity(Intent(requireContext(), EditProfileActivity::class.java))
            }
        } else if (modelDokter != null) {
            val picasso = Picasso.get()
            picasso.setIndicatorsEnabled(true)
            if (modelDokter!!.foto!!.isNotEmpty()) picasso.load(modelDokter!!.foto!!).into(userDP)
            if (modelDokter!!.nama!!.isNotEmpty()) namaUser.text = modelDokter!!.nama!!.decryptCBC()
            if (modelDokter!!.spesialis!!.isNotEmpty()) hpUser.text = "Spesialis " + modelDokter!!.spesialis!!.decryptCBC()
            logoutLayout.setOnClickListener {
                modelDokter!!.logout(requireContext())
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
                Toast.makeText(requireContext(), "Logout berhasil!", Toast.LENGTH_LONG).show()
            }
            linearLayoutgp.setOnClickListener {
                startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
            }
            linearLayoutEdit.setOnClickListener {
                startActivity(Intent(requireContext(), EditProfileActivity::class.java))
            }
        }
    }
}