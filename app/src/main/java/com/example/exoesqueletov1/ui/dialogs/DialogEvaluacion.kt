package com.example.exoesqueletov1.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.exoesqueletov1.R

class DialogEvaluacion() :
    AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater = requireActivity().layoutInflater
        view = inflater.inflate(R.layout.dialog_evaluacion, null)
        builder.setView(view)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        return builder.create()
    }
}