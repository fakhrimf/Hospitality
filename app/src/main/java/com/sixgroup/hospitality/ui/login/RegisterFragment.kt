package com.sixgroup.hospitality.ui.login

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sixgroup.hospitality.ConfirmDokterActivity
import com.sixgroup.hospitality.HomeActivity
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.model.PasienModel
import com.sixgroup.hospitality.utils.CAMERA_REQUEST_CODE
import com.sixgroup.hospitality.utils.IMAGE_REQUEST_CODE
import com.sixgroup.hospitality.utils.repository.Repository.Companion.encryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getImageBitmap
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getImageIntent
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.backgroundDark
import kotlinx.android.synthetic.main.fragment_register.emailInput
import kotlinx.android.synthetic.main.fragment_register.passwordInput
import kotlinx.android.synthetic.main.fragment_register.registerButton
import java.text.SimpleDateFormat
import java.util.*


class RegisterFragment : Fragment() {
    var path: Uri? = null
    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel
    private val myCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val w = requireActivity().window
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        w.statusBarColor = ContextCompat.getColor(requireActivity(), android.R.color.transparent)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && data != null && data.data != null) {
            path = data.data!!
            val bitmap = getImageBitmap(requireActivity().contentResolver, path!!)
            userDP.setImageBitmap(bitmap)
        }
    }

    private fun init() {
        val date =
            OnDateSetListener { _, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }
        birthInput.setOnClickListener(View.OnClickListener {
            DatePickerDialog(
                requireContext(),
                R.style.DialogTheme,
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH),
            ).show()
        })
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        registerButton.setOnClickListener {
            val check = isEmpty()
            backgroundDark.alpha = 0.7F
            registerButton.isClickable = false
            if (!check) {
                val message = getPasienModel().registerProfile(requireContext(), path)
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
            startActivityForResult(getImageIntent(), IMAGE_REQUEST_CODE)
        }
        userImageRegisterEditBtn.setOnClickListener {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            startActivityForResult(getImageIntent(), IMAGE_REQUEST_CODE)
        }
        registerDokter.setOnClickListener {
            startActivity(Intent(requireContext(), ConfirmDokterActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun updateLabel(){
        val myFormat = "dd/MM/yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.JAPAN);
        birthInput.setText(dateFormat.format(myCalendar.time));
    }

    private fun isEmpty(): Boolean {
        var check = false
        if (emailInput.text.toString().isEmpty()) {
            check = true
            emailInput.error = getString(R.string.email_error_empty)
        }
        if (birthInput.text.toString().isEmpty()) {
            check = true
            birthInput.error = getString(R.string.birthdate_error_empty)
        }
        if (nameInput.text.toString().isEmpty()) {
            check = true
            nameInput.error = getString(R.string.name_error_empty)
        }
        if (phoneInput.text.toString().isEmpty()) {
            check = true
            phoneInput.error = getString(R.string.phone_error_empty)
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

    private fun getPasienModel() : PasienModel {
        return PasienModel(
            nama = nameInput.text.toString().encryptCBC(),
            noHP = phoneInput.text.toString().encryptCBC(),
            tglLahir = birthInput.text.toString().encryptCBC(),
            idUser = "",
            password = passwordInput.text.toString().encryptCBC(),
            email = emailInput.text.toString().encryptCBC(),
            foto = "",
        )
    }
}