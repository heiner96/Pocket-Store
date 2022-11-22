package com.pocket.store.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Objeto (
    var id: String,
    val nombre: String,
    val correo: String?,
    val web: String?,
    val telefono: String?,
    val latitud: Double?,
    val longitud: Double?,
    val altura: Double?,
    val precio: Double?,
    val ruta_audio: String?,
    val ruta_imagen: String?,
) : Parcelable {
    constructor():
            this(
                "",
                "",
                "",
                "",
                "",
                0.0,
                0.0,
                0.0,
                0.0,
                "",
                ""
            )
}
