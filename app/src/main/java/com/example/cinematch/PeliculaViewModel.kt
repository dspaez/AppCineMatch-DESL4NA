package com.example.cinematch

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class PeliculaViewModel : ViewModel() {

    // 1. Lista privada: mutableStateListOf avisa a Compose cuando hay cambios
    private val _peliculas = mutableStateListOf(
        Pelicula(1, "Inception", "Christopher Nolan", 2010, "Un ladrón que roba secretos corporativos a través del uso de la tecnología de compartir sueños.", 8.8, "https://www.themoviedb.org/t/p/w600_and_h900_face/xlaY2zyzMfkhk0HSC5VUwzoZPU1.jpg"),
        Pelicula(2, "Interstellar", "Christopher Nolan", 2014, "Un equipo de exploradores viaja a través de un agujero de gusano en el espacio en un intento por asegurar la supervivencia de la humanidad.", 8.6, "https://www.themoviedb.org/t/p/w600_and_h900_face/iawqQdFKI7yTUoSkDNP8gyV3J3r.jpg"),
        Pelicula(3, "The Matrix", "Wachowskis", 1999, "Un hacker informático descubre la verdadera y chocante naturaleza de su realidad y su papel en la guerra contra sus controladores.", 8.7, "https://www.themoviedb.org/t/p/w600_and_h900_face/aOIuZAjPaRIE6CMzbazvcHuHXDc.jpg"),
        Pelicula(4, "El Padrino", "Francis Ford Coppola", 1972, "El envejecido patriarca de una dinastía del crimen organizado transfiere el control de su imperio clandestino a su reacio hijo.", 9.2, "https://www.themoviedb.org/t/p/w600_and_h900_face/3bhkrj58Vtu7enYsRolD1fZdja1.jpg")
    )





    // 2. Lista pública (solo lectura) expuesta a la Interfaz Gráfica
    val peliculas: List<Pelicula> = _peliculas

    // 3. Función auxiliar para buscar una película cuando vayamos a los Detalles
    fun obtenerPeliculaPorId(id: Int): Pelicula? {
        // 'find' busca en la lista el primer elemento que cumpla la condición
        return _peliculas.find { it.id == id }
    }
}
