package com.example.kuafrapp.roomdb

import com.example.kuafrapp.model.Barber
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface BarberDAO {
    // Metotlar burada
    @Insert
    suspend fun insertAll(barber: Array<Barber>): List<Long>

    @Query("DELETE FROM barber")
    suspend fun deleteAllBarber()

    @Query("SELECT * FROM barber")
    suspend fun getAllBarber(): List<Barber>

    @Query("SELECT * FROM barber WHERE uuid = :barberId")
    suspend fun getBarber(barberId: Int): Barber?
}
