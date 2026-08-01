package com.example.cinematch

import com.google.gson.annotations.SerializedName

// 1. La respuesta principal: El servidor nos manda una lista dentro de la variable "results"
data class RespuestaTmdb(
    @SerializedName("results")
    val resultados: List<PeliculaRed>
)

// 2. El modelo de CADA película que viene de internet
data class PeliculaRed(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val titulo: String,
    @SerializedName("overview") val sinopsis: String,
    // La API de TMDB solo manda la parte final de la ruta de la imagen (ej: "/8cdWjv.jpg")
    @SerializedName("poster_path") val imagenRuta: String?,
    @SerializedName("vote_average") val calificacion: Double
)
