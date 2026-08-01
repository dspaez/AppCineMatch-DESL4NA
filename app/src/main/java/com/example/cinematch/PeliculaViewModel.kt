package com.example.cinematch

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PeliculaViewModel(private val dao: PeliculaDao): ViewModel() {

    //Transformar el Flow de Room en un stateFlow para que Compose pueda observarlo
// y redibujar la UI cuando cambie

    val peliculas: StateFlow<List<Pelicula>> = dao.obtenerTodasLasPeliculas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Mantener la suscripción mientras haya observadores, con un retraso de 5 segundos
            initialValue = emptyList()
        )

    //Función para insertar una película en la base de datos (Usamos corutinas para no bloquear el hilo principal)
    fun insertarPelicula(pelicula: Pelicula) {
        viewModelScope.launch {
            dao.insertarPelicula(pelicula)
        }
    }

    //Funcion para obtener una película por su ID (Usamos corutinas para no bloquear el hilo principal)
    suspend fun obtenerPeliculaPorId(id: Int): Pelicula? {
        return dao.obtenerPeliculaPorId(id)
    }

    //Lista de películas obtenidas de la API (no de la base de datos)
    private val _peliculasInternet = MutableStateFlow<List<PeliculaRed>>(emptyList())
    val pelicuasInternet: StateFlow<List<PeliculaRed>> = _peliculasInternet

    private val tmdbApiKey = "314b3545dc7f5ee49f8ec249415cc82e"

    fun descargarCarteleraPopular(){
        viewModelScope.launch {
            try {
                val respuesta = RetrofitClient.api.obtenerPeliculasPopulares(apiKey = tmdbApiKey)
                _peliculasInternet.value = respuesta.resultados

            }catch (e: Exception) {
                android.util.Log.e("CineMatch", "Error al descargar cartelera: ${e.message}")
            }
        }
    }

}


//La Fabrica se encarga de construir el ViewModel y pasarle el DAO como dependencia.
// Esto es útil para la inyección de dependencias y para mantener el código
// limpio y modular.
class PeliculaViewModelFactory(private val dao: PeliculaDao): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeliculaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PeliculaViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

