package com.sixgroup.hospitality.ui.payment

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.SuccessPaymentActivity
import com.sixgroup.hospitality.utils.repository.Repository.Companion.decryptCBC
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getDokterData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_payment_new.*
import kotlinx.android.synthetic.main.fragment_payment_new.view.*
import kotlin.random.Random

class PaymentFragment : Fragment() {

    companion object {
        fun newInstance() = PaymentFragment()
    }

    private lateinit var viewModel: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PaymentViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_payment_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = getDokterData()
        data.observeForever {
            val rand = Random.nextInt(0, it.size)
            val randMoney = Random.nextInt(100000, 400000)
            val picked = it[rand]
            Picasso.get().load(picked.foto).into(imgUser)
            namaDokter.text = picked.nama!!.decryptCBC()
            totalBayar.text = "Rp. " + randMoney
        }
        pay.setOnClickListener {
            startActivity(Intent(requireContext(), SuccessPaymentActivity::class.java))
            requireActivity().finish()
        }
    }

}