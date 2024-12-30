package com.example.kuafrapp.roomdb

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.kuafrapp.model.Business
import com.example.kuafrapp.model.Reservation
import com.example.kuafrapp.model.ReservationStatus
import com.example.kuafrapp.model.Service
import com.example.kuafrapp.model.ServiceFeature
import com.example.kuafrapp.model.ServiceType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

@TypeConverters(Converters::class)
@Database(
    entities = [
        Business::class,
        Service::class,
        ServiceFeature::class,
        Reservation::class,
        BusinessService::class
    ],
    version = 3
)
abstract class BakimDatabase : RoomDatabase() {
    abstract fun businessDao(): BusinessDao
    abstract fun serviceDao(): ServiceDao
    abstract fun reservationDao(): ReservationDao

    companion object {
        @Volatile
        private var instance: BakimDatabase? = null

        fun getDatabase(context: Context): BakimDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BakimDatabase::class.java,
                    "bakim_database"
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
    fun fromServiceFeatureList(value: List<ServiceFeature>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toServiceFeatureList(value: String): List<ServiceFeature> {
        val type = object : TypeToken<List<ServiceFeature>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter 
    fun fromBusiness(business: Business): String {
        return Gson().toJson(business)
    }

    @TypeConverter
    fun toBusiness(value: String): Business {
        return Gson().fromJson(value, Business::class.java)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUUID(value: String?): UUID? {
        return value?.let { UUID.fromString(it) }
    }

    @TypeConverter
    fun fromServiceType(type: ServiceType?): String? {
        return type?.description
    }

    @TypeConverter
    fun toServiceType(description: String?): ServiceType? {
        return ServiceType.values().find { it.description == description }
    }

    @TypeConverter
    fun fromReservationStatus(status: ReservationStatus?): String? {
        return status?.name
    }

    @TypeConverter
    fun toReservationStatus(name: String?): ReservationStatus? {
        return name?.let { ReservationStatus.valueOf(it) }
    }

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
    @Query("SELECT s.* FROM services s INNER JOIN businesses_services bs ON s.id = bs.service_id WHERE bs.business_id = :businessId")
    @RewriteQueriesToDropUnusedColumns
    suspend fun getServicesForBusiness(businessId: Int): List<Service>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: Service)
}

@Dao
interface ReservationDao {
    @Query("SELECT * FROM reservations WHERE user_id = :userId")
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
