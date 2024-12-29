package com.example.kuafrapp.model

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

data class Business(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("address")
    val address: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("hours")
    val hours: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("location")
    val location: Location
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

data class Service(
    val id: Int,
    val serviceType: String,
    val serviceFeature: List<ServiceFeature>
)

data class ServiceFeature(
    val id: Int,
    val name: String,
    val price: Double,
    val duration: Int
)

data class Reservation(
    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerializedName("date")
    val date: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("status")
    val status: ReservationStatus,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("businessId")
    val businessId: Int,
    @SerializedName("serviceFeatureId")
    val serviceFeatureId: Int
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
