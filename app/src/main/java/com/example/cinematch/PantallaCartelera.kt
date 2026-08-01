package com.example.cinematch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCartelera(navController: NavController, viewModel: PeliculaViewModel) {

    // 1. Observamos el estado reactivo que viene de internet
    val peliculasInternet by viewModel.pelicuasInternet.collectAsState()

    // 2. Disparamos la descarga solo la primera vez que se carga la pantalla
    LaunchedEffect(Unit) {
        viewModel.descargarCarteleraPopular()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cartelera Mundial (TMDB)", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            )
        }
    ) { paddingValues ->

        // 3. Manejo de estados de la UI (Cargando vs Lista llena)
        if (peliculasInternet.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() // Ruedita de carga mientras baja de internet
            }
        } else {
            // Usamos un LazyVerticalGrid para mostrar 2 columnas (tipo Netflix)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(peliculasInternet) { peliculaTmdb ->
                    ItemCartelera(pelicula = peliculaTmdb)
                }
            }
        }
    }
}

// Componente visual para CADA película de la cartelera
@Composable
fun ItemCartelera(pelicula: PeliculaRed) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f), // Mantiene la proporción clásica de un póster de cine
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Construimos la URL completa para el póster de TheMovieDB
            val posterUrl = "https://image.tmdb.org/t/p/w500${pelicula.imagenRuta}"

            AsyncImage(
                model = posterUrl,
                contentDescription = pelicula.titulo,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Convertimos la calificación a Double de forma segura, la multiplicamos por 10, redondeamos y dividimos
            // Esto convierte un 7.7333 en 7.7 de forma ultra segura sin importar si viene como String o Double.
            val calificacionSegura = (pelicula.calificacion.toString().toDoubleOrNull() ?: 0.0)
            val calificacionRedondeada = (calificacionSegura * 10.0).roundToInt() / 10.0

            // Etiqueta de calificación en la esquina
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                shape = RoundedCornerShape(bottomEnd = 8.dp),
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text(
                    text = "⭐ $calificacionRedondeada",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
