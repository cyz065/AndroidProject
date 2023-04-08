package com.management.winwin.Dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.management.winwin.databinding.DialogFailBinding

class FailedDialog(failedDialogInterface: FailedDialogInterface) :DialogFragment() {
    private lateinit var binding : DialogFailBinding
    private val failedDialogInterface:FailedDialogInterface

    init {
        this.failedDialogInterface = failedDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFailBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.confirm.setOnClickListener {
            this.failedDialogInterface.onFailButtonClick()
            dismiss()
        }
        return view
    }

}

interface FailedDialogInterface {
    fun onFailButtonClick()
}