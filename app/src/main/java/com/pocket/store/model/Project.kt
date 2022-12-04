package com.pocket.store.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Project  (
    var id: String,
    val nombre: String,
    val nombreCliente: String?,
    val ruta_imagen: String?,
    val latitud: Double?,
    val longitud: Double?,
    val altura: Double?,
    val ruta_audio: String?,

) : Parcelable {
    constructor():
    this(
        "",
        "",
        "",
        "",
        0.0,
        0.0,
        0.0,
        ""
    )
}