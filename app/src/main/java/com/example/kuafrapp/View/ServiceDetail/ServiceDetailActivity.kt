package com.example.kuafrapp.View.ServiceDetail

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kuafrapp.R
import com.example.kuafrapp.View.ReservationActivity
import com.example.kuafrapp.adapter.CommentsAdapter
import com.example.kuafrapp.databinding.ActivityServiceDetailBinding
import com.example.kuafrapp.model.Service
import com.example.kuafrapp.model.ServiceFeature
import com.example.kuafrapp.model.UserComment
import com.example.kuafrapp.repository.BakimRepository
import com.example.kuafrapp.service.APIResult
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class ServiceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceDetailBinding
    private lateinit var viewModel: ServiceDetailViewModel
    private var serviceId: Int = 0
    private var businessId: Int = 0
    private val selectedFeatures = mutableSetOf<ServiceFeature>()
    private val availableHours = (9..18).map { String.format("%02d:00", it) }
    private var totalPrice = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ID'leri intent'ten al
        serviceId = intent.getIntExtra("serviceId", 0)
        businessId = intent.getIntExtra("businessId", 0)

        viewModel = ViewModelProvider(this)[ServiceDetailViewModel::class.java]

        setupObservers()
        viewModel.loadServiceDetails(serviceId, businessId)
    }

    private fun setupUI(service: Service) {
        // Servis bilgileri
        binding.businessName.text = service.business.name
        binding.businessAddress.text = service.business.address
        binding.businessPrice.text = service.business.price
        binding.businessHours.text = service.business.hours

        // Servis resmini yükleme
        Glide.with(this)
            .load(service.business.image)
            .placeholder(R.drawable.barber_image_bg)
            .into(binding.businessImage)

        // Hizmet özellikleri
        setupServiceFeatures(service)

        // Yorumları ayarlama
        //setupComments(service.business.comments)
    }

    private fun setupServiceFeatures(service: Service) {
        binding.servicesContainer.removeAllViews()
        service.serviceFeature.forEach { feature ->
            val checkBox = CheckBox(this).apply {
                text = "${feature.name} - ${feature.price}₺ (${feature.duration} dk)"
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedFeatures.add(feature)
                    } else {
                        selectedFeatures.remove(feature)
                    }
                    updateTotalPrice()
                }
            }
            binding.servicesContainer.addView(checkBox)
        }
    }

    private fun setupComments(comments: List<UserComment>) {
        val commentsAdapter = CommentsAdapter()
        binding.commentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ServiceDetailActivity)
            adapter = commentsAdapter
        }
        commentsAdapter.submitList(comments)
    }

    private fun setupObservers() {
        viewModel.serviceDetails.observe(this) { result ->
            when (result) {
                is APIResult.Success<*> -> {
                    setupUI(result.data as Service)
                    setupListeners()
                }
                is APIResult.Error -> {
                    Toast.makeText(this, result.error.userErrorMessage, Toast.LENGTH_LONG).show()
                }
                is APIResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        viewModel.showAlert.observe(this) { show ->
            if (show) {
                AlertDialog.Builder(this)
                    .setTitle("Bilgilendirme")
                    .setMessage(viewModel.alertMessage.value)
                    .setPositiveButton("Tamam") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }

    private fun setupListeners() {
        // Tarih seçimi
        binding.datePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    binding.dateText.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.minDate = System.currentTimeMillis() - 1000
            }.show()
        }

        // Saat seçimi
        binding.timePicker.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Saat Seç")
                .setItems(availableHours.toTypedArray()) { _, which ->
                    binding.timeText.text = availableHours[which]
                }
                .show()
        }

        // Yorum ekleme
        binding.addCommentButton.setOnClickListener {
            val commentText = binding.commentEditText.text.toString()
            if (commentText.isNotEmpty()) {
                viewModel.addUserComment(commentText)
                binding.commentEditText.text.clear()
            } else {
                Toast.makeText(this, "Yorum boş olamaz", Toast.LENGTH_SHORT).show()
            }
        }

        // Randevu alma
        binding.makeAppointmentButton.setOnClickListener {
            if (selectedFeatures.isEmpty()) {
                viewModel.showAlert("Lütfen en az bir hizmet seçin")
            } else {
                // Randevu sayfasına geçiş
                val intent = Intent(this, ReservationActivity::class.java).apply {
                    putExtra("selectedFeatures", ArrayList(selectedFeatures))
                    putExtra("selectedDate", binding.dateText.text.toString())
                    putExtra("selectedTime", binding.timeText.text.toString())
                    putExtra("totalPrice", totalPrice)
                }
                startActivity(intent)
            }
        }
    }

    private fun updateTotalPrice() {
        totalPrice = selectedFeatures.sumOf { it.price.toInt() }
        binding.totalPriceText.text = "${totalPrice}₺"
    }
}

class ServiceDetailViewModelFactory @Inject constructor(
    private val repository: BakimRepository
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceDetailViewModel::class.java)) {
            return ServiceDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}