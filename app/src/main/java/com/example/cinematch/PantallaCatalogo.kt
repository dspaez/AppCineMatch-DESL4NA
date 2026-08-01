package com.example.cinematch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogo(navController: NavController, viewModel: PeliculaViewModel) {

    // Observamos la lista de películas desde el ViewModel y lo actualiza automáticamente cuando cambie
    val listaPelicula by viewModel.peliculas.collectAsState()

    // Obtenemos el contexto actual para poder acceder a los ajustes del usuario
    val context = LocalContext.current

    // Creamos una instancia de AjustesUsuario para acceder al nombre del usuario
    val ajustesUsuario = remember { AjustesUsuario(context) }
    // Observamos el nombre del usuario desde DataStore y lo actualiza automáticamente cuando cambie
    val nombreUsuario by ajustesUsuario.nombreUsuarioFlow.collectAsState(initial = "Cargando")


    // SCAFFOLD nos da una estructura profesional con Barra Superior
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Hola, $nombreUsuario", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),

                // Agregamos un botón de ajustes en la barra superior
                actions = {

                    IconButton(onClick = { navController.navigate("cartelera") }) {
                        Icon(Icons.Default.Movie, contentDescription = "Cartelera")
                    }

                    IconButton(onClick = { navController.navigate("ajustes") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Al hacer clic, insertamos una película de prueba en la BD
                    val nuevaPelicula = Pelicula(
                        titulo = "Spider-Man",
                        director = "Sam Raimi",
                        anio = 2002,
                        sinopsis = "Un estudiante es mordido por una araña radiactiva.",
                        calificacion = 8.0,
                        urlImagen = "https://image.tmdb.org/t/p/w500/r3pPehX4ik8NLYPpbEQ7UcG1PtO.jpg"
                    )
                    viewModel.insertarPelicula(nuevaPelicula)
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Película")
            }
        }
    ) { paddingValues -> // Padding automático para no tapar la barra

        if (listaPelicula.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay películas. Toca el botón '+' para agregar.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) } // Espacio inicial
                items(listaPelicula) { pelicula ->
                    ItemPelicula(pelicula = pelicula, navController = navController)
                }
            }
        }
    }
}

@Composable
fun ItemPelicula(pelicula: Pelicula, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { navController.navigate("detalles/${pelicula.id}") },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = pelicula.urlImagen,
                contentDescription = "Póster de ${pelicula.titulo}",
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)),
                contentScale = ContentScale.Crop,
                // Agregamos un log de error aquí también para depurar el catálogo
                onError = { state ->
                    android.util.Log.e("CineMatch", "Error cargando catálogo: ${state.result.throwable.message}")
                }
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = pelicula.titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = pelicula.director, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "⭐ ${pelicula.calificacion}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
