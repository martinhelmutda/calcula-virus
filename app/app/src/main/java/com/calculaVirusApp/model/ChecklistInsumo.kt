package com.calculaVirusApp.model

data class ChecklistInsumo (
    var id: Int,
    var insumo_nombre:String,
    var insumo_id: Int,
    var checklist_id: Int,
    var cantidad: Int,
    var comprado: Boolean
)