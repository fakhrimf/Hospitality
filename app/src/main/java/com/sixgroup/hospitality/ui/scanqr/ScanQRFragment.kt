package com.sixgroup.hospitality.ui.scanqr

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.sixgroup.hospitality.R
import kotlinx.android.synthetic.main.fragment_scan_qr.*

class ScanQRFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner

    companion object {
        fun newInstance() = ScanQRFragment()
    }

    private lateinit var viewModel: ScanQRViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scan_qr, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScanQRViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scanner)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
            }
        }
        scanner.setOnClickListener {
            codeScanner.startPreview()
        }
    }
}