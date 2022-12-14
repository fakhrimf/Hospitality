package com.sixgroup.hospitality.ui.login

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sixgroup.hospitality.HomeActivity
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.model.DokterModel
import com.sixgroup.hospitality.utils.CAMERA_REQUEST_CODE
import com.sixgroup.hospitality.utils.IMAGE_REQUEST_CODE
import com.sixgroup.hospitality.utils.repository.Repository
import com.sixgroup.hospitality.utils.repository.Repository.Companion.encryptCBC
import kotlinx.android.synthetic.main.fragment_register_dokter.*
import kotlinx.android.synthetic.main.fragment_register_dokter.backButton
import kotlinx.android.synthetic.main.fragment_register_dokter.backgroundDark
import kotlinx.android.synthetic.main.fragment_register_dokter.emailInput
import kotlinx.android.synthetic.main.fragment_register_dokter.nameInput
import kotlinx.android.synthetic.main.fragment_register_dokter.passwordInput
import kotlinx.android.synthetic.main.fragment_register_dokter.registerButton
import kotlinx.android.synthetic.main.fragment_register_dokter.userDP
import kotlinx.android.synthetic.main.fragment_register_dokter.userImageRegister
import kotlinx.android.synthetic.main.fragment_register_dokter.userImageRegisterEditBtn

class RegisterDokterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterDokterFragment()
    }

    var path: Uri? = null
    private lateinit var viewModel: RegisterDokterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterDokterViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_register_dokter, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && data != null && data.data != null) {
            path = data.data!!
            val bitmap = Repository.getImageBitmap(requireActivity().contentResolver, path!!)
            userDP.setImageBitmap(bitmap)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val w = requireActivity().window
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        w.statusBarColor = ContextCompat.getColor(requireActivity(), android.R.color.transparent)
        init()
    }

    private fun init() {
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        registerButton.setOnClickListener {
            val check = isEmpty()
            backgroundDark.alpha = 0.7F
            registerButton.isClickable = false
            if (!check) {
                val message = getDokterModel().registerProfile(requireContext(), path)
                message.observeForever {
                    if (it.success) {
                        startActivity(Intent(requireContext(), HomeActivity::class.java))
                        requireActivity().finish()
                    } else {
                        backgroundDark.alpha = 0F
                        registerButton.isClickable = true
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                backgroundDark.alpha = 0F
                registerButton.isClickable = true
            }
        }
        userImageRegister.setOnClickListener {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            startActivityForResult(Repository.getImageIntent(), IMAGE_REQUEST_CODE)
        }
        userImageRegisterEditBtn.setOnClickListener {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            startActivityForResult(Repository.getImageIntent(), IMAGE_REQUEST_CODE)
        }
    }

    private fun isEmpty(): Boolean {
        var check = false
        if (emailInput.text.toString().isEmpty()) {
            check = true
            emailInput.error = getString(R.string.email_error_empty)
        }
        if (nameInput.text.toString().isEmpty()) {
            check = true
            nameInput.error = getString(R.string.name_error_empty)
        }
        if (yoeInput.text.toString().isEmpty()) {
            check = true
            yoeInput.error = getString(R.string.yoe_error_empty)
        }
        if (spInput.text.toString().isEmpty()) {
            check = true
            spInput.error = getString(R.string.spesialis_error_empty)
        }
        if (passwordInput.text.toString().length < 8) {
            check = true
            passwordInput.error = getString(R.string.password_error_lessthaneight)
        }
        if (passwordInput.text.toString().isEmpty()) {
            check = true
            passwordInput.error = getString(R.string.password_error_empty)
        }
        return check
    }

    private fun getDokterModel() : DokterModel {
        return DokterModel(
            nama = nameInput.text.toString().encryptCBC(),
            spesialis = spInput.text.toString().encryptCBC(),
            yoe = yoeInput.text.toString().toInt(),
            idUser = "",
            password = passwordInput.text.toString().encryptCBC(),
            email = emailInput.text.toString().encryptCBC(),
            foto = "",
        )
    }
}