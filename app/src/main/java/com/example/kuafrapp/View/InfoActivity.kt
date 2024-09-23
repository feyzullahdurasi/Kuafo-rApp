package com.example.kuafrapp.View

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kuafrapp.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.manHairdresserImageButton.setOnClickListener { navigateToMainActivity() }
        binding.womenHairdresserImageButton.setOnClickListener { navigateToMainActivity() }
        binding.petGroomerImageButton.setOnClickListener { navigateToMainActivity() }
    }

    private fun navigateToMainActivity() {
        // MainActivity'e geçiş yapmak için intent kullanıyoruz
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
