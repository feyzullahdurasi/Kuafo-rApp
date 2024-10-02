package com.example.kuafrapp

import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RateBarberActivity : AppCompatActivity() {
    private lateinit var ratingBar: RatingBar
    private lateinit var submitRatingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_barber)

        ratingBar = findViewById(R.id.ratingBar)
        submitRatingButton = findViewById(R.id.submitRatingButton)

        submitRatingButton.setOnClickListener {
            val rating = ratingBar.rating
            // Burada değerlendirmeyi veri tabanına kaydedebilir veya başka bir işlem yapabilirsiniz
            Toast.makeText(this, "Değerlendirmeniz: $rating yıldız", Toast.LENGTH_SHORT).show()
            finish() // Activity'yi kapat
        }
    }
}
