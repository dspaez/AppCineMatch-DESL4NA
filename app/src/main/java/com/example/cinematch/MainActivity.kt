package com.example.cinematch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cinematch.ui.theme.CineMatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineMatchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CineMatchApp()
                }
            }
        }
    }
}

@Composable
fun CineMatchApp() {
    // 1. Creamos el motor de navegación
    val navController = rememberNavController()

    // 2. Instanciamos nuestro "Cerebro". Este ViewModel se compartirá entre pantallas.
    val peliculaViewModel: PeliculaViewModel = viewModel()

    // 3. Definimos el mapa y decimos que inicie en "catalogo"
    NavHost(navController = navController, startDestination = "catalogo") {

        // --- RUTA 1: El Catálogo ---
        composable("catalogo") {
            PantallaCatalogo(navController = navController, viewModel = peliculaViewModel)
        }

        // --- RUTA 2: Los Detalles (Espera un parámetro llamado {id}) ---
        composable("detalles/{id}") { backStackEntry ->
            // Recuperamos el parámetro de la ruta (siempre llega como texto/String)
            val idString = backStackEntry.arguments?.getString("id")

            // Lo convertimos a número entero (Int). Si falla, ponemos 0 por defecto.
            val idInt = idString?.toIntOrNull() ?: 0

            // Llamamos a la pantalla pasándole el ID convertido
            PantallaDetalles(
                navController = navController,
                viewModel = peliculaViewModel,
                peliculaId = idInt
            )
        }
    }
}