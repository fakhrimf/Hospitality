package com.sixgroup.hospitality.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.firebase.database.core.Repo
import com.sixgroup.hospitality.HomeActivity
import com.sixgroup.hospitality.LoginActivity
import com.sixgroup.hospitality.OnboardActivity
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.utils.backgroundFadeInDuration
import com.sixgroup.hospitality.utils.repository.Repository
import com.sixgroup.hospitality.utils.repository.Repository.Companion.storeOnboard
import com.sixgroup.hospitality.utils.titleFadeInDuration
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private val model by lazy {
        Repository.getOnboard(requireActivity())
    }

    private val model2 by lazy {
        Repository.getCurrentUser(requireActivity())
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val w = requireActivity().window
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        w.statusBarColor = ContextCompat.getColor(requireActivity(), android.R.color.transparent)
        splash()
    }

    private val mRunnable = Runnable {
        if (!model || model2 != null) {
            startActivity(Intent(requireContext(), OnboardActivity::class.java))
            requireActivity().finish()
        } else {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun splash() {
        main.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate().apply {
                alpha(1f)
                duration = backgroundFadeInDuration
            }
        }
        title_splash.apply {
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

    override fun onDestroy() {
        mHandler.removeCallbacks(mRunnable)
        super.onDestroy()
    }
}