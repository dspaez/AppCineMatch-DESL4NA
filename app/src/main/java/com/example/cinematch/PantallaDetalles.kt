package com.example.cinematch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun PantallaDetalles(navController: NavController, viewModel: PeliculaViewModel, peliculaId: Int) {
    val pelicula = viewModel.obtenerPeliculaPorId(peliculaId)

    if (pelicula != null) {
        Column(modifier = Modifier.fillMaxSize()) {

            // EL POSTER CON EFECTO DE DEGRADADO (GRADIENT)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                // 1. La imagen al fondo con LOG de Errores (Log.e)
                AsyncImage(
                    model = pelicula.urlImagen,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onError = { state ->
                        android.util.Log.e("CineMatch", "Error de Coil: ${state.result.throwable.message}")
                    }
                )

                // 2. El degradado superpuesto
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background // Se funde con el fondo de la app
                                ),
                                startY = 300f // El degradado empieza en la mitad inferior
                            )
                        )
                )
            }

            // EL CONTENIDO TEXTUAL
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    // Hacemos que la columna se desplace hacia arriba sobre la imagen
                    .offset(y = (-40).dp)
            ) {
                Text(text = pelicula.titulo, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
                Text(
                    text = "${pelicula.año} • Dirigida por ${pelicula.director}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Sinopsis", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = pelicula.sinopsis, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp)

                Spacer(modifier = Modifier.weight(1f)) // Empuja el botón al final

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Volver al Catálogo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
