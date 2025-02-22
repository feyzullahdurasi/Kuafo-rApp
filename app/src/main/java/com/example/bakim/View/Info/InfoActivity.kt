package com.example.bakim.View.Info

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.bakim.View.MainActivity
import com.example.bakim.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    private val viewModel: InfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Butonlara tıklama olaylarını bağlama
        setupListeners()

        // ViewModel üzerinden hata ve yükleme durumlarını gözlemleme
        observeViewModel()
    }

    private fun setupListeners() {
        binding.manHairdresserButton.setOnClickListener { viewModel.selectService("erkek_kuafor") }
        binding.womenHairdresserButton.setOnClickListener { viewModel.selectService("kadin_kuafor") }
        binding.petGroomerButton.setOnClickListener { viewModel.selectService("pet_kuafor") }
        binding.carWashButton.setOnClickListener { viewModel.selectService("araba_yikama") }
        binding.childHairdresserButton.setOnClickListener { viewModel.selectService("cocuk_kuafor") }
        binding.spaButton.setOnClickListener { viewModel.selectService("spa_masaj") }
        binding.nailCareButton.setOnClickListener { viewModel.selectService("tirnak_bakimi") }
        binding.skinCareButton.setOnClickListener { viewModel.selectService("cilt_bakimi") }
    }

    private fun observeViewModel() {
        viewModel.navigateToMain.observe(this, Observer { serviceType ->
            serviceType?.let {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("SERVICE_TYPE", it)
                }
                startActivity(intent)
                viewModel.resetNavigation()
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.errorMessage.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
