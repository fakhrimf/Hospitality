package com.sixgroup.hospitality.ui.account

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_myaccount.*
import kotlinx.android.synthetic.main.fragment_myaccount.userDP
import kotlinx.android.synthetic.main.fragment_register.*

class MyAccountFragment : Fragment() {

    companion object {
        fun newInstance() = MyAccountFragment()
    }

    private lateinit var viewModel: MyAccountViewModel
    private val model by lazy {
        getCurrentUser(requireActivity())
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        if (model != null) {
            if (model!!.foto!!.isNotEmpty()) Picasso.get().load(model!!.foto!!).into(userDP)
            if (model!!.nama!!.isNotEmpty()) namaUser.text = model!!.nama!!.decryptCBC()
            if (model!!.noHP!!.isNotEmpty()) hpUser.text = model!!.noHP!!.decryptCBC()
        }
    }
}