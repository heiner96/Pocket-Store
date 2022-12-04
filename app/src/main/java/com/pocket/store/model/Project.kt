package com.pocket.store.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Project  (
    var id: String,
    val nombre: String,
    val nombreCliente: String?,
    val ruta_imagen: String?,

) : Parcelable {
    constructor():
    this(
        "",
        "",
        "",
        ""
    )
}