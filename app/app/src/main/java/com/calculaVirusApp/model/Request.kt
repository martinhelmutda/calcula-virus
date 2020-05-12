package com.calculaVirusApp.model

data class Request(
    val count: Int,
    val next: Any,
    val results: List<Place>,
    val previous: Any
)