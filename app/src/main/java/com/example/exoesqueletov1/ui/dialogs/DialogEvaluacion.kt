package com.example.exoesqueletov1.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.exoesqueletov1.R

class DialogEvaluacion() : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding = DialogEvaluacionBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.dialog_evaluacion, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(DialogFragment.STYLE_NO_INPUT, R.style.MaterialAlertDialog_Material3_Title_Panel)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }


}