package com.example.cinematch

import kotlinx.coroutines.flow.Flow

class PeliculaRepository (
    private val peliculaDao: PeliculaDao,
    private val tmdbApi: TmdbApi) {

    //1. OPERACIONES LOCALES (ROOM)

    val peliculasLocales: Flow<List<Pelicula>> = peliculaDao.obtenerTodasLasPeliculas()

    suspend fun insertarPeliculaLocal(pelicula: Pelicula) {
        peliculaDao.insertarPelicula(pelicula)
    }

    suspend fun obtenerPeliculaLocalPorId(id: Int): Pelicula? {
        return peliculaDao.obtenerPeliculaPorId(id)
    }


    //2. OPERACIONES REMOTAS (TMDB)

    suspend fun obtenerCarleteraPopular(apiKey: String): List<PeliculaRed> {
        return try {
            val respuesta = tmdbApi.obtenerPeliculasPopulares(apiKey)
            respuesta.resultados
        } catch (e: Exception) {
            android.util.Log.e("CineMatch", "Error al descargar cartelera: ${e.message}")
            emptyList() // Retornar una lista vacía en caso de error
        }

    }
}