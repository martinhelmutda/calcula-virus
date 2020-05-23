package com.calculaVirusApp.model

data class RequestChecklistInsumo(
    val count: Int,
    val next: Any,
    val results: List<ChecklistInsumo>,
    val previous: Any
)