package com.example.kuafrapp.model

import java.util.Date
import java.util.UUID

data class UserComment(
    val id: UUID = UUID.randomUUID(),
    val username: String,
    val rating: Int,
    val commentText: String? = null
)

data class Location(
    val id: UUID = UUID.randomUUID(),
    val latitude: Double?,
    val longitude: Double?,
    val address: String
)

data class User(
    val id: Int,
    val username: String,
    val password: String,
    val email: String,
    val bankcard: BankCard,
    val reservations: List<Reservation>
)

data class BankCard(
    val id: Int,
    val cardHolderName: String,
    val cardNumber: String,
    val cardExpirationDate: CardExpirationDate,
    val cardCVC: String
)

data class CardExpirationDate(
    val cardExpirationMonth: Int,
    val cardExpirationYear: Int
)

data class DailyRevenueItem(
    val id: UUID = UUID.randomUUID(),
    val date: Date,
    val revenue: Double
)

data class Business(
    val user: List<User>,
    val location: List<Location>,
    val comments: List<UserComment>,
    val businessName: String,
    val businessImage: String,
    val businessAddress: String,
    val businessPhone: String,
    val businessHours: String,
    val businessPrice: String,
    val services: List<Service>
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
    val id: UUID = UUID.randomUUID(),
    val service: Service,
    val business: Business,
    val date: Date,
    val time: String,
    val status: ReservationStatus
)

enum class ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELED
}

sealed class UserRole {
    data class UserRoleUser(val user: User) : UserRole()
    data class UserRoleBusiness(val business: Business) : UserRole()
}

data class Bakim(
    val user: UserRole
)

// Mock Data
object MockData {
    val sampleUsers = listOf(
        User(
            id = 101,
            username = "john_doe",
            password = "securepassword",
            email = "john.doe@example.com",
            bankcard = BankCard(
                id = 202,
                cardHolderName = "John Doe",
                cardNumber = "1234567812345678",
                cardExpirationDate = CardExpirationDate(12, 2025),
                cardCVC = "123"
            ),
            reservations = emptyList()
        )
    )

    val sampleLocation = Location(
        latitude = 37.7749, // San Francisco latitude
        longitude = -122.4194, // San Francisco longitude
        address = "San Francisco, CA"
    )

    val sampleComments = listOf(
        UserComment(username = "sarah_smith", rating = 5, commentText = "Great service!"),
        UserComment(username = "mike_jones", rating = 4, commentText = "Friendly staff and good quality.")
    )

    val sampleServices = listOf(
        Service(
            id = 1, serviceType = "mens_hairdresser",
            serviceFeature = listOf(
                ServiceFeature(id = 101, name = "Haircut", price = 250.0, duration = 60),
                ServiceFeature(id = 102, name = "Beard Trim", price = 150.0, duration = 20)
            )
        ),
        Service(
            id = 4, serviceType = "car_wash",
            serviceFeature = listOf(
                ServiceFeature(id = 401, name = "Exterior Wash", price = 100.0, duration = 30),
                ServiceFeature(id = 402, name = "Interior Vacuum", price = 150.0, duration = 30)
            )
        )
    )

    val sampleBusiness = Business(
        user = sampleUsers,
        location = listOf(sampleLocation),
        comments = sampleComments,
        businessName = "City Salon & Car Wash",
        businessImage = "berber",
        businessAddress = "123 Main Street, Springfield",
        businessPhone = "123-456-7890",
        businessHours = "9 AM - 8 PM",
        businessPrice = "$$",
        services = sampleServices
    )

    val sampleReservations = listOf(
        Reservation(
            service = sampleServices[0],  // Men's Hairdresser service
            business = sampleBusiness,
            date = Date(),
            time = "14:00",
            status = ReservationStatus.CONFIRMED
        ),
        Reservation(
            service = sampleServices[1],  // Car Wash service
            business = sampleBusiness,
            date = Date(System.currentTimeMillis() + 86400000),  // One day later
            time = "16:00",
            status = ReservationStatus.PENDING
        )
    )

    val sampleData = Bakim(
        user = UserRole.UserRoleBusiness(sampleBusiness)
    )
}

/*
    val BarberName: String?,
    val BarberLocale: String?,
    val BarberImage: String?,
    val BarberPhone: String?,
    val BarberEmail: String?,
    val BarberPassword: String?,
    val BarberDescription: String?,
    val BarberService: String?,
    val BarberRating: String?,
    val BarberPrice: String?,
    val BarberLocation: String?,
    val BarberLatitude: String?,
    val BarberLongitude: String?,
    val BarberDistance: String?,
    val BarberStatus: String?,
    val BarberCreatedAt: String?,
    val BarberUpdatedAt: String?,
    val BarberDeletedAt: String?,
    val BarberType: String?,
    val BarberServiceType: String?,
    val BarberServiceDescription: String?,
    val BarberServicePrice: String?,
    val BarberServiceDuration: String?,
    val BarberServiceCreatedAt: String?,
    val BarberServiceUpdatedAt: String?,
    val BarberServiceDeletedAt: String?,
    val BarberServiceTypeDescription: String?,
    val BarberServiceTypeCreatedAt: String?,
    val BarberServiceTypeUpdatedAt: String?,
    val BarberServiceTypeDeletedAt: String?

 */