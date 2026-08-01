package com.example.cinematch

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 1. EL MENÚ: Definimos qué queremos pedirle al servidor
interface TmdbApi {
    @GET("movie/popular")
    suspend fun obtenerPeliculasPopulares(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES" // Queremos las sinopsis en español
    ): RespuestaTmdb
}

// 2. EL MOTOR: Configuramos Retrofit para toda la app
object RetrofitClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    val api: TmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Le enseñamos a leer JSON
            .build()
            .create(TmdbApi::class.java)
    }
}
