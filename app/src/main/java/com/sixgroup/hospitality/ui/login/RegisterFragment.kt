package com.sixgroup.hospitality.ui.login

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.model.PasienModel
import com.sixgroup.hospitality.utils.SECRET_IV
import com.sixgroup.hospitality.utils.SECRET_KEY
import kotlinx.android.synthetic.main.fragment_register.*
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel
    private val myCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
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
        init()
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
            if (!check) {
//                Toast.makeText(requireContext(), "Proceed Register", Toast.LENGTH_SHORT).show()
                val message = getPasienModel().registerProfile(requireContext())
                message.observeForever {
                    if (it.success) {
                        Toast.makeText(requireContext(), "Success Register", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
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
            tglLahir = myCalendar.time.toString().encryptCBC(),
            idUser = "",
            password = passwordInput.text.toString().encryptCBC(),
            email = emailInput.text.toString().encryptCBC(),
            foto = "",
        )
    }

    private fun String.encryptCBC(): String {
        val iv = IvParameterSpec(SECRET_IV.toByteArray())
        val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
        val crypted = cipher.doFinal(this.toByteArray())
        val encodedByte = Base64.encode(crypted, Base64.DEFAULT)
        return String(encodedByte)
    }

    private fun String.decryptCBC(): String {
        val decodedByte: ByteArray = Base64.decode(this, Base64.DEFAULT)
        val iv = IvParameterSpec(SECRET_IV.toByteArray())
        val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)
        val output = cipher.doFinal(decodedByte)
        return String(output)
    }
}