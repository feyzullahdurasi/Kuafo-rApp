package com.example.kuafrapp.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.kuafrapp.databinding.ActivityBarberDetailBinding
import com.example.kuafrapp.util.dowloandImage
import com.example.kuafrapp.util.makePlaceHolder
import com.example.kuafrapp.viewmodel.BarberDetailViewModel

class BarberDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBarberDetailBinding
    private lateinit var viewModel: BarberDetailViewModel
    private var barberId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding kullanımı
        binding = ActivityBarberDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel başlatma
        viewModel = ViewModelProvider(this).get(BarberDetailViewModel::class.java)

        // Intent ile gönderilen barberId'yi al
        intent?.let {
            barberId = it.getIntExtra("barberId", 0)
        }

        // ViewModel'den veriyi çek
        viewModel.roomGetData(barberId)

        // LiveData'yı gözlemleme
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.barberLiveData.observe(this) { barber ->
            binding.barberName.text = barber.barberName
            binding.localeName.text = barber.localeName
            binding.barberImage.dowloandImage(barber.barberImage, makePlaceHolder(this))
        }
    }
}
