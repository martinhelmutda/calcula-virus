package com.calculaVirusApp.model

data class RequestInsumo (
    val count: Int,
    val next: Any,
    val results: List<Insumo>,
    val previous: Any
)