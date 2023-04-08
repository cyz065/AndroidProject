package com.management.winwin.Dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.management.winwin.databinding.DialogSuccessBinding

class SuccessDialog(successDialogInterface: SuccessDialogInterface) :DialogFragment() {
    private lateinit var binding:DialogSuccessBinding
    private val successDialogInterface: SuccessDialogInterface

    init {
        this.successDialogInterface = successDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSuccessBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.confirm.setOnClickListener {
            this.successDialogInterface.onConfirmButtonClick()
            dismiss()
        }
        return view
    }
}

interface SuccessDialogInterface {
    fun onConfirmButtonClick()
}