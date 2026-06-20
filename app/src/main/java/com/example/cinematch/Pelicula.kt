package com.example.cinematch

data class Pelicula(
    val id: Int,
    val titulo: String,
    val director: String,
    val año: Int,
    val sinopsis: String,
    val calificacion: Double,
    val urlImagen: String // Agregamos esto para usarlo más adelante con fotos reales
)