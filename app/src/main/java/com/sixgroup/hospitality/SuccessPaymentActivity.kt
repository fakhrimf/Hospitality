package com.sixgroup.hospitality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sixgroup.hospitality.ui.payment.SuccessPaymentFragment

class SuccessPaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_payment)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SuccessPaymentFragment.newInstance())
                .commitNow()
        }
    }
}