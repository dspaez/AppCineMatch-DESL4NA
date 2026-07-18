package com.example.cinematch

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "peliculas")
data class Pelicula(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val titulo: String,
    val director: String,
    val anio: Int,
    val sinopsis: String,
    val calificacion: Double,
    val urlImagen: String // Agregamos esto para usarlo más adelante con fotos reales
)