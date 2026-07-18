package com.example.cinematch

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pelicula::class], version = 1, exportSchema = false)
abstract class PeliculaDatabase: RoomDatabase() {

    abstract fun peliculaDao(): PeliculaDao

    companion object {
        @Volatile
        private var INSTANCE: PeliculaDatabase? = null

        fun getDatabase(context: Context): PeliculaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PeliculaDatabase::class.java,
                    "cinematch_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}