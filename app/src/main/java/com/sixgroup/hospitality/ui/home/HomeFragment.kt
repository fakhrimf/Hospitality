package com.sixgroup.hospitality.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sixgroup.hospitality.MyAppointmentActivity
import com.sixgroup.hospitality.databinding.FragmentHomeBinding
import com.sixgroup.hospitality.ui.appointment.CreateAppointmentActivity
import com.sixgroup.hospitality.ui.home.placeholder.PlaceholderContent
import com.sixgroup.hospitality.utils.repository.Repository.Companion.getCurrentUser
import kotlinx.android.synthetic.main.fragment_home.*

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
        // Set the adapter
        with(list) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = PostRecyclerViewAdapter(PlaceholderContent.ITEMS)
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