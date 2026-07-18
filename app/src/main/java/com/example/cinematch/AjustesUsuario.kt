package com.example.cinematch

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. Creamos la instancia de DataStore (Se crea una sola vez para toda la app)
// "ajustes_cinematch" será el nombre del archivo oculto en el celular.
val Context.dataStore by preferencesDataStore(name = "ajustes_cinematch")

class AjustesUsuario(private val context: Context) {

    // 2. Definimos nuestras "Llaves" (Keys)
    companion object {
        val NOMBRE_USUARIO = stringPreferencesKey("nombre_usuario")
    }

    // 3. Función para GUARDAR (Es suspendida porque escribe en la memoria física)
    suspend fun guardarNombre(nombre: String) {
        context.dataStore.edit { preferencias ->
            preferencias[NOMBRE_USUARIO] = nombre
        }
    }

    // 4. Función para LEER (Devuelve un Flow, así la UI se actualiza sola si el nombre cambia)
    val nombreUsuarioFlow: Flow<String> = context.dataStore.data.map { preferencias ->
        // Si no hay nombre guardado, devolvemos "Invitado" por defecto
        preferencias[NOMBRE_USUARIO] ?: "Invitado"
    }
}
