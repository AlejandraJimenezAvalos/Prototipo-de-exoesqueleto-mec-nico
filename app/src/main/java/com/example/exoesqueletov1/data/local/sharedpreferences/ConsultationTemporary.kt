package com.example.exoesqueletov1.data.local.sharedpreferences

class ConsultationTemporary {
    var id = ""
    var idPatient = ""
    var motivo = ""
    var sintomatologia = ""
    var pesoKg = ""
    var pesoG = ""
    var talla = ""
    var estaturaM = ""
    var estaturaCm = ""
    var libre = false
    var claudicante = false
    var conAyuda = false
    var espasticas = false
    var ataxica = false
    var observaciones = ""
    var reflejos = ""
    var sensibilidad = ""
    var lenguaje = ""
    var otros = ""
    var dolor = ""
    var musculoSuperiorIzquierdo = ""
    var musculoSuperiorDerecho = ""
    var musculoInferiorIzquierdo = ""
    var musculoInferiorDerecho = ""
    var troncoIzquierdo = ""
    var troncoDerecho = ""
    var cuelloIzquierdo = ""
    var cuelloDerecho = ""
    var valoracionInicial = ""
    var subjetivo = ""
    var analisis = ""
    var planAccion = ""
    var inicioMarcha = false
    var pieDerechoNoSobrepasa = false
    var pieDerechoNoLevanta = false
    var pieIzquierdoNoSobrepasa = false
    var pieIzquierdoNoLevanta = false
    var longitud = false
    var continuidad = false
    var trayectoriaDesviacionAlta = false
    var trayectoriaDesviacionMedia = false
    var trayectoriaDesviacionNula = false
    var noBalanceoAlto = false
    var noBalanceoMedio = false
    var noBalanceoNulo = false
    var talones = false
    var listOfGrados = mutableListOf<GradosObservaciones>()
    var pruebasEquilibrioA = 0
    var pruebasEquilibrioB = 0
    var pruebasEquilibrioC = 0
    var segundosMenor10 = false
    var segundos = ""
    var objetivos = ""
    var hipotesis = ""
    var estructuraCorporal = ""
    var funcionCorporal = ""
    var actividad = ""
    var participacion = ""
    var diagnostico = ""
    var plan = ""

    fun getDolorInt() = if (dolor.isEmpty()) 11 else dolor.toInt()
    fun getMusculoSuperiorIzquierdoInt() =
        if (musculoSuperiorIzquierdo.isEmpty()) 11 else musculoSuperiorIzquierdo.toInt()

    fun getMusculoSuperiorDerechoInt() =
        if (musculoSuperiorDerecho.isEmpty()) 11 else musculoSuperiorDerecho.toInt()

    fun getMusculoInferiorIzquierdoInt() =
        if (musculoInferiorIzquierdo.isEmpty()) 11 else musculoInferiorIzquierdo.toInt()

    fun getMusculoInferiorDerechoInt() =
        if (musculoInferiorDerecho.isEmpty()) 11 else musculoInferiorDerecho.toInt()

    fun getTroncoIzquierdoInt() = if (troncoIzquierdo.isEmpty()) 11 else troncoIzquierdo.toInt()
    fun getTroncoDerechoInt() = if (troncoDerecho.isEmpty()) 11 else troncoDerecho.toInt()
    fun getCuelloIzquierdoInt() = if (cuelloIzquierdo.isEmpty()) 11 else cuelloIzquierdo.toInt()
    fun getCuelloDerechoInt() = if (cuelloDerecho.isEmpty()) 11 else cuelloDerecho.toInt()


}