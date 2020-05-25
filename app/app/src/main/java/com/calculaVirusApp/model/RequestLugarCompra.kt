package com.calculaVirusApp.model

data class RequestLugarCompra (
    val count: Int,
    val next: Any,
    val results: List<LugarCompra>,
    val previous: Any
)