package com.sixgroup.hospitality.ui.payment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.utils.backgroundFadeInDuration
import com.sixgroup.hospitality.utils.titleFadeInDuration
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.android.synthetic.main.fragment_success_payment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SuccessPaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SuccessPaymentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val mRunnable = Runnable {
        requireActivity().finish()
    }
    private val mHandler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        background.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate().apply {
                alpha(1f)
                duration = backgroundFadeInDuration
            }
        }
        icon.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate().apply {
                alpha(1f)
                duration = titleFadeInDuration
                setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        mHandler.postDelayed(mRunnable, titleFadeInDuration)
                    }
                })
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_success_payment, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SuccessPaymentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SuccessPaymentFragment()
    }
}