package com.calculaVirusApp.model

data class LugarCompra(
    var id: Int,
    var nombre: String,
    var descripcion:String
) {
    override fun toString(): String {
        return nombre
    }
}