package com.calculaVirusApp.model

data class RequestChecklist(
    val count: Int,
    val next: Any,
    val results: List<Checklist>,
    val previous: Any
)