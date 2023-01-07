package com.sixgroup.hospitality.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sixgroup.hospitality.HomeActivity
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.RegisterActivity
import com.sixgroup.hospitality.model.DokterModel
import com.sixgroup.hospitality.model.PasienModel
import com.sixgroup.hospitality.utils.APP_SHARED_PREFERENCE
import com.sixgroup.hospitality.utils.repository.Repository
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getRememberMe
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getRememberMeDokter
import com.sixgroup.hospitality.utils.repository.Repository.Companion.removeDokterSP
import com.sixgroup.hospitality.utils.repository.Repository.Companion.removePasienSP
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Repository.storeOnboard(requireContext(), true)
        val w = requireActivity().window
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        w.statusBarColor = ContextCompat.getColor(requireActivity(), android.R.color.transparent)
        init()
    }

    private fun init() {
        if (getRememberMe(requireContext()) != null || getRememberMeDokter(requireContext()) != null) {
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            requireActivity().finish()
        }
        buttonLogin.setOnClickListener {
            removeDokterSP(requireContext())
            removePasienSP(requireContext())
            val sharedPref = requireContext().getSharedPreferences(
                APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE)
            sharedPref.edit().clear().apply()
            backgroundDark.alpha = 0.7F
            buttonLogin.isClickable = false
            var masuk = getPasienModel().login(requireActivity().applicationContext, this, checkBox.isChecked)
            masuk.observeForever { masukPasien ->
                if (masukPasien) {
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    requireActivity().finish()
                } else {
                    masuk = getDokterModel().login(requireActivity().applicationContext, this, checkBox.isChecked)
                    masuk.observeForever {
                        if (it) {
                            startActivity(Intent(requireContext(), HomeActivity::class.java))
                            requireActivity().finish()
                        } else {
                            backgroundDark.alpha = 0F
                            buttonLogin.isClickable = true
                            Toast.makeText(requireContext(), "Email atau Password salah", Toast.LENGTH_SHORT).show()
                        }
                    }
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
            tglLahir = null,
        )
    }

    private fun getDokterModel() : DokterModel {
        return DokterModel(
            email = emailInput.text.toString(),
            password = passwordInput.text.toString(),
        )
    }
}