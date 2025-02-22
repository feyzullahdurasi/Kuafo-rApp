package com.example.bakim.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Relation
import androidx.room.Update
import com.example.bakim.model.Bakim
import com.example.bakim.model.Service

@Dao
interface BakimDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bakims: List<Bakim>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bakim: Bakim): Long

    @Update
    suspend fun update(bakim: Bakim)

    @Delete
    suspend fun delete(bakim: Bakim)
}

data class BakimWithServices(
    @Embedded val Bakim: Bakim,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "BakimId"
    )
    val services: List<Service>
)