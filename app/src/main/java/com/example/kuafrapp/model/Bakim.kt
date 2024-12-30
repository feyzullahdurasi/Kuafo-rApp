package com.example.kuafrapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.kuafrapp.roomdb.Converters
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class UserComment(
    val id: UUID = UUID.randomUUID(),
    val username: String,
    val rating: Int,
    val commentText: String? = null
)

data class Location(
    @SerializedName("id")
    val id: Int,
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null,
    @SerializedName("address")
    val address: String
)

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("email") 
    val email: String,
    @SerializedName("bankCards")
    val bankCards: List<BankCard> = emptyList(),
    @SerializedName("reservations")
    val reservations: List<Reservation> = emptyList()
)

data class BankCard(
    @SerializedName("id")
    val id: Int,
    @SerializedName("cardHolderName")
    val cardHolderName: String,
    @SerializedName("cardNumber")
    val cardNumber: String,
    @SerializedName("cardExpirationMonth")
    val cardExpirationMonth: Int,
    @SerializedName("cardExpirationYear")
    val cardExpirationYear: Int,
    @SerializedName("cardCVC")
    val cardCVC: String
)

data class DailyRevenueItem(
    val id: UUID = UUID.randomUUID(),
    val date: Date,
    val revenue: Double
)

@Entity(tableName = "businesses")
data class Business(
    @PrimaryKey 
    val id: Int,
    val name: String,
    val image: String?,
    val address: String,
    val phone: String,
    val hours: String,
    val price: String,
    val location: String,
    val services: String
)

enum class ServiceType(val description: String) {
    MEN_HAIRDRESSER("mens_hairdresser"),
    WOMEN_HAIRDRESSER("womens_hairdresser"),
    PET_CARE("pet_care"),
    CAR_WASH("car_wash"),
    SKIN_CARE("skin_care"),
    SPA_MASSAGE("spa_massage"),
    NAIL_CARE("nail_care"),
    HOME_CLEANER("home_cleaner"),
    EVENT_SPACES_RENTAL("event_spaces_rental");
}

@Entity(tableName = "services")
data class Service(
    @PrimaryKey
    val id: Int,
    val serviceType: String,
    @TypeConverters(Converters::class)
    val serviceFeature: List<ServiceFeature>,
    @TypeConverters(Converters::class)
    val business: Business
)

@Entity(tableName = "service_features")
data class ServiceFeature(
    @PrimaryKey 
    val id: Int,
    val name: String,
    val price: Double,
    val duration: Int
)

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey
    val id: String,
    val date: String,
    val time: String,
    val status: String,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "business_id")
    val businessId: Int,
    @ColumnInfo(name = "service_feature_id")
    val serviceFeatureId: Int
)

@Entity(
    tableName = "businesses_services",
    primaryKeys = ["business_id", "service_id"],
    indices = [Index("service_id")],
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

enum class ReservationStatus {
    @SerializedName("Pending")
    PENDING,
    @SerializedName("Confirmed")
    CONFIRMED,
    @SerializedName("Canceled")
    CANCELED
}

sealed class UserRole {
    data class UserRoleUser(val user: User) : UserRole()
    data class UserRoleBusiness(val business: Business) : UserRole()
}

data class Bakim(
    val user: UserRole
)

data class Review(
    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("businessId")
    val businessId: Int,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("comment")
    val comment: String? = null,
    @SerializedName("createdAt")
    val createdAt: Date = Date()
)

data class ReservationRequest(
    val serviceId: Int,
    val businessId: Int,
    val userId: Int,
    val date: String,
    val time: String,
    val selectedFeatures: List<ServiceFeature>
)
