package com.example.kuafrapp.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kuafrapp.model.Barber

@Database(entities = [Barber::class], version = 1)
abstract class BarberDatabase : RoomDatabase() {

    abstract fun barberDao(): BarberDAO

    companion object {

        @Volatile
        private var instance : BarberDatabase? = null
        private var lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            BarberDatabase::class.java,
            "BarberDatabase"
        ).build()

    }

}
