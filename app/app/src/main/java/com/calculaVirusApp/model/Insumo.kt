package com.calculaVirusApp.model

import java.time.DateTimeException
import java.util.*

data class Insumo (
    var id:Int,
    var nombre: String,
    var descripcion: String,
    var lugar_compra: String,
    var categoria: String,
    var caducidad: Date,
    var prioridad: Int,
    var duracion_promedio: Int,
    var cantidad: String,
    var marca:String
)