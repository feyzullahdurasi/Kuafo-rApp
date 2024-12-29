package com.example.kuafrapp.roomdb

import android.content.Context
import androidx.room.*
import com.example.kuafrapp.model.*
import java.util.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Database(
    entities = [
        Business::class,
        Service::class,
        ServiceFeature::class,
        Reservation::class,
        BusinessService::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class BarberDatabase : RoomDatabase() {
    abstract fun businessDao(): BusinessDao
    abstract fun serviceDao(): ServiceDao
    abstract fun reservationDao(): ReservationDao

    companion object {
        @Volatile
        private var instance: BarberDatabase? = null

        fun getDatabase(context: Context): BarberDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BarberDatabase::class.java,
                    "barber_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromServiceFeatureList(value: List<ServiceFeature>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toServiceFeatureList(value: String): List<ServiceFeature> {
        val type = object : TypeToken<List<ServiceFeature>>() {}.type
        return Gson().fromJson(value, type)
    }

    // Add other converters as needed for your custom types
}

// Dao interfaceleri ekleyelim
@Dao
interface BusinessDao {
    @Query("SELECT * FROM businesses")
    suspend fun getAllBusinesses(): List<Business>

    @Query("SELECT * FROM businesses WHERE id = :id")
    suspend fun getBusinessById(id: Int): Business?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusiness(business: Business)

    @Delete
    suspend fun deleteBusiness(business: Business)
}

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services")
    suspend fun getAllServices(): List<Service>

    @Query("SELECT * FROM services s INNER JOIN businesses_services bs ON s.id = bs.service_id WHERE bs.business_id = :businessId")
    suspend fun getServicesForBusiness(businessId: Int): List<Service>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: Service)
}

@Dao
interface ReservationDao {
    @Query("SELECT * FROM reservations WHERE userId = :userId")
    suspend fun getUserReservations(userId: Int): List<Reservation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: Reservation)

    @Query("DELETE FROM reservations WHERE id = :id")
    suspend fun cancelReservation(id: String)
}

@Entity(
    tableName = "businesses_services",
    primaryKeys = ["business_id", "service_id"],
    foreignKeys = [
        ForeignKey(
            entity = Business::class,
            parentColumns = ["id"],
            childColumns = ["business_id"]
        ),
        ForeignKey(
            entity = Service::class,
            parentColumns = ["id"],
            childColumns = ["service_id"]
        )
    ]
)
data class BusinessService(
    @ColumnInfo(name = "business_id") val businessId: Int,
    @ColumnInfo(name = "service_id") val serviceId: Int
)
