package com.example.exoesqueletov1.data.sharedpreferences

import com.example.exoesqueletov1.data.models.ExpedientModel

class PatientTemporary() {
    var idPatient = ""
    var name = ""
    var birthday = ""
    var gender = ""
    var occupation = ""
    var email = ""
    var phone = ""
    var lada = ""
    var address = ""
    var listAntecedents = mutableListOf<ExpedientModel>()
    var listHabits = mutableListOf<ExpedientModel>()
    var listCicatriz = mutableListOf<ExpedientModel>()
}