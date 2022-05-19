package com.example.exoesqueletov1.ui.dialogs

import androidx.appcompat.app.AppCompatDialogFragment
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.exoesqueletov1.R

class DialogLoading : AppCompatDialogFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        isCancelable = false
        val view: View = inflater.inflate(R.layout.dialog_loading, null)
        builder.setView(view)
        return builder.create()
    }
}