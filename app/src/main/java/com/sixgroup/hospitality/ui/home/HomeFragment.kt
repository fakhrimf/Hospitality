package com.sixgroup.hospitality.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sixgroup.hospitality.MyAppointmentActivity
import com.sixgroup.hospitality.databinding.FragmentHomeBinding
import com.sixgroup.hospitality.ui.appointment.CreateAppointmentActivity
import com.sixgroup.hospitality.ui.home.news.ApiInterface
import com.sixgroup.hospitality.ui.home.news.RetrofitClient.Companion.getClient
import com.sixgroup.hospitality.ui.home.news.Status
import com.sixgroup.hospitality.ui.home.placeholder.PlaceholderContent
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentUser
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.spin_kit
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import kotlin.math.log

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var columnCount = 1

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiInterface = getClient().create(ApiInterface::class.java)
        val call = apiInterface.getStatus()
        val liveData = MutableLiveData<Status>()
        spin_kit.visibility = View.VISIBLE
        call.enqueue(object: Callback<Status> {
            override fun onResponse(call: Call<Status>, response: Response<Status>) {
                Log.d("FINDTHIS", "onResponse: ${response.code()}")
                if (response.body() != null)
                    liveData.value = response.body()
                spin_kit.visibility = View.GONE
                Log.d("TAG", "onResponse: ${response.body()}")
            }

            override fun onFailure(call: Call<Status>, t: Throwable) {
                call.cancel()
            }
        })

        // Set the adapter
        with(list) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            liveData.observeForever {
                adapter = PostRecyclerViewAdapter(it.articles, context)
            }
        }
        fab.setOnClickListener {
            startActivity(Intent(requireContext(), CreateAppointmentActivity::class.java))
        }
        if (getCurrentUser(requireContext()) == null) {
            fab.isVisible = false
            fab.isClickable = false
        }
        cvMyAppointment.setOnClickListener {
            startActivity(Intent(requireContext(), MyAppointmentActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}