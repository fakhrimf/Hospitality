package com.sixgroup.hospitality.ui.scanqr

import android.Manifest.permission.CAMERA
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.sixgroup.hospitality.R
import com.sixgroup.hospitality.utils.CAMERA_REQUEST_CODE
import kotlinx.android.synthetic.main.fragment_scan_qr.*

class ScanQRFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner

    companion object {
        fun newInstance() = ScanQRFragment()
    }

    private lateinit var viewModel: ScanQRViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scan_qr, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ScanQRViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        ActivityCompat.requestPermissions(activity, arrayOf(CAMERA), CAMERA_REQUEST_CODE)
        codeScanner = CodeScanner(activity, scanner)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
            }
        }
        scanner.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}