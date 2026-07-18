package com.example.cinematch

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

//Dao es un patron de diseño (Data Access Object)
//Centralizar las operaciones de la base de datos, para que el resto de la aplicacion no tenga que preocuparse por los detalles de la base de datos.

@Dao
interface PeliculaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPelicula(pelicula: Pelicula)

    @Query("SELECT * FROM peliculas")
    fun obtenerTodasLasPeliculas(): Flow<List<Pelicula>>


    @Query("SELECT * FROM peliculas WHERE id = :idBuscado")
    suspend fun  obtenerPeliculaPorId(idBuscado: Int): Pelicula?

    @Query("DELETE FROM peliculas") //Solo lo usamos para pruebas y academicamente, no en produccion
    suspend fun borrarTodas()

}