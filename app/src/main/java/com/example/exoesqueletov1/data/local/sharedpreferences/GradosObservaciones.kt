package com.example.exoesqueletov1.data.local.sharedpreferences

class GradosObservaciones {
    var grados = ""
    var observaciones = ""
    var position = 0

    fun isNotEmpty() = grados.isNotEmpty() && observaciones.isNotEmpty()
}