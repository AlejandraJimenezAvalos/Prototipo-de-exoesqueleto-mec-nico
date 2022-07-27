package com.example.exoesqueletov1.ui.fragments.patient.ui.consultations.consultation

import android.view.View
import com.example.exoesqueletov1.data.local.relations.ConsultationRelation

class Data(consultation: ConsultationRelation) {
    val libre = if (consultation.marcha!!.marcha!!.libre) View.VISIBLE else View.GONE
    val claudante = if (consultation.marcha!!.marcha!!.claudante) View.VISIBLE else View.GONE
    val conAyuda = if (consultation.marcha!!.marcha!!.conAyuda) View.VISIBLE else View.GONE
    val espaticas = if (consultation.marcha!!.marcha!!.espaticas) View.VISIBLE else View.GONE
    val ataxica = if (consultation.marcha!!.marcha!!.ataxica) View.VISIBLE else View.GONE

    val dolor =
        when (consultation.consultation!!.dolor) {
            "0" -> "${consultation.consultation.dolor} Sin dolor"
            "1" -> "${consultation.consultation.dolor} Sin dolor"
            "2" -> "${consultation.consultation.dolor} Poco dolor"
            "3" -> "${consultation.consultation.dolor} Poco dolor"
            "4" -> "${consultation.consultation.dolor} Dolor moderado"
            "5" -> "${consultation.consultation.dolor} Dolor moderado"
            "6" -> "${consultation.consultation.dolor} Dolor fuerte"
            "7" -> "${consultation.consultation.dolor} Dolor fuerte"
            "8" -> "${consultation.consultation.dolor} Dolor muy fuerte"
            "9" -> "${consultation.consultation.dolor} Dolor muy fuerte"
            "10" -> "${consultation.consultation.dolor} Dolor insoportable"
            else -> "0 Sin dolor"
        }

    val pesoKg = consultation.exploracionFisica!!.pesoKg.toString()
    val pesoG = consultation.exploracionFisica!!.pesoG.toString()
    val estaturaM = consultation.exploracionFisica!!.estaturaM.toString()
    val estaturaCm = consultation.exploracionFisica!!.estaturaCm.toString()
}