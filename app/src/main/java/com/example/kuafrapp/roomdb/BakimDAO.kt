package com.example.kuafrapp.roomdb

import com.example.kuafrapp.model.Bakim
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Transaction

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

    @Query("DELETE FROM Bakim")
    suspend fun deleteAllBakim()

    @Query("SELECT * FROM Bakim")
    suspend fun getAllBakim(): List<Bakim>

    @Query("SELECT * FROM Bakim WHERE uuid = :BakimId")
    suspend fun getBakim(BakimId: Int): Bakim?

    @Query("SELECT * FROM Bakim WHERE name LIKE '%' || :searchQuery || '%'")
    suspend fun searchBakims(searchQuery: String): List<Bakim>

    @Query("""
        SELECT b.* FROM Bakim b
        INNER JOIN Service s ON s.BakimId = b.uuid
        WHERE s.serviceType = :serviceType
    """)
    suspend fun getBakimsByService(serviceType: String): List<Bakim>

    @Query("""
        SELECT b.* FROM Bakim b
        WHERE b.rating >= :minRating
        ORDER BY b.rating DESC
    """)
    suspend fun getBakimsByRating(minRating: Float): List<Bakim>

    @Transaction
    @Query("SELECT * FROM Bakim WHERE uuid = :BakimId")
    suspend fun getBakimWithServices(BakimId: Int): BakimWithServices

    @Query("""
        SELECT * FROM Bakim 
        WHERE location LIKE '%' || :location || '%'
        ORDER BY rating DESC
    """)
    suspend fun getBakimsByLocation(location: String): List<Bakim>
}

data class BakimWithServices(
    @Embedded val Bakim: Bakim,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "BakimId"
    )
    val services: List<Service>
)
