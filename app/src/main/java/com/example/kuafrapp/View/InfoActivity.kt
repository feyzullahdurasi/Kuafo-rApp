package com.example.kuafrapp.View

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kuafrapp.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding kullanımı
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Butonlara tıklama olaylarını ekliyoruz ve her birine ilgili hizmetin türünü gönderiyoruz
        binding.manHairdresserButton.setOnClickListener { navigateToMainActivity("erkek_kuafor") }
        binding.womenHairdresserButton.setOnClickListener { navigateToMainActivity("kadin_kuafor") }
        binding.petGroomerButton.setOnClickListener { navigateToMainActivity("pet_kuafor") }
        binding.carWashButton.setOnClickListener { navigateToMainActivity("araba_yikama") }
        binding.childHairdresserButton.setOnClickListener { navigateToMainActivity("cocuk_kuafor") }
        binding.spaButton.setOnClickListener { navigateToMainActivity("spa_masaj") }
        binding.nailCareButton.setOnClickListener { navigateToMainActivity("tirnak_bakimi") }
        binding.skinCareButton.setOnClickListener { navigateToMainActivity("cilt_bakimi") }
    }

    // MainActivity'ye veri aktarıp geçiş yapıyoruz
    private fun navigateToMainActivity(serviceType: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("SERVICE_TYPE", serviceType)  // Seçilen hizmeti intent ile gönderiyoruz
        }
        startActivity(intent)
    }
}
