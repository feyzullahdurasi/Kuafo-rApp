package com.example.kuafrapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Barber(
    @ColumnInfo(name = "isim")
    @SerializedName("isim")
    val barberName: String?,
    @ColumnInfo(name = "kalori")
    @SerializedName("kalori")
    val localeName : String?,
    @ColumnInfo(name = "karbonhidrat")
    @SerializedName("karbonhidrat")
    val besinKarbonhidrat : String?,
    @ColumnInfo(name = "protein")
    @SerializedName("protein")
    val rating: String?,
    @ColumnInfo(name = "yag")
    @SerializedName("yag")
    val comment : String?,
    @ColumnInfo(name = "gorsel")
    @SerializedName("gorsel")
    val barberImage: String?

) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}


data class Comment(
    val username: String,
    val commentText: String
)
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