package com.example.cinematch

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

