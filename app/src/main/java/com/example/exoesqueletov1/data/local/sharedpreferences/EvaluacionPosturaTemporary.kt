package com.example.exoesqueletov1.data.local.sharedpreferences

class EvaluacionPosturaTemporary {
    var grados = ""
    var observaciones = ""
    var position = 0
    var name = ""

    fun isNotEmpty() = grados.isNotEmpty() || observaciones.isNotEmpty()
}