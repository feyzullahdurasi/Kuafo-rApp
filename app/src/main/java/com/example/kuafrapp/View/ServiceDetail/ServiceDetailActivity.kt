package com.example.kuafrapp.View.ServiceDetail

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kuafrapp.R
import com.example.kuafrapp.View.ReservationActivity
import com.example.kuafrapp.databinding.ActivityServiceDetailBinding
import com.example.kuafrapp.model.Business
import com.example.kuafrapp.model.Service
import com.example.kuafrapp.model.ServiceFeature
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ServiceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceDetailBinding
    private lateinit var viewModel: ServiceDetailViewModel
    private lateinit var service: Service
    private lateinit var business: Business
    private val selectedFeatures = mutableSetOf<ServiceFeature>()
    private val availableHours = (9..18).map { String.format("%02d:00", it) }
    private var totalPrice = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent'ten service ve business bilgilerini al
        service = intent.getParcelableExtra("service")!!
        business = intent.getParcelableExtra("business")!!

        viewModel = ViewModelProvider(this)[ServiceDetailViewModel::class.java]

        setupUI()
        setupObservers()
        setupListeners()
    }

    private fun setupUI() {
        // Servis bilgileri
        binding.businessName.text = business.businessName
        binding.businessAddress.text = business.businessAddress
        binding.businessPrice.text = business.businessPrice
        binding.businessHours.text = business.businessHours

        // Servis resmini yükleme
        Glide.with(this)
            .load(business.businessImage)
            .placeholder(R.drawable.barber_image_bg)
            .into(binding.businessImage)

        // Hizmet özellikleri
        setupServiceFeatures()

        // Yorumları ayarlama
        setupComments()
    }

    private fun setupServiceFeatures() {
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

    private fun setupComments() {
        /*val commentsAdapter = CommentsAdapter(business.comments)
        binding.commentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ServiceDetailActivity)
            adapter = commentsAdapter
        }*/
    }

    private fun setupObservers() {
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