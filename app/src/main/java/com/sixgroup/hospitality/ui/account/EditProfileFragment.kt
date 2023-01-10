package com.sixgroup.hospitality.ui.account

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.utils.CAMERA_REQUEST_CODE
import com.sixgroup.hospitality.utils.IMAGE_REQUEST_CODE
import com.sixgroup.hospitality.utils.repository.Repository
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.encryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentDokter
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    private val dokter by lazy {
        getCurrentDokter(requireContext())
    }

    private val pasien by lazy {
        getCurrentUser(requireContext())
    }

    private lateinit var namaBefore: String
    private lateinit var emailBefore: String
    private lateinit var hpBefore: String
    var path: Uri? = null
    private lateinit var viewModel: EditProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
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
        init()
    }

    private fun init() {
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        userImageRegister.setOnClickListener {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE
            )
            startActivityForResult(Repository.getImageIntent(), IMAGE_REQUEST_CODE)
        }
        userImageRegisterEditBtn.setOnClickListener {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE
            )
            startActivityForResult(Repository.getImageIntent(), IMAGE_REQUEST_CODE)
        }
        if (dokter != null) {
            nohplayout.hint = "Tahun Pengalaman"
            emailInputEditProfil.setText(dokter!!.email!!.decryptCBC())
            emailUser.text = dokter!!.email!!.decryptCBC()
            emailBefore = dokter!!.email!!.decryptCBC()
            namaInputEdit.setText(dokter!!.nama!!.decryptCBC())
            namaBefore = dokter!!.nama!!.decryptCBC()
            namaUser.text = dokter!!.nama!!.decryptCBC()
            phoneInputEdit.setText(dokter!!.yoe!!)
            hpBefore = dokter!!.yoe!!.toString()
            if (dokter!!.foto!!.isNotEmpty()) {
                Picasso.get().load(dokter!!.foto!!).into(userDP)
            }
        } else if (pasien != null) {
            emailInputEditProfil.setText(pasien!!.email!!.decryptCBC())
            emailUser.text = pasien!!.email!!.decryptCBC()
            emailBefore = pasien!!.email!!.decryptCBC()
            namaInputEdit.setText(pasien!!.nama!!.decryptCBC())
            namaBefore = pasien!!.nama!!.decryptCBC()
            namaUser.text = pasien!!.nama!!.decryptCBC()
            phoneInputEdit.setText(pasien!!.noHP!!.decryptCBC())
            hpBefore = pasien!!.noHP!!.decryptCBC()
            if (pasien!!.foto!!.isNotEmpty()) {
                Picasso.get().load(pasien!!.foto!!).into(userDP)
            }
        }
        editprofilebutton.setOnClickListener {
            if (!isEmptyOrNotChanged()) {
                backgroundDark.alpha = 0.7f
                editprofilebutton.isClickable = false
                if (dokter != null) {
                    dokter!!.editProfil(
                        nama = namaInputEdit.text.toString().encryptCBC(),
                        email = emailInputEditProfil.text.toString().encryptCBC(),
                        yoe = phoneInputEdit.text.toString().toInt(),
                        context = requireContext(),
                        path = path,
                    ).observeForever {
                        if (it.success) {
                            Toast.makeText(
                                requireContext(), "Edit Profil Sukses!", Toast.LENGTH_LONG
                            ).show()
                            backgroundDark.alpha = 0f
                            editprofilebutton.isClickable = true
                            requireActivity().finish()
                        } else {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                            backgroundDark.alpha = 0f
                            editprofilebutton.isClickable = true
                        }
                    }
                } else if (pasien != null) {
                    pasien!!.editProfile(
                        nama = namaInputEdit.text.toString().encryptCBC(),
                        email = emailInputEditProfil.text.toString().encryptCBC(),
                        noHP = phoneInputEdit.text.toString().encryptCBC(),
                        context = requireContext(),
                        path = path,
                    ).observeForever {
                        if (it.success) {
                            Toast.makeText(
                                requireContext(), "Edit Profil Sukses!", Toast.LENGTH_LONG
                            ).show()
                            backgroundDark.alpha = 0f
                            editprofilebutton.isClickable = true
                            requireActivity().finish()
                        } else {
                            backgroundDark.alpha = 0f
                            editprofilebutton.isClickable = true
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun isEmptyOrNotChanged(): Boolean {
        var check = false
        if (emailInputEditProfil.text.toString() == emailBefore && namaInputEdit.text.toString() == namaBefore && phoneInputEdit.text.toString() == hpBefore) {
            check = true
            if (path != null) {
                check = false
            }
        }
        if (emailInputEditProfil.text.toString().isEmpty()) {
            check = true
            emailInputEditProfil.error = "Email Kosong!"
        }
        if (namaInputEdit.text.toString().isEmpty()) {
            check = true
            namaInputEdit.error = "Nama Kosong!"
        }
        if (phoneInputEdit.text.toString().isEmpty()) {
            check = true
            phoneInputEdit.error = "Nomor Kosong!"
        }
        return check
    }
}