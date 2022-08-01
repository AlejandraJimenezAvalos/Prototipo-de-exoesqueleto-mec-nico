package com.example.exoesqueletov1.utils

import android.Manifest.permission.*
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.exoesqueletov1.BuildConfig

object Constants {

    enum class Status { Success, Failure, Canceled, Loading, NotExist }
    enum class Gender { Femenino, Masculino }
    enum class ActionUsers { Delete, LogIn }
    enum class TypeUser { Admin, Specialist, Patient }
    enum class SingInPagerNavigation { Login, SingIn }
    enum class StatusDevice { Emparejado, Cercano }
    enum class Origin { User, Create }
    enum class TypeData { Antecedents, Habits, Scar }
    enum class Type { Caminata, Repeticiones, Null }
    enum class Modo { Derecha, Izquierda, Ambos, Pasos, Minutos, Repeticiones, Null }
    enum class Finalize { New, Start, Finalize }

    const val ID_RUTINA = "idRutina"
    const val COLLECTION_PATIENT = "patient"
    const val COLLECTION_PROFILE = "profile"
    const val COLLECTION_USER = "user"
    const val COLLECTION_MESSAGES = "messages"
    const val COLLECTION_EXOSKELETON = "exoskeleton"
    const val COLLECTION_EXPEDIENT = "expedient"
    const val COLLECTION_CONSULTATION_DATA = "consultations"
    const val COLLECTION_EXPLORACION_FISICA = "exploracion_fisica"
    const val COLLECTION_EVALUACION_POSTURA = "evaluacion_postura"
    const val COLLECTION_DIAGNOSTICO = "diagnostico"
    const val COLLECTION_EVALUACION_MUSCULAR = "evaluacion_muscular"
    const val COLLECTION_EVALUACION_MUSCULO = "evaluacion_musculo"
    const val COLLECTION_MARCHA = "marcha"
    const val COLLECTION_ANALISIS = "analisis"
    const val COLLECTION_VALORACION_FUNCIONAL = "valoracion_funcional"
    const val COLLECTION_PLAN = "plan"
    const val COLECTION_RUTINA = "rutina"
    const val ID = "id"
    const val ADDRESS = "address"
    const val CELL = "cell"
    const val EMAIL = "email"
    const val NAME = "name"
    const val PHONE = "phone"
    const val SCHOOL = "school"
    const val USER = "user"
    const val DESCRIPTION = "description"
    const val MAC = "mac"
    const val COUNTRY = "country"
    const val DATE = "date"
    const val GENDER = "gender"
    const val LAST_NAME = "lastName"
    const val PASSWORD = "password"
    const val PHOTO_PATH = "photoPath"
    const val ID_USER = "idUser"
    const val USER_ID = "userId"
    const val FROM = "from"
    const val TO = "to"
    const val MESSAGE = "message"
    const val STATUS = "status"
    const val OCCUPATION = "occupation"
    const val ORIGIN = "origin"
    const val BIRTHDAY = "birthday"
    const val VALUE = "value"
    const val ID_PATIENT = "idPatient"
    const val TYPE = "type"
    const val LADA = "lada"
    const val ID_EXPLORACION = "idExploracion"
    const val ID_MARCHA = "idMarcha"
    const val ID_DIAGNOSTICO = "idDiagnostico"
    const val ID_EVALUACION_MUSCULAR = "idEvaluacionMuscular"
    const val ID_ANALISIS = "idAnalisis"
    const val ID_EVALUACION_POSTURA = "idEvaluacionPostura"
    const val ID_VALORACION_FUNCIONAL = "idValoracionFuncional"
    const val ID_PLAN = "idPlan"
    const val MOTIVO = "motivo"
    const val SINTOMATOLOGIA = "sintomatologia"
    const val DOLOR = "dolor"
    const val ID_CONSULT = "idConsult"
    const val PESO_KG = "pesoKg"
    const val PESO_G = "pesoG"
    const val ESTATURA_M = "estaturaM"
    const val ESTATURA_CM = "estaturaCm"
    const val TALLA = "talla"
    const val VISTA = "vista"
    const val GRADOS = "grados"
    const val OBSERVACIONES = "observaciones"
    const val REFLEJO = "reflejos"
    const val SENSIBIDAD = "sensibilidad"
    const val LENGUAJE = "lenguaje"
    const val OTROS = "otros"
    const val VALORACION = "valoracion"
    const val SUBJETIVO = "subjetivo"
    const val ANALISIS = "analisis"
    const val PLAN_ACCION = "planAccion"
    const val ZONA = "zona"
    const val VALOR = "valor"
    const val LIBRE = "libre"
    const val CLAUDANTE = "claudante"
    const val CON_AYUDA = "conAyuda"
    const val ESPATICAS = "espaticas"
    const val ATAXICA = "ataxica"
    const val RESPUESTA_A = "respuestaA"
    const val RESPUESTA_B = "respuestaB"
    const val RESPUESTA_C = "respuestaC"
    const val STATE_SEGUNDOS = "stateSegundos"
    const val SEGUNDOS = "segundos"
    const val OBJETIVOS = "objetivos"
    const val HIPOTESIS = "hipotesis"
    const val ESTRUCTURA = "estructura"
    const val FUNSION = "funsion"
    const val ACTIVIDAD = "actividad"
    const val PARTICIPACION = "participacion"
    const val DIAGNOSTICO = "diagnostico"
    const val PLAN = "plan"
    const val MODO = "modo"
    const val FINALIZE = "finalize"


    @RequiresApi(Build.VERSION_CODES.S)
    val PERMISSIONS = arrayOf(
        INTERNET,
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE,
        CALL_PHONE,
        ACCESS_COARSE_LOCATION,
        ACCESS_FINE_LOCATION,
        CAMERA,
        FOREGROUND_SERVICE,
        BLUETOOTH,
        BLUETOOTH_ADMIN,
        BLUETOOTH_SCAN,
        BLUETOOTH_ADVERTISE,
        BLUETOOTH_CONNECT,
    )

    val names = listOf(
        "Inclinación Lateral de la Cabeza",
        "Cabeza rotada",
        "Asimetría Maxilar",
        "Clavículas Asimétricas",
        "Hombro caído",
        "Hombro elevado",
        "Cubito Valgo",
        "Cubito Varo",
        "Rotación Interna de Cadera",
        "Rotación Externa de Cadera",
        "Genu Varum",
        "Genu Valgum",
        "Torsión Tibial Interna",
        "Torsión Tibial Externa",
        "Hallux Valgus",
        "Dedos en Garra",
        "Dedos en Martillo",
        "Desplazamiento Anterior del Cuerpo",
        "Desplazamiento posterior del Cuerpo",
        "Cabeza Adelantada",
        "Vertebras Torácicas: Cifosis",
        "Vertebras Torácicas: Pectus Excavatum",
        "Pecho Tonel",
        "Pectus Carinatum",
        "Columna: Lordosis",
        "Espalda Cifotica (Columna)",
        "Espalda Plana (Columna)",
        "Inclinación Ant. de Pelvis y Cadera",
        "Inclinación Post. de Pelvis y Cadera",
        "Genu Recurvatum",
        "Rodillas Flexionadas",
        "Cabeza Inclinada",
        "Cabeza Rotada",
        "Hombro Caído",
        "Hombro Elevado",
        "Espalda Plana",
        "Abducción de Escapulas",
        "Aducción de Escapulas",
        "Escapulas Aladas",
        "Curvatura Lateral de la Columna",
        "Rotación Interna de Cadera (Tronco)",
        "Rotación Externa de Cadera",
        "Inclinicación Lateral de la Pelvis",
        "Rotación Pélvica",
        "Cadera Abducida",
        "Pie Pronado",
        "Pie Supinado",
        "Pie Plano",
        "Pie Cavo",
    )

    // Bluetooth configs

    // values have to be globally unique
    const val INTENT_ACTION_DISCONNECT: String = BuildConfig.APPLICATION_ID + ".Disconnect"
    const val NOTIFICATION_CHANNEL: String = BuildConfig.APPLICATION_ID + ".Channel"
    const val INTENT_CLASS_MAIN_ACTIVITY: String = BuildConfig.APPLICATION_ID + ".MainActivity"

    // values have to be unique within each app
    const val NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001

    enum class StatusConnection { Connect, ConnectError, Read, IoError }
    enum class Connection { False, Pending, True }
    enum class StatusBluetoothDevice { Pending, Connected, Error, Read, Disconnected, ConnectionFailed }

}