package com.sixgroup.hospitality.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sixgroup.hospitality.HomeActivity
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.RegisterActivity
import com.sixgroup.hospitality.model.PasienModel
import com.sixgroup.hospitality.utils.SECRET_IV
import com.sixgroup.hospitality.utils.SECRET_KEY
import kotlinx.android.synthetic.main.fragment_login.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        buttonLogin.setOnClickListener {
            val masuk = getPasienModel().login(requireContext(), this)
            masuk.observeForever {
                if (it) {
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "Email atau Password salah", Toast.LENGTH_SHORT).show()
                }
            }
        }
        registerButton.setOnClickListener {
            startActivity(Intent(requireContext(), RegisterActivity::class.java))
        }
    }

    private fun getPasienModel() : PasienModel {
        return PasienModel(
            email = emailInput.text.toString(),
            password = passwordInput.text.toString(),
            tglLahir = null
        )
    }
}